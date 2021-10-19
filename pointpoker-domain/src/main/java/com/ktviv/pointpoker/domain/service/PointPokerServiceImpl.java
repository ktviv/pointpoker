package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.broker.EventProcessor;
import com.ktviv.pointpoker.domain.broker.EventQueueBlock;
import com.ktviv.pointpoker.domain.entity.Participant;
import com.ktviv.pointpoker.domain.entity.PokerSession;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.events.UserJoinedEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import com.ktviv.pointpoker.domain.results.CreateSessionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Optional;

@Service
public class PointPokerServiceImpl implements PointPokerService {

    private final PokerSessionRepository pokerSessionRepository;
    private final EventProcessor eventProcessor;
    private final Clock clock;

    @Autowired
    public PointPokerServiceImpl(PokerSessionRepository pokerSessionRepository, EventProcessor eventProcessor, Clock clock) {

        this.pokerSessionRepository = pokerSessionRepository;
        this.eventProcessor = eventProcessor;
        this.clock = clock;
    }

    @Override
    public CreateSessionResult createSession(String sessionId, Participant participant) {

        PokerSession pokerSession = this.pokerSessionRepository.createOrGetPokerSession(sessionId);
        pokerSession.getParticipants().put(participant.getUserId(), participant);
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionId, participant.getUserId(), clock.millis());
        pokerSession.getEvents().add(sessionCreatedEvent);

        EventQueueBlock eventQueueBlock = new EventQueueBlock();
        eventQueueBlock.enable();
        this.eventProcessor.addSessionQueue(sessionId, eventQueueBlock);
        return CreateSessionResult.of(pokerSession.getSessionId());
    }

    @Override
    public void joinSession(String sessionId, Participant participant) throws PokerSessionNotFoundException {

        this.pokerSessionRepository.getPokerSession(sessionId).map(session -> {

            session.getParticipants().put(participant.getUserId(), participant);
            UserJoinedEvent userJoinedEvent = new UserJoinedEvent(sessionId, participant.getUserId(), clock.millis());
            session.getEvents().add(userJoinedEvent);
            return session;
        }).orElseThrow(PokerSessionNotFoundException::new);
    }

    @Override
    public void vote(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        Optional<PokerSession> pokerSessionOptional = this.pokerSessionRepository.getPokerSession(sessionId);
        if (!pokerSessionOptional.isPresent()) {

            throw new PokerSessionNotFoundException();
        } else if (!pokerSessionOptional.get().getParticipants().containsKey(participant.getUserId())) {

            throw new UnAssociatedParticipantException();
        }
        pokerSessionOptional.get().updateParticipant(participant.getUserId(), participant);
        UserVotedEvent userVotedEvent = new UserVotedEvent(sessionId, participant.getUserId(), participant.getVotePoint().orElse(-1f), participant.getDisplayName(), clock.millis());
        pokerSessionOptional.get().getEvents().add(userVotedEvent);
        EventQueueBlock eventQueueBlock = this.eventProcessor.getSessionQueueBlock(sessionId);
        eventQueueBlock.enable();
        eventQueueBlock.addEvent(userVotedEvent);
    }

    @Override
    public void reset(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

        Optional<PokerSession> pokerSessionOptional = this.pokerSessionRepository.getPokerSession(sessionId);
        if (!pokerSessionOptional.isPresent()) {

            throw new PokerSessionNotFoundException();
        } else if (!pokerSessionOptional.get().getParticipants().containsKey(participant.getUserId())) {

            throw new UnAssociatedParticipantException();
        }
        pokerSessionOptional.get().resetVotes();
        VoteResetEvent voteResetEvent = new VoteResetEvent(sessionId, participant.getUserId(), clock.millis());
        pokerSessionOptional.get().getEvents().add(voteResetEvent);
        EventQueueBlock eventQueueBlock = this.eventProcessor.getSessionQueueBlock(sessionId);
        ;
        eventQueueBlock.addEvent(voteResetEvent);
//        eventQueueBlock.clearEvents();
//        eventQueueBlock.disable();
    }

    @Override
    public EventQueueBlock getSessionVotes(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {
        Optional<PokerSession> pokerSessionOptional = this.pokerSessionRepository.getPokerSession(sessionId);
        if (!pokerSessionOptional.isPresent()) {

            throw new PokerSessionNotFoundException();
        } else if (!pokerSessionOptional.get().getParticipants().containsKey(participant.getUserId())) {

            throw new UnAssociatedParticipantException();
        }
        return this.eventProcessor.getSessionQueueBlock(sessionId);
    }

    @Override
    public void deleteSession(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException {

    }
}
