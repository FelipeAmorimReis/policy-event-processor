package com.felipe.policy.event.processor.infrastructure.clients.fraud;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;

public interface FraudAnalysisClient {
    FraudAnalysisResponseDTO analyze(InsurancePolicyRequest request);
}