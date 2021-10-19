package com.ktviv.pointpoker.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public class Participant {

    private final String userId;
    private final String displayName;
    private final Optional<Float> votePoint;

    private Participant(String userId, String displayName, Optional<Float> votePoint) {

        this.userId = userId;
        this.displayName = displayName;
        this.votePoint = votePoint;
    }

    public static Participant of(String userId, String displayName, Optional<Float> votePoint) {

        return new Participant(userId, displayName, votePoint);
    }
}
