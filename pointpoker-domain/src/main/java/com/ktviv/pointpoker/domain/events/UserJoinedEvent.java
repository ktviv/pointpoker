package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class UserJoinedEvent extends PokerEvent {

    private static final String TYPE = "user-joined";

    public UserJoinedEvent(String sessionId, String userId, long timestamp) {
        super(sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
