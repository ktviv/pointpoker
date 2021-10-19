package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class VoteResetEvent extends PokerEvent {

    private static final String TYPE = "vote-reset";

    public VoteResetEvent(String sessionId, String userId, long timestamp) {
        super(sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
