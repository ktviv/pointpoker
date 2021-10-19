package com.ktviv.pointpoker.domain.events;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public abstract class PokerEvent implements Comparable<PokerEvent> {

    private final String eventId;
    private final String type;
    private final String sessionId;
    private final String userId;
    private final Long timestamp;

    public PokerEvent(String sessionId, String userId, long timestamp) {

        this.eventId = UUID.randomUUID().toString();

        this.type = this.getType();

        this.sessionId = sessionId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public abstract String getType();

    @Override
    public int compareTo(PokerEvent o) {

        return this.timestamp.compareTo(o.getTimestamp());
    }
}
