package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class SessionCreatedEvent extends PokerEvent {

    public static final String TYPE = "session-created";

    public SessionCreatedEvent(Object source, String sessionId, String userId, long timestamp) {

        super(source, sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
