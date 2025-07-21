package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;

public interface CreateInsurancePolicyUseCase {
    InsurancePolicyResponseDTO execute(InsurancePolicyRequestDTO dto);
}
