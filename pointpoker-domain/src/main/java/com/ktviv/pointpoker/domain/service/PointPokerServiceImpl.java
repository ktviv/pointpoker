package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.entity.Participant;
import com.ktviv.pointpoker.domain.entity.PokerSession;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.SessionAutoTerminatedEvent;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.events.UserExitedEvent;
import com.ktviv.pointpoker.domain.events.UserJoinedEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import com.ktviv.pointpoker.domain.results.CreateSessionResult;
import com.ktviv.pointpoker.domain.service.config.PointPokerServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@Service
@Slf4j
public class PointPokerServiceImpl implements PointPokerService {

    private final PointPokerServiceConfig pointPokerServiceConfig;

    @Autowired
    public PointPokerServiceImpl(PointPokerServiceConfig pointPokerServiceConfig) {

        this.pointPokerServiceConfig = pointPokerServiceConfig;
    }

    @Override
    public CreateSessionResult createSession(String sessionId, Participant participant) {

        //Create a new session
        PokerSession pokerSession = this.pointPokerServiceConfig.getPokerSessionRepository().createOrGetPokerSession(sessionId);
        //Add creator to session
        addParticipantToSession(pokerSession, participant);
        //Publish event
        publishEvent(new SessionCreatedEvent(this, sessionId, participant.getUserId(), now()));
        //Return result
        return CreateSessionResult.of(pokerSession.getSessionId());
    }

    @Override
    public void joinSession(String sessionId, Participant participant) throws PokerSessionNotFoundException {

        //Fetch existing session
        getPokerSession(sessionId).map(session -> {

            //Add participant to session
            addParticipantToSession(session, participant);
            //Publish event
            publishEvent(new UserJoinedEvent(this, sessionId, participant.getUserId(), now()));
            return session;
        }).orElseThrow(PokerSessionNotFoundException::new);
    }

    @Override
    public void vote(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        //Fetch existing session
        PokerSession pokerSession = getValidSession(sessionId, participant);
        //Update participant
        pokerSession.updateParticipant(participant.getUserId(), participant);
        //Publish event
        publishEvent(new UserVotedEvent(this, sessionId, participant.getUserId(), participant.getVotePoint().orElse(-1f), participant.getDisplayName(), now()));
    }

    @Override
    public void reset(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        //Fetch existing session
        PokerSession pokerSession = getValidSession(sessionId, participant);
        //Reset votes
        pokerSession.resetVotes();
        //Publish event
        publishEvent(new VoteResetEvent(this, sessionId, participant.getUserId(), now()));
    }

    @Override
    public void exitSession(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        //Fetch existing session
        PokerSession pokerSession = getValidSession(sessionId, participant);
        //Exit from session
        pokerSession.removeParticipant(participant);
        //Publish event
        publishEvent(new UserExitedEvent(this, sessionId, participant.getUserId(), now()));
        //If last user exited then terminate the session
        if (pokerSession.getParticipants().isEmpty()) {

            log.info("No more participants exist for session {} and last user to exit {}. Terminating the session!!!.", sessionId, participant.getUserId());
            removePokerSession(sessionId);
            publishEvent(new SessionAutoTerminatedEvent(this, sessionId, now()));
        }
    }

    @Override
    public SseEmitter broadcastEvents(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        //Fetch existing session
        PokerSession pokerSession = getValidSession(sessionId, participant);
        //Fetch the emitter
        return getEmitter(sessionId, participant.getUserId()).orElseThrow(() -> new RuntimeException("Emitter unavailable"));
    }

    private PokerSession getValidSession(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        Optional<PokerSession> pokerSessionOptional = getPokerSession(sessionId);
        if (!pokerSessionOptional.isPresent()) {

            throw new PokerSessionNotFoundException();
        } else if (!pokerSessionOptional.get().getParticipants().containsKey(participant.getUserId())) {

            throw new UnAssociatedParticipantException();
        }
        return pokerSessionOptional.get();
    }

    private Long now() {

        return this.pointPokerServiceConfig.getClock().millis();
    }

    private void publishEvent(PokerEvent pokerEvent) {

        log.info("Publishing event : {}", pokerEvent);
        this.pointPokerServiceConfig
                .getApplicationEventMulticaster()
                .multicastEvent(pokerEvent);
    }

    private Optional<PokerSession> getPokerSession(String sessionId) {

        return this.pointPokerServiceConfig.getPokerSessionRepository().getPokerSession(sessionId);
    }

    private void removePokerSession(String sessionId) {

        this.pointPokerServiceConfig.getPokerSessionRepository().deletePokerSession(sessionId);
    }

    private void addParticipantToSession(PokerSession pokerSession, Participant participant) {

        log.info("Adding/Updating participant {} (name = {}) to session {}", participant.getUserId(), participant.getDisplayName(), pokerSession.getSessionId());
        pokerSession.getParticipants().put(participant.getUserId(), participant);
    }

    private Optional<SseEmitter> getEmitter(String sessionId, String userId) {

        return this.pointPokerServiceConfig
                .getSessionUserEventService()
                .getEventEmitter(sessionId, userId, this.pointPokerServiceConfig.getSessionEventBroadcastStrategy());
    }
}
