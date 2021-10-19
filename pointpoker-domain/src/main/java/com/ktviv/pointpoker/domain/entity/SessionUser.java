package com.ktviv.pointpoker.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class SessionUser {

    private String id;
    private String displayName;
    private String email;

    public static SessionUser of(String id, String displayName, String email) {

        return new SessionUser(id, displayName, email);
    }
}
