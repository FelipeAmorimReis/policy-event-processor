package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.GetInsurancePolicyByIdUseCase;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.POLICY_NOT_FOUND_WITH_ID;

@Service
@RequiredArgsConstructor
public class GetInsurancePolicyByIdService implements GetInsurancePolicyByIdUseCase {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyPersistenceMapper persistenceMapper;
    private final InsurancePolicyMapper dtoMapper;

    @Override
    public InsurancePolicyResponseDTO execute(UUID id) {
        return mapToResponse(findPolicyById(id));
    }

    private InsurancePolicyRequestEntity findPolicyById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POLICY_NOT_FOUND_WITH_ID + id));
    }

    private InsurancePolicyResponseDTO mapToResponse(InsurancePolicyRequestEntity entity) {
        return dtoMapper.toResponseDTO(persistenceMapper.toDomain(entity));
    }
}