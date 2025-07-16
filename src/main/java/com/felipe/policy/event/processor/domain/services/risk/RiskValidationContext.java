package com.felipe.policy.event.processor.domain.services.risk;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;

import java.util.Map;

public class RiskValidationContext {

    private final Map<RiskClassification, RiskValidationStrategy> strategyMap;

    public RiskValidationContext(Map<RiskClassification, RiskValidationStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public boolean validate(RiskClassification classification, InsuranceCategory category, double insuredAmount) {
        validateInput(classification, category, insuredAmount);

        RiskValidationStrategy strategy = strategyMap.get(classification);

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid risk classification: " + classification);
        }

        return strategy.isValid(category, insuredAmount);
    }

    private void validateInput(RiskClassification classification, InsuranceCategory category, double insuredAmount) {
        validateNotNull(classification, "Risk classification must not be null.");
        validateNotNull(category, "Insurance category must not be null.");
        validateNonNegative(insuredAmount, "Insured amount must be non-negative.");
    }

    private void validateNotNull(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateNonNegative(double value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
