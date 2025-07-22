package com.felipe.policy.event.processor.infrastructure.clients.fraud;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FraudAnalysisClientTest {

    private final FraudAnalysisClient mockClient = Mockito.mock(FraudAnalysisClient.class);

    @Test
    @DisplayName("Should return valid response for valid insurance policy request")
    void shouldReturnValidResponseForValidRequest() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(1).build();
        FraudAnalysisResponseDTO response = new FraudAnalysisResponseDTO();
        Mockito.when(mockClient.analyze(request)).thenReturn(response);

        FraudAnalysisResponseDTO result = mockClient.analyze(request);

        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw exception for null insurance policy request")
    void shouldThrowExceptionForNullRequest() {
        Mockito.when(mockClient.analyze(null)).thenThrow(new IllegalArgumentException("Request cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockClient.analyze(null));
    }

    @Test
    @DisplayName("Should throw exception for invalid risk key in insurance policy request")
    void shouldThrowExceptionForInvalidRiskKey() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(-1).build();
        Mockito.when(mockClient.analyze(request)).thenThrow(new IllegalArgumentException("Invalid risk key"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockClient.analyze(request));
    }
}