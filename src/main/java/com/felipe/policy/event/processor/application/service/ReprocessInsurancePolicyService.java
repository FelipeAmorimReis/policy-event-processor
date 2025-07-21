package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.ReprocessInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReprocessInsurancePolicyService implements ReprocessInsurancePolicyUseCase {

    private final InsurancePolicyRequestRepository repository;

    private final InsurancePolicyEventPublisher eventPublisher;

    @Override
    public ReprocessPolicyResponseDTO execute(UUID id) {
        return repository.findById(id)
                .map(policy -> {
                    InsuranceRequestStatus status = policy.getStatus();

                    if (canReprocess(status)) {
                        // Atualiza status e histórico
                        policy.setStatus(InsuranceRequestStatus.PENDING);
                        policy.getHistory().add(new StatusHistoryEntity(
                                InsuranceRequestStatus.PENDING,
                                Instant.now()
                        ));

                        repository.save(policy); // Persiste alterações antes de publicar

                        eventPublisher.publish(policy); // Publica o evento no Kafka

                        return buildReprocessResponse(
                                id,
                                "Apólice enviada para reprocessamento.",
                                "REPROCESSING_ACCEPTED"
                        );
                    }

                    return buildReprocessResponse(
                            id,
                            "Não é possível reprocessar apólice com status atual: " + status,
                            "REPROCESSING_REJECTED"
                    );
                })
                .orElseThrow(() -> new EntityNotFoundException("Apólice não encontrada com ID: " + id));
    }

    private boolean canReprocess(InsuranceRequestStatus status) {
        return status == InsuranceRequestStatus.PENDING || status == InsuranceRequestStatus.REJECTED;
    }

    private ReprocessPolicyResponseDTO buildReprocessResponse(UUID id, String message, String status) {
        return ReprocessPolicyResponseDTO.builder()
                .orderId(id)
                .message(message)
                .status(status)
                .timestamp(Instant.now())
                .build();
    }
}
