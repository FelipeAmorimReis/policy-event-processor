package com.felipe.policy.event.processor.application.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidPolicyStatusExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        InvalidPolicyStatusException exception = new InvalidPolicyStatusException("Invalid status");
        Assertions.assertEquals("Invalid status", exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        Throwable cause = new RuntimeException("Cause of invalid status");
        InvalidPolicyStatusException exception = new InvalidPolicyStatusException("Invalid status");
        exception.initCause(cause);
        Assertions.assertEquals("Invalid status", exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}


