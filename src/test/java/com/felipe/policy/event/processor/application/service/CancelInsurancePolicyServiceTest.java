package com.felipe.policy.event.processor.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.exceptions.InvalidPolicyStatusException;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CancelInsurancePolicyServiceTest {

    @Test
    void policyIsCancelledSuccessfully() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setStatus(InsuranceRequestStatus.PENDING);
        entity.setHistory(new ArrayList<>());

        InsurancePolicyRequestRepository policyRepo = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(entity));
        when(policyRepo.save(Mockito.any(InsurancePolicyRequestEntity.class))).thenReturn(entity);
        when(responseFactory.buildCancelResponse()).thenReturn(new CancelPolicyResponseDTO("Cancel success message", "Cancel status", Instant.now()));

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(policyRepo, responseFactory, eventPublisher);
        CancelPolicyResponseDTO response = service.execute(policyId);

        Assertions.assertEquals("Cancel success message", response.getMessage());
        Assertions.assertEquals("Cancel status", response.getStatus());
        Assertions.assertNotNull(response.getTimestamp());
        verify(eventPublisher).publish(entity);
    }

    @Test
    void exceptionThrownWhenPolicyNotFound() {
        UUID policyId = UUID.randomUUID();

        InsurancePolicyRequestRepository policyRepo = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(policyRepo.findById(policyId)).thenReturn(Optional.empty());

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(policyRepo, responseFactory, eventPublisher);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.execute(policyId));
        Assertions.assertEquals("Cancel | No policy found with ID: {}" + policyId, exception.getMessage());
    }

    @Test
    void exceptionThrownWhenPolicyStatusIsInvalid() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setStatus(InsuranceRequestStatus.APPROVED);

        InsurancePolicyRequestRepository policyRepo = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(entity));

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(policyRepo, responseFactory, eventPublisher);

        InvalidPolicyStatusException exception = Assertions.assertThrows(InvalidPolicyStatusException.class, () -> service.execute(policyId));
        Assertions.assertEquals("Cancellation not allowed: policy is already APPROVED or REJECTED.", exception.getMessage());
    }
}