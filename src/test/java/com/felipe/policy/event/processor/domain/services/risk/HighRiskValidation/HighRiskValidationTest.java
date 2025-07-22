package com.felipe.policy.event.processor.domain.services.risk.HighRiskValidation;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HighRiskValidationTest {

    private final HighRiskValidation validation = new HighRiskValidation();

    @Test
    @DisplayName("Should return true for AUTO category with insured amount within limit")
    void shouldReturnTrueForAutoCategoryWithinLimit() {
        boolean result = validation.isValid(InsuranceCategory.AUTO, 200000.00);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for AUTO category with insured amount exceeding limit")
    void shouldReturnFalseForAutoCategoryExceedingLimit() {
        boolean result = validation.isValid(InsuranceCategory.AUTO, 300000.00);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should return true for RESIDENTIAL category with insured amount within limit")
    void shouldReturnTrueForResidentialCategoryWithinLimit() {
        boolean result = validation.isValid(InsuranceCategory.RESIDENTIAL, 100000.00);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for RESIDENTIAL category with insured amount exceeding limit")
    void shouldReturnFalseForResidentialCategoryExceedingLimit() {
        boolean result = validation.isValid(InsuranceCategory.RESIDENTIAL, 200000.00);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should return true for OTHER category with insured amount within limit")
    void shouldReturnTrueForOtherCategoryWithinLimit() {
        boolean result = validation.isValid(InsuranceCategory.OTHER, 100000.00);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for OTHER category with insured amount exceeding limit")
    void shouldReturnFalseForOtherCategoryExceedingLimit() {
        boolean result = validation.isValid(InsuranceCategory.OTHER, 150000.00);
        Assertions.assertFalse(result);
    }
}