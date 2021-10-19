package com.ktviv.pointpoker.domain.results;

import lombok.Data;

@Data
public final class CreateSessionResult {

    private String sessionId;

    private CreateSessionResult(String sessionId) {

        this.sessionId = sessionId;
    }

    public static CreateSessionResult of(String sessionId) {

        return new CreateSessionResult(sessionId);
    }
}
