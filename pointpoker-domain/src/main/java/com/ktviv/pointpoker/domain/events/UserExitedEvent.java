package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class UserExitedEvent extends PokerEvent {

    public static final String TYPE = "user-exited";

    public UserExitedEvent(Object source, String sessionId, String userId, long timestamp) {

        super(source, sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
