package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.events.UserExitedEvent;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserExitedEventEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public UserExitedEventEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }


    @Override
    public void handle(PokerEvent pokerEvent) {

        UserExitedEvent userExitedEvent = (UserExitedEvent) pokerEvent;
        log.info("Handling userExitedEvent {}", userExitedEvent);
        sessionUserEventService.clearUserEventQueue(userExitedEvent.getSessionId(), userExitedEvent.getSessionId());
    }

    @Override
    public String getHandlerType() {

        return SessionCreatedEvent.TYPE;
    }
}
