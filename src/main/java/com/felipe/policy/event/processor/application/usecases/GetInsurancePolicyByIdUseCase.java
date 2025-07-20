package com.felipe.policy.event.processor.application.usecases;


import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;

import java.util.UUID;

public interface GetInsurancePolicyByIdUseCase {
    InsurancePolicyResponseDTO execute(UUID id);
}