package com.felipe.policy.event.processor.infrastructure.config;

import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.HighRiskValidation.HighRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.NoInformationRiskValidation.NoInformationRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.PreferentialRiskValidation.PreferentialRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RegularRiskValidation.RegularRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RiskValidationConfig {

    @Bean
    public RiskValidationContext riskValidationContext(Map<RiskClassification, RiskValidationStrategy> strategyMap) {
        return new RiskValidationContext(strategyMap);
    }

    @Bean
    public Map<RiskClassification, RiskValidationStrategy> strategyMap(
            HighRiskValidation highRiskValidation,
            RegularRiskValidation regularRiskValidation,
            PreferentialRiskValidation preferentialRiskValidation,
            NoInformationRiskValidation noInformationRiskValidation) {
        Map<RiskClassification, RiskValidationStrategy> strategyMap = new HashMap<>();

        strategyMap.put(RiskClassification.HIGH_RISK, highRiskValidation);
        strategyMap.put(RiskClassification.REGULAR, regularRiskValidation);
        strategyMap.put(RiskClassification.PREFERENTIAL, preferentialRiskValidation);
        strategyMap.put(RiskClassification.NO_INFORMATION, noInformationRiskValidation);

        return strategyMap;
    }
}