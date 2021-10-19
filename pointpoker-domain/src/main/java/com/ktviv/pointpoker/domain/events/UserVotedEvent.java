package com.ktviv.pointpoker.domain.events;

import lombok.ToString;

@ToString
public class UserVotedEvent extends PokerEvent{

    private static final String TYPE = "user-voted";
    private String displayName;
    private float storyPoint;


    public UserVotedEvent(String sessionId, String userId, float storyPoint, String displayName, long timestamp) {
        super(sessionId, userId, timestamp);
        this.storyPoint = storyPoint;
        this.displayName = displayName;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public float getStoryPoint() {
        return this.storyPoint;
    }

    public String getDisplayName() {
        return displayName;
    }
}
