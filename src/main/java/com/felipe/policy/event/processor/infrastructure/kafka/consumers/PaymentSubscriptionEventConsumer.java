package com.felipe.policy.event.processor.infrastructure.kafka.consumers;

import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentSubscriptionEventConsumer {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyEventPublisher eventPublisher;

    @KafkaListener(topics = "payment-subscription-result", groupId = "insurance-service")
    public void consume(@Payload PaymentSubscriptionResultEventDTO event) {
        log.info(LOG_EVENT_RECEIVED, event);
        repository.findById(event.getOrderId())
                .ifPresentOrElse(
                        policy -> handlePolicyEvent(event, policy),
                        () -> log.warn(LOG_POLICY_NOT_FOUND_KAFKA, event.getOrderId())
                );
    }

    private void handlePolicyEvent(PaymentSubscriptionResultEventDTO event, InsurancePolicyRequestEntity policy) {
        if (isPolicyStateInvalid(event, policy)) {
            log.warn(LOG_INVALID_POLICY_STATE, policy.getId());
            return;
        }

        InsuranceRequestStatus newStatus = determineNewStatus(event);
        updatePolicyAndPublishEvent(policy, newStatus);
    }

    private boolean isPolicyStateInvalid(PaymentSubscriptionResultEventDTO event, InsurancePolicyRequestEntity policy) {
        return isCancelled(policy) || isNotPending(policy) || isEventOutdated(event, policy);
    }

    private boolean isCancelled(InsurancePolicyRequestEntity policy) {
        if (policy.getStatus() == InsuranceRequestStatus.CANCELLED) {
            log.warn(LOG_POLICY_CANCELLED, policy.getId());
            return true;
        }
        return false;
    }

    private boolean isNotPending(InsurancePolicyRequestEntity policy) {
        if (policy.getStatus() != InsuranceRequestStatus.PENDING) {
            log.warn(LOG_POLICY_NOT_PENDING, policy.getId());
            return true;
        }
        return false;
    }

    private boolean isEventOutdated(PaymentSubscriptionResultEventDTO event, InsurancePolicyRequestEntity policy) {
        if (event.getAnalyzedAt().isBefore(policy.getCreatedAt())) {
            log.error(LOG_EVENT_OUTDATED, policy.getId());
            return true;
        }
        return false;
    }

    private InsuranceRequestStatus determineNewStatus(PaymentSubscriptionResultEventDTO event) {
        boolean isPaymentSuccessful = PAID.equalsIgnoreCase(event.getPaymentStatus());
        boolean isSubscriptionAuthorized = AUTHORIZED.equalsIgnoreCase(event.getSubscriptionStatus());
        return (isPaymentSuccessful && isSubscriptionAuthorized)
                ? InsuranceRequestStatus.APPROVED
                : InsuranceRequestStatus.REJECTED;
    }

    private void updatePolicyAndPublishEvent(InsurancePolicyRequestEntity policy, InsuranceRequestStatus newStatus) {
        updatePolicyStatus(policy, newStatus);
        eventPublisher.publish(policy);
        log.info(LOG_POLICY_STATUS_UPDATED, newStatus, policy.getId());
    }

    private void updatePolicyStatus(InsurancePolicyRequestEntity policy, InsuranceRequestStatus newStatus) {
        policy.setStatus(newStatus);

        if (newStatus == InsuranceRequestStatus.APPROVED || newStatus == InsuranceRequestStatus.REJECTED) {
            policy.setFinishedAt(Instant.now());
        }

        policy.getHistory().add(new StatusHistoryEntity(newStatus, Instant.now()));
        repository.save(policy);
    }
}