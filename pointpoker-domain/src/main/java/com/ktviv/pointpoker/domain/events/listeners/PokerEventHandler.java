package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;

public interface PokerEventHandler {

    void handle(PokerEvent pokerEvent);

    String getHandlerType();
}
