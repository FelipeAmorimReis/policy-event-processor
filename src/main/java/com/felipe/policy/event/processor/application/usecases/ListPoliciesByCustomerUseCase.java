package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ListPoliciesByCustomerUseCase {
    List<InsurancePolicyResponseDTO> execute(UUID customerId);
}