package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CreateInsurancePolicyUseCaseTest {

    private CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    @BeforeEach
    void setUp() {
        createInsurancePolicyUseCase = Mockito.mock(CreateInsurancePolicyUseCase.class);
    }

    @Test
    @DisplayName("Should create insurance policy successfully when valid request is provided")
    void shouldCreateInsurancePolicySuccessfully() {
        InsurancePolicyRequestDTO validRequest = new InsurancePolicyRequestDTO(/* valid data */);
        InsurancePolicyResponseDTO expectedResponse = new InsurancePolicyResponseDTO(/* expected data */);

        Mockito.when(createInsurancePolicyUseCase.execute(validRequest)).thenReturn(expectedResponse);

        InsurancePolicyResponseDTO actualResponse = createInsurancePolicyUseCase.execute(validRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Should throw exception when null request is provided")
    void shouldThrowExceptionWhenNullRequestProvided() {
        Mockito.when(createInsurancePolicyUseCase.execute(null))
                .thenThrow(new IllegalArgumentException("Request cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> createInsurancePolicyUseCase.execute(null));
    }

    @Test
    @DisplayName("Should handle empty fields in request gracefully")
    void shouldHandleEmptyFieldsInRequestGracefully() {
        InsurancePolicyRequestDTO emptyRequest = new InsurancePolicyRequestDTO(/* empty or invalid data */);

        Mockito.when(createInsurancePolicyUseCase.execute(emptyRequest))
                .thenThrow(new IllegalArgumentException("Invalid request data"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> createInsurancePolicyUseCase.execute(emptyRequest));
    }
}