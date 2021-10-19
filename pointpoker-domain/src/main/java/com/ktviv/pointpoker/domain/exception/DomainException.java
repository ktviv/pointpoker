package com.ktviv.pointpoker.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends Exception {

    private final String errorType;

    public DomainException(String errorType) {
        this.errorType = errorType;
    }

    public DomainException(String errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public DomainException(String errorType, String message, Exception exception) {
        super(message, exception);
        this.errorType = errorType;
    }
}
