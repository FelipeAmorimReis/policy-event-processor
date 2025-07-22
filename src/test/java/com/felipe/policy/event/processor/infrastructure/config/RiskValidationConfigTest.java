package com.felipe.policy.event.processor.infrastructure.config;

import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.HighRiskValidation.HighRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.NoInformationRiskValidation.NoInformationRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.PreferentialRiskValidation.PreferentialRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RegularRiskValidation.RegularRiskValidation;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy.RiskValidationStrategy;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RiskValidationConfigTest {

    private final ApplicationContext mockContext = Mockito.mock(ApplicationContext.class);
    private final HighRiskValidation mockHighRiskValidation = Mockito.mock(HighRiskValidation.class);
    private final RegularRiskValidation mockRegularRiskValidation = Mockito.mock(RegularRiskValidation.class);
    private final PreferentialRiskValidation mockPreferentialRiskValidation = Mockito.mock(PreferentialRiskValidation.class);
    private final NoInformationRiskValidation mockNoInformationRiskValidation = Mockito.mock(NoInformationRiskValidation.class);
    private final RiskValidationConfig config = new RiskValidationConfig();

    @Test
    @DisplayName("Should create strategy map with all risk classifications")
    void shouldCreateStrategyMapWithAllRiskClassifications() {
        Map<RiskClassification, RiskValidationStrategy> strategyMap = config.strategyMap(
                mockHighRiskValidation,
                mockRegularRiskValidation,
                mockPreferentialRiskValidation,
                mockNoInformationRiskValidation
        );

        Assertions.assertEquals(4, strategyMap.size());
        Assertions.assertTrue(strategyMap.containsKey(RiskClassification.HIGH_RISK));
        Assertions.assertTrue(strategyMap.containsKey(RiskClassification.REGULAR));
        Assertions.assertTrue(strategyMap.containsKey(RiskClassification.PREFERENTIAL));
        Assertions.assertTrue(strategyMap.containsKey(RiskClassification.NO_INFORMATION));
    }

    @Test
    @DisplayName("Should create RiskValidationContext with valid strategy map")
    void shouldCreateRiskValidationContextWithValidStrategyMap() {
        Map<RiskClassification, RiskValidationStrategy> strategyMap = config.strategyMap(
                mockHighRiskValidation,
                mockRegularRiskValidation,
                mockPreferentialRiskValidation,
                mockNoInformationRiskValidation
        );

        RiskValidationContext context = config.riskValidationContext(strategyMap);

        Assertions.assertNotNull(context);
    }
}