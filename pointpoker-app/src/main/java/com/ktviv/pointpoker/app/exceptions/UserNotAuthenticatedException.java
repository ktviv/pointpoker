package com.ktviv.pointpoker.app.exceptions;

import com.ktviv.pointpoker.domain.exception.DomainException;

public class UserNotAuthenticatedException extends DomainException {

    private static final String TYPE = "user-not-authenticated";

    public UserNotAuthenticatedException() {
        super(TYPE);
    }
}
