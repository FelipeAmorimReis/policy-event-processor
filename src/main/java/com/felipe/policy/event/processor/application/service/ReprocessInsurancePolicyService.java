package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import com.felipe.policy.event.processor.application.usecases.ReprocessInsurancePolicyUseCase;
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
import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReprocessInsurancePolicyService implements ReprocessInsurancePolicyUseCase {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyEventPublisher eventPublisher;
    private final PolicyResponseFactory responseFactory;

    @Override
    public ReprocessPolicyResponseDTO execute(UUID id) {
        log.info(LOG_REPROCESS_STARTED, id);
        InsurancePolicyRequestEntity policy = repository.findById(id)
                .orElseThrow(() -> {
                    log.error(LOG_POLICY_NOT_FOUND, id);
                    return new EntityNotFoundException(POLICY_NOT_FOUND_WITH_ID + id);
                });

        return processPolicy(policy);
    }

    private ReprocessPolicyResponseDTO processPolicy(InsurancePolicyRequestEntity policy) {
        if (canReprocess(policy.getStatus())) {
            updatePolicyStatus(policy);
            log.info(LOG_KAFKA_EVENT_PUBLISHED, policy.getId() ,policy.getStatus());
            eventPublisher.publish(policy);
            log.info(LOG_REPROCESS_SENT, policy.getId());
            return responseFactory.buildReprocessAccepted();
        }

        log.warn(LOG_REPROCESS_REJECTED, policy.getId(), policy.getStatus());
        return responseFactory.buildReprocessRejected(policy.getStatus().name());
    }

    private void updatePolicyStatus(InsurancePolicyRequestEntity policy) {
        policy.setStatus(InsuranceRequestStatus.PENDING);
        policy.getHistory().add(new StatusHistoryEntity(InsuranceRequestStatus.PENDING, Instant.now()));
        repository.save(policy);
        log.info(LOG_POLICY_STATUS_UPDATED, policy.getId());
    }

    private boolean canReprocess(InsuranceRequestStatus status) {
        return status == InsuranceRequestStatus.PENDING;
    }
}