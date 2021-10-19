package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.broker.EventQueueBlock;
import com.ktviv.pointpoker.domain.entity.Participant;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import com.ktviv.pointpoker.domain.results.CreateSessionResult;

public interface PointPokerService {

    CreateSessionResult createSession(String sessionId, Participant participant);

    void joinSession(String sessionId, Participant participant) throws PokerSessionNotFoundException;

    void vote(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    void reset(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    EventQueueBlock getSessionVotes(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    void deleteSession(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;
}
