package com.felipe.policy.event.processor.domain.services.risk.HighRiskValidation;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;

public class HighRiskValidation implements RiskValidationStrategy {
    @Override
    public boolean isValid(InsuranceCategory category, double insuredAmount) {
        switch (category) {
            case AUTO:
                return insuredAmount <= 250000.00;
            case RESIDENTIAL:
                return insuredAmount <= 150000.00;
            default:
                return insuredAmount <= 125000.00;
        }
    }
}
