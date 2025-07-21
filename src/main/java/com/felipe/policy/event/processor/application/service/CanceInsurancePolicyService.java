package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.StatusHistoryPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.StatusHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CanceInsurancePolicyService implements CancelInsurancePolicyUseCase {

    private final InsurancePolicyRequestRepository policyRepo;
    private final StatusHistoryRepository statusRepo;
    private final InsurancePolicyPersistenceMapper policyMapper;
    private final StatusHistoryPersistenceMapper statusMapper;

    @Override
    public CancelPolicyResponseDTO execute(UUID id) {
        InsurancePolicyRequestEntity entity = policyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found with id: " + id));

        if (entity.getStatus() == InsuranceRequestStatus.APPROVED || entity.getStatus() == InsuranceRequestStatus.REJECTED) {
            throw new IllegalStateException("Cannot cancel an already approved or rejected policy.");
        }

        // Atualiza o status da apólice
        entity.setStatus(InsuranceRequestStatus.CANCELLED);

        // Adiciona o histórico na própria entidade (com cascade)
        StatusHistoryEntity historyEntity = new StatusHistoryEntity(
                InsuranceRequestStatus.CANCELLED,
                Instant.now()
        );

        entity.getHistory().add(historyEntity);

        // Salva a apólice (incluindo o histórico, por cascade)
        policyRepo.save(entity);

        // Retorno
        return CancelPolicyResponseDTO.builder()
                .message("Apólice cancelada com sucesso.")
                .status("CANCELLED")
                .timestamp(Instant.now())
                .build();
    }
}