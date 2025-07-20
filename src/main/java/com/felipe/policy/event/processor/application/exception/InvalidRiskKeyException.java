package com.felipe.policy.event.processor.application.exception;

public class InvalidRiskKeyException extends RuntimeException {
    public InvalidRiskKeyException(String message) {
        super(message);
    }
}
