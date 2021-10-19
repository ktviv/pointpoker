package com.ktviv.pointpoker.domain.exception;

public class UserNotFoundException extends DomainException {

    private static final String TYPE = "user-not-found";

    public UserNotFoundException() {
        super(TYPE);
    }

    public UserNotFoundException(String errorMessage) {
        super(TYPE, errorMessage);
    }

    public UserNotFoundException(String errorMessage, Exception exception) {
        super(TYPE, errorMessage, exception);
    }
}
