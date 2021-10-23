package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Drainer implements PokerEventHandler {

    public static final String DRAINER_HANDLER_TYPE = "drainer";

    @Override
    public void handle(PokerEvent pokerEvent) {

        log.info("Draining event : " + pokerEvent.toString());
    }

    @Override
    public String getHandlerType() {

        return DRAINER_HANDLER_TYPE;
    }
}
