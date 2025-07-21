package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.GetInsurancePolicyByIdUseCase;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetInsurancePolicyByIdService implements GetInsurancePolicyByIdUseCase {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyPersistenceMapper persistenceMapper;
    private final InsurancePolicyMapper dtoMapper;

    @Override
    public InsurancePolicyResponseDTO execute(UUID id) {
        return repository.findById(id)
                .map(persistenceMapper::toDomain)
                .map(dtoMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found with id: " + id));
    }
}