package com.ktviv.pointpoker.app.controller;

import com.ktviv.pointpoker.app.exceptions.UserNotAuthenticatedException;
import com.ktviv.pointpoker.app.http.requests.VoteRequest;
import com.ktviv.pointpoker.app.http.responses.CreateSessionResponse;
import com.ktviv.pointpoker.app.security.AuthenticationFacade;
import com.ktviv.pointpoker.app.utils.ApplicationUserMapper;
import com.ktviv.pointpoker.app.utils.UserVoteResponseMapper;
import com.ktviv.pointpoker.domain.broker.EventQueueBlock;
import com.ktviv.pointpoker.domain.entity.ApplicationUser;
import com.ktviv.pointpoker.domain.entity.Participant;
import com.ktviv.pointpoker.domain.entity.SessionUser;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import com.ktviv.pointpoker.domain.results.CreateSessionResult;
import com.ktviv.pointpoker.domain.service.PointPokerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin("http://localhost:4200")
@Slf4j
public class ServiceController {

    private final PointPokerService pointPokerService;
    private final AuthenticationFacade authenticationFacade;
    private final SseEmitter sseEmitter;
    private final ExecutorService executorService;

    @Autowired
    public ServiceController(PointPokerService pointPokerService, AuthenticationFacade authenticationFacade, SseEmitter sseEmitter, ExecutorService executorService) {

        this.pointPokerService = pointPokerService;
        this.authenticationFacade = authenticationFacade;
        this.sseEmitter = sseEmitter;
        this.executorService = executorService;
    }


    @PostMapping
    public ResponseEntity<?> createSession() throws UserNotAuthenticatedException {

        CreateSessionResult createSessionResult = pointPokerService.createSession(UUID.randomUUID().toString(), getParticipant(getSessionUser()));
        return ResponseEntity.ok(new CreateSessionResponse(createSessionResult.getSessionId()));
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<?> joinSession(@PathVariable String sessionId) throws UserNotAuthenticatedException, PokerSessionNotFoundException {

        pointPokerService.joinSession(sessionId, getParticipant(getSessionUser()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/vote")
    public ResponseEntity<?> vote(@PathVariable String sessionId, @RequestBody VoteRequest voteRequest) throws UserNotAuthenticatedException, PokerSessionNotFoundException, UnAssociatedParticipantException {

        pointPokerService.vote(sessionId, getParticipant(getSessionUser(), voteRequest.getStoryPoint()));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{sessionId}/reset")
    public ResponseEntity<?> reset(@PathVariable String sessionId) throws UserNotAuthenticatedException, PokerSessionNotFoundException, UnAssociatedParticipantException {

        pointPokerService.reset(sessionId, getParticipant(getSessionUser()));
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{sessionId}/vote", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchVotesNew(@PathVariable String sessionId) throws UserNotAuthenticatedException, PokerSessionNotFoundException, UnAssociatedParticipantException {

        final SseEmitter emitter = new SseEmitter(120000L);
        emitter.onCompletion(() -> log.info("SseEmitter is completed for session {}", sessionId));
        emitter.onTimeout(() -> log.info("SseEmitter is timed out for session {}", sessionId));
        emitter.onError((ex) -> log.info(String.format("SseEmitter got error for session %s", sessionId), ex));
        EventQueueBlock eventQueueBlock = this.pointPokerService.getSessionVotes(sessionId, getParticipant(getSessionUser()));
        this.executorService.execute(() -> {
            try {
                while (eventQueueBlock.isActive()) {

                    PokerEvent pokerEvent = eventQueueBlock.getEvents().take();
                    if (pokerEvent instanceof UserVotedEvent) {

                        emitter.send(UserVoteResponseMapper.from((UserVotedEvent) pokerEvent));
                    } else if (pokerEvent instanceof VoteResetEvent) {

                        emitter.send(UserVoteResponseMapper.from((VoteResetEvent) pokerEvent));
                        emitter.complete();
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {

                log.error("Error whilst emitting user-vote-events for sessionId {}", sessionId);
                log.error("SSE Emitter failed to send events", e);
                emitter.completeWithError(e);
            }

        });
        return emitter;
    }


    private SessionUser getSessionUser() throws UserNotAuthenticatedException {

        Optional<ApplicationUser> applicationUser = this.authenticationFacade.getCurrentAuthenticatedUser();
        return applicationUser.map(ApplicationUserMapper::fromApplicationUser).orElseThrow(UserNotAuthenticatedException::new);
    }

    private Participant getParticipant(SessionUser sessionUser) {

        return Participant.of(sessionUser.getId(), sessionUser.getDisplayName(), Optional.empty());
    }

    private Participant getParticipant(SessionUser sessionUser, float storyPoint) {

        return Participant.of(sessionUser.getId(), sessionUser.getDisplayName(), Optional.of(storyPoint));
    }


}
