package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class VoteResetEvent extends PokerEvent {

    public static final String TYPE = "vote-reset";

    public VoteResetEvent(Object source, String sessionId, String userId, long timestamp) {
        super(source, sessionId, userId, timestamp);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
