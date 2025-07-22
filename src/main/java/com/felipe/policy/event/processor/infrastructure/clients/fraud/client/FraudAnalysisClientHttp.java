package com.felipe.policy.event.processor.infrastructure.clients.fraud.client;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.exceptions.FraudAnalysisException;
import com.felipe.policy.event.processor.application.exceptions.InvalidRiskKeyException;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;

@Component
@RequiredArgsConstructor
public class FraudAnalysisClientHttp implements FraudAnalysisClient {

    private final RestTemplate restTemplate;

    @Override
    public FraudAnalysisResponseDTO analyze(InsurancePolicyRequest request) {
        validateRiskKey(request.getRiskKey());
        return callFraudApi(request);
    }

    private FraudAnalysisResponseDTO callFraudApi(InsurancePolicyRequest request) {
        try {
            return restTemplate.postForObject(FRAUD_API_URL, request, FraudAnalysisResponseDTO.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new FraudAnalysisException(FRAUD_API_400_MESSAGE, e);
        } catch (Exception e) {
            throw new FraudAnalysisException(FRAUD_API_UNEXPECTED_ERROR, e);
        }
    }

    private void validateRiskKey(int riskKey) {
        if (riskKey < 1 || riskKey > 4) {
            throw new InvalidRiskKeyException(RISK_KEY_INVALID_PREFIX + riskKey);
        }
    }
}