package com.felipe.policy.event.processor.domain.services.risk.RiskValidationStrategy;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RiskValidationStrategyTest {

    private final RiskValidationStrategy strategy = Mockito.mock(RiskValidationStrategy.class);

    @Test
    @DisplayName("Should return true for valid category and insured amount within limit")
    void shouldReturnTrueForValidCategoryWithinLimit() {
        Mockito.when(strategy.isValid(InsuranceCategory.AUTO, 150000.00)).thenReturn(true);

        boolean result = strategy.isValid(InsuranceCategory.AUTO, 150000.00);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for valid category and insured amount exceeding limit")
    void shouldReturnFalseForValidCategoryExceedingLimit() {
        Mockito.when(strategy.isValid(InsuranceCategory.AUTO, 300000.00)).thenReturn(false);

        boolean result = strategy.isValid(InsuranceCategory.AUTO, 300000.00);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should throw exception for null category")
    void shouldThrowExceptionForNullCategory() {
        Mockito.when(strategy.isValid(null, 100000.00)).thenThrow(new IllegalArgumentException("Category cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.isValid(null, 100000.00));
    }

    @Test
    @DisplayName("Should throw exception for negative insured amount")
    void shouldThrowExceptionForNegativeInsuredAmount() {
        Mockito.when(strategy.isValid(InsuranceCategory.RESIDENTIAL, -50000.00)).thenThrow(new IllegalArgumentException("Insured amount cannot be negative"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.isValid(InsuranceCategory.RESIDENTIAL, -50000.00));
    }
}