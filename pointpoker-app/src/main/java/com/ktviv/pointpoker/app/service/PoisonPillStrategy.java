package com.ktviv.pointpoker.app.service;

import com.ktviv.pointpoker.app.utils.UserVoteResponseMapper;
import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.service.BaseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
@Slf4j
public class PoisonPillStrategy extends BaseStrategy {


    @Autowired
    public PoisonPillStrategy(@Qualifier("cachedThreadPoolExecutorService") ExecutorService executorService) {

        super(executorService);
    }

    @Override
    public SseEmitter execute(String sessionId, String userId, EventQueue eventQueue) {

        final SseEmitter emitter = new SseEmitter(120000L);
        emitter.onCompletion(emitterOnCompletionRunnable(sessionId, userId));
        emitter.onTimeout(emitterOnTimeoutRunnable(sessionId, userId));
        emitter.onError(emitterOnError(sessionId, userId));
        getExecutorService().execute(executionRunnable(sessionId, userId, eventQueue, emitter));
        return emitter;
    }

    private Runnable emitterOnCompletionRunnable(final String sessionId, final String userId) {

        return () -> log.info("SseEmitter is completed for session {} and user {}", sessionId, userId);
    }

    private Runnable emitterOnTimeoutRunnable(final String sessionId, final String userId) {

        return () -> log.info("SseEmitter is timed out for session {} and user {}", sessionId, userId);
    }

    private Consumer<Throwable> emitterOnError(final String sessionId, final String userId) {

        return (ex) -> log.info(String.format("SseEmitter erred for session %s and user %s", sessionId, userId), ex);
    }

    private Runnable executionRunnable(final String sessionId, final String userId, final EventQueue eventQueue, final SseEmitter emitter) {

        return () -> {
            try {
                while (eventQueue.isActive()) {

                    PokerEvent pokerEvent = eventQueue.getEvents().take();
                    if (pokerEvent instanceof UserVotedEvent) {

                        emitter.send(UserVoteResponseMapper.from((UserVotedEvent) pokerEvent));
                    } else if (pokerEvent instanceof VoteResetEvent) {

                        emitter.send(UserVoteResponseMapper.from((VoteResetEvent) pokerEvent));
                        emitter.complete();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                log.error(String.format("Take from eventQueueBlock events was interrupted for session %s and user %s", sessionId, userId), e);
                emitter.completeWithError(e);
            } catch (IOException e) {
                log.error(String.format("Failed to send event using emitter instance for session %s and user %s", sessionId, userId), e);
                emitter.completeWithError(e);
            }
        };
    }
}

