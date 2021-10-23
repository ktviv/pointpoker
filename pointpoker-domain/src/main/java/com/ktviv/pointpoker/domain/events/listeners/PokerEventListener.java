package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PokerEventListener {

    private EventHandlerFactory eventHandlerFactory;

    @Autowired
    public PokerEventListener(EventHandlerFactory eventHandlerFactory) {

        this.eventHandlerFactory = eventHandlerFactory;
    }

    @EventListener(classes = {PokerEvent.class})
    public void handlePokerEvents(PokerEvent pokerEvent) {

        eventHandlerFactory
                .getPokerEventHandler(pokerEvent.getType())
                .handle(pokerEvent);
    }
}
