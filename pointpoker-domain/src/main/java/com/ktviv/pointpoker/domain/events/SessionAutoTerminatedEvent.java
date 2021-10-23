package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class SessionAutoTerminatedEvent extends PokerEvent {

    public static final String TYPE = "session-auto-terminated";
    public static final String USER = "auto";

    public SessionAutoTerminatedEvent(Object source, String sessionId, long timestamp) {

        super(source, sessionId, USER, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
