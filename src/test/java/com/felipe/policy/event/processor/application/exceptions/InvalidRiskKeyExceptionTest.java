package com.felipe.policy.event.processor.application.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidRiskKeyExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        InvalidRiskKeyException exception = new InvalidRiskKeyException("Risk key is invalid");
        Assertions.assertEquals("Risk key is invalid", exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        Throwable cause = new RuntimeException("Cause of invalid risk key");
        InvalidRiskKeyException exception = new InvalidRiskKeyException("Risk key is invalid");
        exception.initCause(cause);
        Assertions.assertEquals("Risk key is invalid", exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}