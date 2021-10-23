package com.ktviv.pointpoker.domain.events;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@ToString
public abstract class PokerEvent extends ApplicationEvent implements Comparable<PokerEvent> {

    private final String eventId;
    private final String type;
    private final String sessionId;
    private final String userId;
    private final Long eventTimeStamp;

    public PokerEvent(Object source, String sessionId, String userId, long eventTimeStamp) {

        super(source);
        this.eventId = UUID.randomUUID().toString();
        this.type = this.getType();
        this.sessionId = sessionId;
        this.userId = userId;
        this.eventTimeStamp = eventTimeStamp;
    }

    public abstract String getType();

    @Override
    public int compareTo(PokerEvent o) {

        return this.eventTimeStamp.compareTo(o.getEventTimeStamp());
    }
}
