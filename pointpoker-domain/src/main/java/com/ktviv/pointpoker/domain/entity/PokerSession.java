package com.ktviv.pointpoker.domain.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@ToString
public class PokerSession {

    private final String sessionId;
    private final Map<String, Participant> participants;

    public PokerSession(String sessionId) {

        this.sessionId = sessionId;
        this.participants = new ConcurrentHashMap<>();
    }

    public void updateParticipant(String userId, Participant participant) {

        this.participants.put(userId, participant);
    }

    public void resetVotes() {

        Set<Participant> participantSet = this.participants
                .values()
                .stream()
                .map(v -> Participant.of(v.getUserId(), v.getDisplayName(), Optional.empty()))
                .collect(Collectors.toSet());
        participantSet
                .forEach(p -> this.updateParticipant(p.getUserId(), p));
    }

    public void removeParticipant(Participant participant) {

        this.participants.remove(participant.getUserId());
    }
}
