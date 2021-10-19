package com.ktviv.pointpoker.infra.db.repo;

import com.ktviv.pointpoker.domain.entity.PokerSession;
import com.ktviv.pointpoker.domain.service.PokerSessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPokerSessionRepositoryImpl implements PokerSessionRepository {

    private final Map<String, PokerSession> pokerSessions = new ConcurrentHashMap<>();

    @Override
    public PokerSession createOrGetPokerSession(String sessionId) {

        return this.pokerSessions.computeIfAbsent(sessionId, PokerSession::new);
    }

    @Override
    public Optional<PokerSession> getPokerSession(String sessionId) {

        return Optional.ofNullable(pokerSessions.get(sessionId));
    }

    @Override
    public void updatePokerSession(PokerSession pokerSession) {

        pokerSessions.put(pokerSession.getSessionId(), pokerSession);
    }
}
