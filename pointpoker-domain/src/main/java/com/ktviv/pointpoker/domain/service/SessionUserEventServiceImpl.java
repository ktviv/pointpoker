package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SessionUserEventServiceImpl implements SessionUserEventService {

    private Map<String, Map<String, EventQueue>> sessionUserEventMap;

    public SessionUserEventServiceImpl() {

        this.sessionUserEventMap = new ConcurrentHashMap<>();
    }


    @Override
    public void createUserEventQueue(String sessionId, String userId, EventQueue eventQueue) {

        if (!this.sessionUserEventMap.containsKey(sessionId)) {

            Map<String, EventQueue> userEventMap = new ConcurrentHashMap<>();
            userEventMap.put(userId, eventQueue);
            this.sessionUserEventMap.put(sessionId, userEventMap);
        } else {

            Map<String, EventQueue> userEventMap = this.sessionUserEventMap.get(sessionId);
            if (userEventMap.containsKey(userId)) {

                userEventMap.get(userId).clearAll();
                userEventMap.remove(userId);
            }
            userEventMap.put(userId, eventQueue);
        }
    }

    @Override
    public Optional<EventQueue> getUserEventQueue(String sessionId, String userId) {

        if (this.sessionUserEventMap.containsKey(sessionId) && this.sessionUserEventMap.get(sessionId).containsKey(userId)) {

            return Optional.of(this.sessionUserEventMap.get(sessionId).get(userId));
        }
        return Optional.empty();
    }

    @Override
    public void clearUserEventQueue(String sessionId, String userId) {

        if (this.sessionUserEventMap.containsKey(sessionId) && this.sessionUserEventMap.get(sessionId).containsKey(userId)) {

            this.sessionUserEventMap.get(sessionId).get(userId).clearAll();
            this.sessionUserEventMap.get(sessionId).remove(userId);
        }
    }

    @Override
    public void clearSession(String sessionId) {

        if (this.sessionUserEventMap.containsKey(sessionId)) {

            Map<String, EventQueue> userEventMap = this.sessionUserEventMap.get(sessionId);
            if (!userEventMap.isEmpty()) {
                userEventMap.values().forEach(EventQueue::clearAll);
                userEventMap.clear();
            }
            this.sessionUserEventMap.remove(sessionId);
        }
    }

    @Override
    public Optional<SseEmitter> getEventEmitter(String sessionId, String userId, SessionEventBroadcastStrategy strategy) {

        if (this.sessionUserEventMap.containsKey(sessionId) && this.sessionUserEventMap.get(sessionId).containsKey(userId)) {

            return Optional.of(strategy.execute(sessionId, userId, this.sessionUserEventMap.get(sessionId).get(userId)));
        }
        log.warn("Cannot build SseEmitter. Either session {} or user {} is invalid or doesn't exist", sessionId, userId);
        return Optional.empty();
    }

    @Override
    public void broadcastEvent(PokerEvent pokerEvent) throws PokerSessionNotFoundException {

        if (!this.sessionUserEventMap.containsKey(pokerEvent.getSessionId())) {

            log.error("Cannot broadcast. Session with id {} doesn't exist", pokerEvent.getSessionId());
            throw new PokerSessionNotFoundException();
        }
        Map<String, EventQueue> userEventMap = this.sessionUserEventMap.get(pokerEvent.getSessionId());
        userEventMap.values().forEach(e -> e.addEvent(pokerEvent));
    }
}

