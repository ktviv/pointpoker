package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.entity.Participant;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import com.ktviv.pointpoker.domain.results.CreateSessionResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface PointPokerService {

    CreateSessionResult createSession(String sessionId, Participant participant);

    void joinSession(String sessionId, Participant participant) throws PokerSessionNotFoundException;

    void vote(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    void reset(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    void exitSession(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;

    SseEmitter broadcastEvents(String sessionId, Participant participant) throws PokerSessionNotFoundException, UnAssociatedParticipantException;
}
