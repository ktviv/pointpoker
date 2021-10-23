package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.EventQueue;
import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.UserJoinedEvent;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserJoinedEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public UserJoinedEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }


    @Override
    public void handle(PokerEvent pokerEvent) {

        UserJoinedEvent userJoinedEvent = (UserJoinedEvent) pokerEvent;
        log.info("Handling userJoinedEvent {}", userJoinedEvent);
        sessionUserEventService.createUserEventQueue(userJoinedEvent.getSessionId(), userJoinedEvent.getUserId(), new EventQueue(true));
    }

    @Override
    public String getHandlerType() {

        return UserJoinedEvent.TYPE;
    }
}
