package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoteResetEventHandler implements PokerEventHandler {

    private final SessionUserEventService sessionUserEventService;

    public VoteResetEventHandler(SessionUserEventService sessionUserEventService) {

        this.sessionUserEventService = sessionUserEventService;
    }

    @Override
    public void handle(PokerEvent pokerEvent) {

        try {

            VoteResetEvent voteResetEvent = (VoteResetEvent) pokerEvent;
            log.info("Handling voteResetEvent {}", voteResetEvent);
            sessionUserEventService.broadcastEvent(voteResetEvent);
        } catch (PokerSessionNotFoundException e) {

            log.error(String.format("Cannot handle voteResetEvent. Session with id %s not found", pokerEvent.getSessionId()), e);
        }
    }

    @Override
    public String getHandlerType() {

        return VoteResetEvent.TYPE;
    }
}
