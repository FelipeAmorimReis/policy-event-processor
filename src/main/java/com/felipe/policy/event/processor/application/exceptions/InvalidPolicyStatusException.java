package com.felipe.policy.event.processor.application.exceptions;
public class InvalidPolicyStatusException extends BusinessException {
    public InvalidPolicyStatusException(String message) {
        super(message);
    }
}