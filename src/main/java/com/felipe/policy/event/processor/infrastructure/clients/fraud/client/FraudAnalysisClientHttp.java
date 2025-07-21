package com.felipe.policy.event.processor.infrastructure.clients.fraud.client;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.exception.FraudAnalysisException;
import com.felipe.policy.event.processor.application.exception.InvalidRiskKeyException;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FraudAnalysisClientHttp implements FraudAnalysisClient {

    private final RestTemplate restTemplate;
    private static final String FRAUD_API_URL = "http://wiremock:8080/fraud/analyze";

    @Override
    public FraudAnalysisResponseDTO analyze(InsurancePolicyRequest request) {
        validateRiskKey(request.getRiskKey());

        try {
            return restTemplate.postForObject(
                    FRAUD_API_URL,
                    request,
                    FraudAnalysisResponseDTO.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            throw new FraudAnalysisException("Fraud API returned 400 - Invalid riskKey", e);
        } catch (Exception e) {
            throw new FraudAnalysisException("Unexpected error while calling Fraud API", e);
        }
    }

    private void validateRiskKey(int riskKey) {
        if (riskKey < 1 || riskKey > 4) {
            throw new InvalidRiskKeyException("riskKey must be between 1 and 4. Provided: " + riskKey);
        }
    }
}