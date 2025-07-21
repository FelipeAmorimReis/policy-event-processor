package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;

import java.util.UUID;

public interface CancelInsurancePolicyUseCase {
    CancelPolicyResponseDTO execute(UUID id);
}