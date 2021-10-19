package com.ktviv.pointpoker.domain.entity;

import com.ktviv.pointpoker.domain.events.PokerEvent;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
@ToString
public class PokerSession {

    private String sessionId;
    private Map<String, Participant> participants;
    private List<PokerEvent> events;

    public PokerSession(String sessionId) {

        this.sessionId = sessionId;
        this.participants = new ConcurrentHashMap<>();
        this.events = new CopyOnWriteArrayList<>();
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
