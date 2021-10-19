package com.ktviv.pointpoker.domain.exception;

public class UnAssociatedParticipantException extends DomainException {

    private static final String TYPE = "participant-unassociated-with-session";

    public UnAssociatedParticipantException() {
        super(TYPE);
    }
}
