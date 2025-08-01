package com.felipe.policy.event.processor.domain.services.risk;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;
import com.felipe.policy.event.processor.application.exceptions.InvalidRequestException;

import java.util.Map;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;

public class RiskValidationContext {

    private final Map<RiskClassification, RiskValidationStrategy> strategyMap;

    public RiskValidationContext(Map<RiskClassification, RiskValidationStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public boolean validate(RiskClassification classification, InsuranceCategory category, double insuredAmount) {
        validateInput(classification, category, insuredAmount);

        RiskValidationStrategy strategy = strategyMap.get(classification);

        if (strategy == null) {
            throw new InvalidRequestException(INVALID_RISK_CLASSIFICATION_PREFIX + classification);
        }

        return strategy.isValid(category, insuredAmount);
    }

    private void validateInput(RiskClassification classification, InsuranceCategory category, double insuredAmount) {
        validateNotNull(classification, RISK_CLASSIFICATION_REQUIRED);
        validateNotNull(category, INSURANCE_CATEGORY_REQUIRED);
        validateNonNegative(insuredAmount, INSURED_AMOUNT_NON_NEGATIVE);
    }

    private void validateNotNull(Object value, String errorMessage) {
        if (value == null) {
            throw new InvalidRequestException(errorMessage);
        }
    }

    private void validateNonNegative(double value, String errorMessage) {
        if (value < 0) {
            throw new InvalidRequestException(errorMessage);
        }
    }
}
