package com.felipe.policy.event.processor.infrastructure.clients.fraud.client;

import com.felipe.policy.event.processor.application.dto.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FraudAnalysisClientHttp implements FraudAnalysisClient {

    private final RestTemplate restTemplate;
    private static final String FRAUD_API_URL = "http://wiremock:8080/fraud/analyze";

    @Override
    public FraudAnalysisResponseDTO analyze(InsurancePolicyRequest request) {
        try {
            return restTemplate.postForObject(
                    FRAUD_API_URL,
                    request,
                    FraudAnalysisResponseDTO.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Error with API fraude", e);
        }
    }
}