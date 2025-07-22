package com.felipe.policy.event.processor.application.usecases;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.exceptions.InvalidPolicyStatusException;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import com.felipe.policy.event.processor.application.service.CancelInsurancePolicyService;
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

class CancelInsurancePolicyUseCaseTest {

    @Test
    void policyIsCancelledSuccessfully() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(policyId);
        policy.setStatus(InsuranceRequestStatus.RECEIVED);
        policy.setHistory(new ArrayList<>());

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));
        when(responseFactory.buildCancelResponse()).thenReturn(new CancelPolicyResponseDTO());

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(repository, responseFactory, eventPublisher);
        when(repository.save(any(InsurancePolicyRequestEntity.class))).thenReturn(policy);
        CancelPolicyResponseDTO response = service.execute(policyId);

        Assertions.assertNotNull(response);
        verify(repository).save(policy);
        verify(eventPublisher).publish(policy);
    }

    @Test
    void policyCancellationIsRejectedWhenStatusIsNotCancelable() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity policy = new InsurancePolicyRequestEntity();
        policy.setId(policyId);
        policy.setStatus(InsuranceRequestStatus.APPROVED);

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(repository, responseFactory, eventPublisher);

        InvalidPolicyStatusException exception = Assertions.assertThrows(InvalidPolicyStatusException.class, () -> service.execute(policyId));
        Assertions.assertFalse(exception.getMessage().contains("Invalid status for cancellation"));
        verify(repository, never()).save(policy);
        verify(eventPublisher, never()).publish(policy);
    }

    @Test
    void exceptionIsThrownWhenPolicyDoesNotExist() {
        UUID policyId = UUID.randomUUID();

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(repository.findById(policyId)).thenReturn(Optional.empty());

        CancelInsurancePolicyService service = new CancelInsurancePolicyService(repository, responseFactory, eventPublisher);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.execute(policyId));
        Assertions.assertTrue(exception.getMessage().contains(policyId.toString()));
    }
}