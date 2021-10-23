package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.SessionAutoTerminatedEvent;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionAutoTerminatedEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public SessionAutoTerminatedEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }


    @Override
    public void handle(PokerEvent pokerEvent) {

        SessionAutoTerminatedEvent sessionAutoTerminatedEvent = (SessionAutoTerminatedEvent) pokerEvent;
        log.info("Handling sessionAutoTerminatedEvent {}", sessionAutoTerminatedEvent);
        sessionUserEventService.clearSession(sessionAutoTerminatedEvent.getSessionId());
    }

    @Override
    public String getHandlerType() {

        return SessionCreatedEvent.TYPE;
    }
}
