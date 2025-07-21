package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;

import java.util.UUID;

public interface ReprocessInsurancePolicyUseCase {
    ReprocessPolicyResponseDTO execute(UUID id);
}
