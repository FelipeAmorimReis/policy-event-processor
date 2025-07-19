package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;

public interface CreateInsurancePolicyUseCase {
    InsurancePolicyResponseDTO execute(InsurancePolicyRequestDTO dto);
}
