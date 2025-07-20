package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.StatusHistoryPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.StatusHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CanceInsurancePolicyService implements CancelInsurancePolicyUseCase {

    private final InsurancePolicyRequestRepository policyRepo;
    private final StatusHistoryRepository statusRepo;
    private final InsurancePolicyPersistenceMapper policyMapper;
    private final StatusHistoryPersistenceMapper statusMapper;

    @Override
    public void execute(UUID id) {
        var entity = policyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found with id: " + id));

        var domain = policyMapper.toDomain(entity);

        if (domain.getStatus() == InsuranceRequestStatus.ISSUED) {
            throw new IllegalStateException("Cannot cancel an already issued policy.");
        }

        if (domain.getStatus() == InsuranceRequestStatus.APPROVED || domain.getStatus() == InsuranceRequestStatus.REJECTED) {
            throw new IllegalStateException("Cannot cancel an already approved or rejected policy.");
        }

        // Atualiza o status da entidade
        entity.setStatus(InsuranceRequestStatus.CANCELLED);
        policyRepo.save(entity);

        // Cria histórico
        var historyEntity = statusMapper.toEntity(
                new StatusHistory(InsuranceRequestStatus.CANCELLED, OffsetDateTime.now().toInstant())
        );

        statusRepo.save(historyEntity);
    }
}