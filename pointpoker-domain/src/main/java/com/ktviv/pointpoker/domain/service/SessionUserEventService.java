package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

public interface SessionUserEventService {

    void createUserEventQueue(String sessionId, String userId, EventQueue eventQueue);

    Optional<EventQueue> getUserEventQueue(String sessionId, String userId);

    void clearUserEventQueue(String sessionId, String userId);

    void clearSession(String sessionId);

    Optional<SseEmitter> getEventEmitter(String sessionId, String userId, SessionEventBroadcastStrategy strategy);

    void broadcastEvent(PokerEvent pokerEvent) throws PokerSessionNotFoundException;
}
