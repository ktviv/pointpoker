package com.ktviv.pointpoker.domain.events.listeners;

import com.ktviv.pointpoker.domain.events.SessionAutoTerminatedEvent;
import com.ktviv.pointpoker.domain.events.SessionCreatedEvent;
import com.ktviv.pointpoker.domain.events.UserExitedEvent;
import com.ktviv.pointpoker.domain.events.UserJoinedEvent;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventHandlerFactory {

    private final Map<String, PokerEventHandler> eventHandlerMap = new HashMap<>();

    @Autowired
    public EventHandlerFactory(SessionUserEventService sessionUserEventService) {


        this.eventHandlerMap.put(SessionCreatedEvent.TYPE, new SessionCreatedEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(UserJoinedEvent.TYPE, new UserJoinedEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(UserVotedEvent.TYPE, new UserVotedEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(VoteResetEvent.TYPE, new VoteResetEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(UserExitedEvent.TYPE, new UserExitedEventEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(SessionAutoTerminatedEvent.TYPE, new SessionAutoTerminatedEventHandler(sessionUserEventService));
        this.eventHandlerMap.put(Drainer.DRAINER_HANDLER_TYPE, new Drainer());
    }

    public PokerEventHandler getPokerEventHandler(String handlerType) {

        if (eventHandlerMap.containsKey(handlerType)) {

            return eventHandlerMap.get(handlerType);
        } else {

            return eventHandlerMap.get(Drainer.DRAINER_HANDLER_TYPE);
        }
    }

}
