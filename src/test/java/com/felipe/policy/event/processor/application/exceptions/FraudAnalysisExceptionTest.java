package com.felipe.policy.event.processor.application.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FraudAnalysisExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        FraudAnalysisException exception = new FraudAnalysisException("Fraud detected");
        Assertions.assertEquals("Fraud detected", exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        Throwable cause = new RuntimeException("Underlying issue");
        FraudAnalysisException exception = new FraudAnalysisException("Fraud detected", cause);
        Assertions.assertEquals("Fraud detected", exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}

