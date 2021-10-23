package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserVotedEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public UserVotedEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }


    @Override
    public void handle(PokerEvent pokerEvent) {

        try {

            UserVotedEvent userVotedEvent = (UserVotedEvent) pokerEvent;
            log.info("Handling userVotedEvent {}", userVotedEvent);
            sessionUserEventService.broadcastEvent(userVotedEvent);
        } catch (PokerSessionNotFoundException e) {

            log.error(String.format("Cannot handle userVotedEvent. Session with id %s not found", pokerEvent.getSessionId()), e);
        }
    }

    @Override
    public String getHandlerType() {

        return UserVotedEvent.TYPE;
    }
}
