package com.felipe.policy.event.processor.infrastructure.clients.fraud.client;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.exceptions.FraudAnalysisException;
import com.felipe.policy.event.processor.application.exceptions.InvalidRiskKeyException;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class FraudAnalysisClientHttpTest {

    private final RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
    private final FraudAnalysisClientHttp client = new FraudAnalysisClientHttp(mockRestTemplate);

    @Test
    @DisplayName("Should analyze successfully for valid request")
    void shouldAnalyzeSuccessfullyForValidRequest() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(2).build();
        FraudAnalysisResponseDTO response = new FraudAnalysisResponseDTO();
        Mockito.when(mockRestTemplate.postForObject(Mockito.anyString(), Mockito.eq(request), Mockito.eq(FraudAnalysisResponseDTO.class)))
                .thenReturn(response);

        FraudAnalysisResponseDTO result = client.analyze(request);

        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw exception for invalid risk key")
    void shouldThrowExceptionForInvalidRiskKey() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(5).build();

        Assertions.assertThrows(InvalidRiskKeyException.class, () -> client.analyze(request));
    }

    @Test
    @DisplayName("Should throw FraudAnalysisException for API bad request")
    void shouldThrowFraudAnalysisExceptionForApiBadRequest() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(2).build();
        Mockito.when(mockRestTemplate.postForObject(Mockito.anyString(), Mockito.eq(request), Mockito.eq(FraudAnalysisResponseDTO.class)))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        Assertions.assertThrows(FraudAnalysisException.class, () -> client.analyze(request));
    }

    @Test
    @DisplayName("Should throw FraudAnalysisException for unexpected API error")
    void shouldThrowFraudAnalysisExceptionForUnexpectedApiError() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder().riskKey(2).build();
        Mockito.when(mockRestTemplate.postForObject(Mockito.anyString(), Mockito.eq(request), Mockito.eq(FraudAnalysisResponseDTO.class)))
                .thenThrow(RuntimeException.class);

        Assertions.assertThrows(FraudAnalysisException.class, () -> client.analyze(request));
    }
}