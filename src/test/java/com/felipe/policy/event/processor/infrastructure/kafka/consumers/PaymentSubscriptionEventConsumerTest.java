package com.felipe.policy.event.processor.infrastructure.kafka.consumers;

import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

class PaymentSubscriptionEventConsumerTest {

    private final InsurancePolicyRequestRepository mockRepository = Mockito.mock(InsurancePolicyRequestRepository.class);
    private final InsurancePolicyEventPublisher mockEventPublisher = Mockito.mock(InsurancePolicyEventPublisher.class);
    private final PaymentSubscriptionEventConsumer consumer = new PaymentSubscriptionEventConsumer(mockRepository, mockEventPublisher);

    @Test
    @DisplayName("Should handle valid event and update policy status to approved")
    void shouldHandleValidEventAndUpdatePolicyStatusToApproved() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "PAID", "AUTHORIZED", Instant.now());
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(id);
        policy.setHistory(new ArrayList<>());
        policy.setStatus(InsuranceRequestStatus.PENDING);
        policy.setCreatedAt(Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(policy));

        consumer.consume(event);

        Assertions.assertEquals(InsuranceRequestStatus.APPROVED, policy.getStatus());
        Mockito.verify(mockEventPublisher).publish(policy);
    }

    @Test
    @DisplayName("Should handle valid event and update policy status to rejected")
    void shouldHandleValidEventAndUpdatePolicyStatusToRejected() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "FAILED", "DECLINED", Instant.now());
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(id);
        policy.setHistory(new ArrayList<>());
        policy.setStatus(InsuranceRequestStatus.PENDING);
        policy.setCreatedAt(Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(policy));

        consumer.consume(event);

        Assertions.assertEquals(InsuranceRequestStatus.REJECTED, policy.getStatus());
        Mockito.verify(mockEventPublisher).publish(policy);
    }

    @Test
    @DisplayName("Should not update policy status if policy is cancelled")
    void shouldNotUpdatePolicyStatusIfPolicyIsCancelled() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "PAID", "AUTHORIZED", Instant.now());
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(id);
        policy.setStatus(InsuranceRequestStatus.CANCELLED);
        policy.setCreatedAt(Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(policy));

        consumer.consume(event);

        Assertions.assertEquals(InsuranceRequestStatus.CANCELLED, policy.getStatus());
        Mockito.verify(mockEventPublisher, Mockito.never()).publish(policy);
    }

    @Test
    @DisplayName("Should not update policy status if policy is not pending")
    void shouldNotUpdatePolicyStatusIfPolicyIsNotPending() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "PAID", "AUTHORIZED", Instant.now());
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(id);
        policy.setStatus(InsuranceRequestStatus.APPROVED);
        policy.setCreatedAt(Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(policy));

        consumer.consume(event);

        Assertions.assertEquals(InsuranceRequestStatus.APPROVED, policy.getStatus());
        Mockito.verify(mockEventPublisher, Mockito.never()).publish(policy);
    }

    @Test
    @DisplayName("Should not update policy status if event is outdated")
    void shouldNotUpdatePolicyStatusIfEventIsOutdated() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "PAID", "AUTHORIZED", Instant.now().minusSeconds(3600));
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(id);
        policy.setStatus(InsuranceRequestStatus.PENDING);
        policy.setCreatedAt(Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(policy));

        consumer.consume(event);

        Assertions.assertEquals(InsuranceRequestStatus.PENDING, policy.getStatus());
        Mockito.verify(mockEventPublisher, Mockito.never()).publish(policy);
    }

    @Test
    @DisplayName("Should log warning if policy is not found")
    void shouldLogWarningIfPolicyIsNotFound() {
        UUID id = UUID.randomUUID();
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO(id, "PAID", "AUTHORIZED", Instant.now());
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.empty());

        consumer.consume(event);

        Mockito.verify(mockEventPublisher, Mockito.never()).publish(Mockito.any());
    }
}