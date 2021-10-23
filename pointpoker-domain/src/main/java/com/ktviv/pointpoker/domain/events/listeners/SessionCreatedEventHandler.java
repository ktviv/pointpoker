package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionCreatedEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public SessionCreatedEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }


    @Override
    public void handle(PokerEvent pokerEvent) {

        SessionCreatedEvent sessionCreatedEvent = (SessionCreatedEvent) pokerEvent;
        log.info("Handling sessionCreatedEvent {}", sessionCreatedEvent);
        sessionUserEventService.createUserEventQueue(sessionCreatedEvent.getSessionId(), sessionCreatedEvent.getUserId(), new EventQueue(true));
    }

    @Override
    public String getHandlerType() {

        return SessionCreatedEvent.TYPE;
    }
}
