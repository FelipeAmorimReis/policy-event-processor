package com.felipe.policy.event.processor.application.exceptions;

public class FraudAnalysisException extends BusinessException {
    public FraudAnalysisException(String message) {
        super(message);
    }

    public FraudAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
