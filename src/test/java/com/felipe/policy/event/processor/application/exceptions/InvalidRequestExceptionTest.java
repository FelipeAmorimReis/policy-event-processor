package com.felipe.policy.event.processor.application.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidRequestExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        InvalidRequestException exception = new InvalidRequestException("Request is invalid");
        Assertions.assertEquals("Request is invalid", exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        Throwable cause = new RuntimeException("Cause of invalid request");
        InvalidRequestException exception = new InvalidRequestException("Request is invalid", cause);
        Assertions.assertEquals("Request is invalid", exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}