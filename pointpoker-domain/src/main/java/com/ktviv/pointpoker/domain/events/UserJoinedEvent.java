package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class UserJoinedEvent extends PokerEvent {

    public static final String TYPE = "user-joined";

    public UserJoinedEvent(Object source, String sessionId, String userId, long timestamp) {
        super(source, sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
