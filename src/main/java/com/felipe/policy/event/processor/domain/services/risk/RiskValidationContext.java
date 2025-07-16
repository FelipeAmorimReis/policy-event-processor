package com.felipe.policy.event.processor.domain.services.risk;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;

import com.felipe.policy.event.processor.domain.services.risk.HighRiskValidation.HighRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.NoInformationRiskValidation.NoInformationRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.PreferentialRiskValidation.PreferentialRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RegularRiskValidation.RegularRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;

import java.util.HashMap;
import java.util.Map;
public class RiskValidationContext {

    private final Map<RiskClassification, RiskValidationStrategy> strategyMap;

    public RiskValidationContext() {
        this.strategyMap = new HashMap<>();
        strategyMap.put(RiskClassification.HIGH_RISK, new HighRiskValidation());
        strategyMap.put(RiskClassification.REGULAR, new RegularRiskValidation());
        strategyMap.put(RiskClassification.PREFERENTIAL, new PreferentialRiskValidation());
        strategyMap.put(RiskClassification.NO_INFORMATION, new NoInformationRiskValidation());
    }

    public boolean validate(RiskClassification classification, InsuranceCategory category, double insuredAmount) {
        RiskValidationStrategy strategy = strategyMap.get(classification);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid risk classification: " + classification);
        }
        return strategy.isValid(category, insuredAmount);
    }

}
