package com.felipe.policy.event.processor.domain.services.risk.NoInformationRiskValidation;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class NoInformationRiskValidation implements RiskValidationStrategy {
    @Override
    public boolean isValid(InsuranceCategory category, double insuredAmount) {
        switch (category) {
            case LIFE:
            case RESIDENTIAL:
                return insuredAmount <= 200000.00;
            case AUTO:
                return insuredAmount <= 75000.00;
            default:
                return insuredAmount <= 55000.00;
        }
    }
}
