package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.exceptions.InvalidPolicyStatusException;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;
import static com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelInsurancePolicyService implements CancelInsurancePolicyUseCase {

    private final InsurancePolicyRequestRepository policyRepo;
    private final PolicyResponseFactory responseFactory;
    private final InsurancePolicyEventPublisher eventPublisher;

    @Override
    public CancelPolicyResponseDTO execute(UUID id) {
        InsurancePolicyRequestEntity entity = findPolicyById(id);

        validatePolicyStatus(entity);
        updatePolicyStatus(entity, CANCELLED);

        return responseFactory.buildCancelResponse();
    }

    private InsurancePolicyRequestEntity findPolicyById(UUID id) {
        return policyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POLICY_NOT_FOUND_WITH_ID + id));
    }

    private void validatePolicyStatus(InsurancePolicyRequestEntity entity) {
        if (Set.of(CANCELLED, APPROVED, REJECTED).contains(entity.getStatus())) {
            throw new InvalidPolicyStatusException(CANCEL_INVALID_STATUS);
        }
    }

    private void updatePolicyStatus(InsurancePolicyRequestEntity entity, InsuranceRequestStatus newStatus) {
        entity.setStatus(newStatus);

        if (newStatus == CANCELLED) {
            entity.setFinishedAt(Instant.now());
        }

        StatusHistoryEntity historyEntity = new StatusHistoryEntity(newStatus, Instant.now());
        entity.getHistory().add(historyEntity); // <<< ESTA LINHA FALTAVA

        InsurancePolicyRequestEntity saved = policyRepo.save(entity);
        log.info(LOG_KAFKA_EVENT_PUBLISHED, saved.getId(), saved.getStatus());
        eventPublisher.publish(saved);
    }
}