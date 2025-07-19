package com.felipe.policy.event.processor.infrastructure.clients.fraud;

import com.felipe.policy.event.processor.application.dto.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;

@Component
public class FraudAnalysisClientFake implements FraudAnalysisClient {
    @Override
    public FraudAnalysisResponseDTO analyze(InsurancePolicyRequest request) {
        return FraudAnalysisResponseDTO.builder()
                .orderId(request.getId())
                .customerId(request.getCustomerId())
                .analyzedAt(Instant.now())
                .classification(RiskClassification.HIGH_RISK)
                .occurrences(Collections.emptyList())
                .build();
    }
}