package com.ktviv.pointpoker.domain.broker;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

//@Component
public class EventBroadcaster {

//    @SendTo("/topic/news")
//    public String broadcastNews(@Payload PokerEvent pokerEvent) {
//
//        return pokerEvent.toString();
//    }
}
