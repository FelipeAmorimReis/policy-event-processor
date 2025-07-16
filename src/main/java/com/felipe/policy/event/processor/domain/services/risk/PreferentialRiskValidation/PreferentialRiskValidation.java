package com.felipe.policy.event.processor.domain.services.risk.PreferentialRiskValidation;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;

public class PreferentialRiskValidation implements RiskValidationStrategy {
    @Override
    public boolean isValid(InsuranceCategory category, double insuredAmount) {
        switch (category) {
            case LIFE:
                return insuredAmount < 800000.00;
            case AUTO:
            case RESIDENTIAL:
                return insuredAmount < 450000.00;
            default:
                return insuredAmount <= 375000.00;
        }
    }
}
