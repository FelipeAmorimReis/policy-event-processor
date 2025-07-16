package com.felipe.policy.event.processor.domain.services.risk.RegularRiskValidation;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;

public class RegularRiskValidation implements RiskValidationStrategy {
    @Override
    public boolean isValid(InsuranceCategory category, double insuredAmount) {
        switch (category) {
            case LIFE:
            case RESIDENTIAL:
                return insuredAmount <= 500000.00;
            case AUTO:
                return insuredAmount <= 350000.00;
            default:
                return insuredAmount <= 255000.00;
        }
    }
}

