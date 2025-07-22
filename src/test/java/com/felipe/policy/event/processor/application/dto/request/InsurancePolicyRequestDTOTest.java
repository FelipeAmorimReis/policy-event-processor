package com.felipe.policy.event.processor.application.dto.request;
import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class InsurancePolicyRequestDTOTest {

    private final Validator validator;

    public InsurancePolicyRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void validInsurancePolicyRequestDTO() {
        InsurancePolicyRequestDTO dto = InsurancePolicyRequestDTO.builder()
                .customerId(UUID.randomUUID())
                .productId(123L)
                .category(InsuranceCategory.LIFE)
                .salesChannel("Online")
                .paymentMethod("Credit Card")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(100.00))
                .insuredAmount(BigDecimal.valueOf(1000.00))
                .coverages(Map.of("Coverage1", BigDecimal.valueOf(500.00)))
                .assistances(List.of("Roadside Assistance"))
                .riskKey(2)
                .build();

        Assertions.assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void missingCustomerIdShouldFailValidation() {
        InsurancePolicyRequestDTO dto = InsurancePolicyRequestDTO.builder()
                .productId(123L)
                .category(InsuranceCategory.LIFE)
                .salesChannel("Online")
                .paymentMethod("Credit Card")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(100.00))
                .insuredAmount(BigDecimal.valueOf(1000.00))
                .coverages(Map.of("Coverage1", BigDecimal.valueOf(500.00)))
                .assistances(List.of("Roadside Assistance"))
                .riskKey(2)
                .build();

        Assertions.assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void riskKeyBelowMinimumShouldFailValidation() {
        InsurancePolicyRequestDTO dto = InsurancePolicyRequestDTO.builder()
                .customerId(UUID.randomUUID())
                .productId(123L)
                .category(InsuranceCategory.LIFE)
                .salesChannel("Online")
                .paymentMethod("Credit Card")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(100.00))
                .insuredAmount(BigDecimal.valueOf(1000.00))
                .coverages(Map.of("Coverage1", BigDecimal.valueOf(500.00)))
                .assistances(List.of("Roadside Assistance"))
                .riskKey(0)
                .build();

        Assertions.assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void riskKeyAboveMaximumShouldFailValidation() {
        InsurancePolicyRequestDTO dto = InsurancePolicyRequestDTO.builder()
                .customerId(UUID.randomUUID())
                .productId(123L)
                .category(InsuranceCategory.LIFE)
                .salesChannel("Online")
                .paymentMethod("Credit Card")
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(100.00))
                .insuredAmount(BigDecimal.valueOf(1000.00))
                .coverages(Map.of("Coverage1", BigDecimal.valueOf(500.00)))
                .assistances(List.of("Roadside Assistance"))
                .riskKey(5)
                .build();

        Assertions.assertFalse(validator.validate(dto).isEmpty());
    }
}