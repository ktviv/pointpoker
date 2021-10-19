package com.ktviv.pointpoker.domain.broker;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventProcessor {

    private Map<String, EventQueueBlock> sessionEventsMap = new ConcurrentHashMap<>();


    public void addSessionQueue(String sessionId, EventQueueBlock eventQueueBlock) {

        this.sessionEventsMap.put(sessionId, eventQueueBlock);
    }

    public EventQueueBlock getSessionQueueBlock(String sessionId) {

        return this.sessionEventsMap.get(sessionId);
    }

    public void clearSessionQueueBlock(String sessionId) {

        EventQueueBlock eventQueueBlock = this.sessionEventsMap.get(sessionId);
        eventQueueBlock.clearEvents();
        this.sessionEventsMap.remove(sessionId);
    }

}
