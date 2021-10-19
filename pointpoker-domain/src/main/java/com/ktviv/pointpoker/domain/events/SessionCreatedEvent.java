package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class SessionCreatedEvent extends PokerEvent {

    private static final String TYPE = "session-created";

    public SessionCreatedEvent(String sessionId, String userId, long timestamp) {

        super(sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
