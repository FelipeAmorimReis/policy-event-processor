package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReprocessInsurancePolicyServiceTest {

    @Test
    void policyIsReprocessedSuccessfullyWhenStatusIsPending() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(policyId);
        policy.setStatus(InsuranceRequestStatus.PENDING);
        policy.setHistory(new ArrayList<>());

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));
        when(responseFactory.buildReprocessAccepted()).thenReturn(new ReprocessPolicyResponseDTO());

        ReprocessInsurancePolicyService service = new ReprocessInsurancePolicyService(repository, eventPublisher, responseFactory);
        ReprocessPolicyResponseDTO response = service.execute(policyId);

        Assertions.assertNotNull(response);
        verify(repository).save(policy);
        verify(eventPublisher).publish(policy);
    }

    @Test
    void policyReprocessIsRejectedWhenStatusIsNotPending() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(policyId);
        policy.setStatus(InsuranceRequestStatus.RECEIVED);

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));
        when(responseFactory.buildReprocessRejected(policy.getStatus().name())).thenReturn(new ReprocessPolicyResponseDTO());

        ReprocessInsurancePolicyService service = new ReprocessInsurancePolicyService(repository, eventPublisher, responseFactory);
        ReprocessPolicyResponseDTO response = service.execute(policyId);

        Assertions.assertNotNull(response);
        verify(repository, never()).save(policy);
        verify(eventPublisher, never()).publish(policy);
    }

    @Test
    void exceptionIsThrownWhenPolicyDoesNotExist() {
        UUID policyId = UUID.randomUUID();

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);

        when(repository.findById(policyId)).thenReturn(Optional.empty());

        ReprocessInsurancePolicyService service = new ReprocessInsurancePolicyService(repository, eventPublisher, responseFactory);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.execute(policyId));
        Assertions.assertTrue(exception.getMessage().contains(policyId.toString()));
    }
}