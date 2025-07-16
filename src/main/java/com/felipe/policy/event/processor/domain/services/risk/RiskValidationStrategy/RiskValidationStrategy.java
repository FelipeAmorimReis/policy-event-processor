package com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;

public interface RiskValidationStrategy {
    boolean isValid(InsuranceCategory category, double insuredAmount);
}
