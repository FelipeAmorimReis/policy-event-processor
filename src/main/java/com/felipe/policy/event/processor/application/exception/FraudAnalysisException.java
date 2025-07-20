package com.felipe.policy.event.processor.application.exception;

public class FraudAnalysisException extends RuntimeException {
    public FraudAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
