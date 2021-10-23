package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.entity.PokerSession;

import java.util.Optional;

public interface PokerSessionRepository {

    PokerSession createOrGetPokerSession(String sessionId);

    Optional<PokerSession> getPokerSession(String sessionId);

    void updatePokerSession(PokerSession pokerSession);

    PokerSession deletePokerSession(String sessionId);
}
