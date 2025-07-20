package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.ListPoliciesByCustomerUseCase;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListPoliciesByCustomerService implements ListPoliciesByCustomerUseCase {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyPersistenceMapper persistenceMapper;
    private final InsurancePolicyMapper dtoMapper;

    @Override
    public List<InsurancePolicyResponseDTO> execute(UUID customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(persistenceMapper::toDomain)
                .map(dtoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}