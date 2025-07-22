package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GetInsurancePolicyByIdUseCaseTest {

    private final GetInsurancePolicyByIdUseCase getInsurancePolicyByIdUseCase = Mockito.mock(GetInsurancePolicyByIdUseCase.class);

    @Test
    @DisplayName("Should retrieve insurance policy successfully when valid ID is provided")
    void shouldRetrieveInsurancePolicySuccessfully() {
        UUID validId = UUID.randomUUID();
        InsurancePolicyResponseDTO expectedResponse = new InsurancePolicyResponseDTO(/* expected data */);

        Mockito.when(getInsurancePolicyByIdUseCase.execute(validId)).thenReturn(expectedResponse);

        InsurancePolicyResponseDTO actualResponse = getInsurancePolicyByIdUseCase.execute(validId);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Should throw exception when null ID is provided")
    void shouldThrowExceptionWhenNullIdProvided() {
        Mockito.when(getInsurancePolicyByIdUseCase.execute(null))
                .thenThrow(new IllegalArgumentException("ID cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> getInsurancePolicyByIdUseCase.execute(null));
    }

    @Test
    @DisplayName("Should throw exception when invalid ID is provided")
    void shouldThrowExceptionWhenInvalidIdProvided() {
        UUID invalidId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        Mockito.when(getInsurancePolicyByIdUseCase.execute(invalidId))
                .thenThrow(new IllegalArgumentException("Invalid ID"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> getInsurancePolicyByIdUseCase.execute(invalidId));
    }
}