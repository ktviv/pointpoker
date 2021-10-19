package com.ktviv.pointpoker.domain.exception;

public class PokerSessionNotFoundException extends DomainException {

    private static final String TYPE = "poker-session-not-found";

    public PokerSessionNotFoundException() {
        super(TYPE);
    }
}
