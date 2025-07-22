package com.felipe.policy.event.processor.application.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        BusinessException exception = new BusinessException("Error occurred") {};
        Assertions.assertEquals("Error occurred", exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        Throwable cause = new RuntimeException("Cause of error");
        BusinessException exception = new BusinessException("Error occurred", cause) {};
        Assertions.assertEquals("Error occurred", exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}
