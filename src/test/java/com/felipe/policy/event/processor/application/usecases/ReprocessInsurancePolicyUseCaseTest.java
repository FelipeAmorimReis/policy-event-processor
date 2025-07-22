package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReprocessInsurancePolicyUseCaseTest {

    private final ReprocessInsurancePolicyUseCase reprocessInsurancePolicyUseCase = Mockito.mock(ReprocessInsurancePolicyUseCase.class);

    @Test
    @DisplayName("Should reprocess insurance policy successfully when valid ID is provided")
    void shouldReprocessInsurancePolicySuccessfully() {
        UUID validId = UUID.randomUUID();
        ReprocessPolicyResponseDTO expectedResponse = new ReprocessPolicyResponseDTO(/* expected data */);

        Mockito.when(reprocessInsurancePolicyUseCase.execute(validId)).thenReturn(expectedResponse);

        ReprocessPolicyResponseDTO actualResponse = reprocessInsurancePolicyUseCase.execute(validId);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Should throw exception when null ID is provided")
    void shouldThrowExceptionWhenNullIdProvided() {
        Mockito.when(reprocessInsurancePolicyUseCase.execute(null))
                .thenThrow(new IllegalArgumentException("ID cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> reprocessInsurancePolicyUseCase.execute(null));
    }

    @Test
    @DisplayName("Should throw exception when invalid ID is provided")
    void shouldThrowExceptionWhenInvalidIdProvided() {
        UUID invalidId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        Mockito.when(reprocessInsurancePolicyUseCase.execute(invalidId))
                .thenThrow(new IllegalArgumentException("Invalid ID"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> reprocessInsurancePolicyUseCase.execute(invalidId));
    }
}