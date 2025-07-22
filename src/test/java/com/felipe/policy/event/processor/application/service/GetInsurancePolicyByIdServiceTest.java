package com.felipe.policy.event.processor.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class GetInsurancePolicyByIdServiceTest {

    @Test
    void policyIsRetrievedSuccessfullyById() {
        UUID policyId = UUID.randomUUID();
        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        InsurancePolicyRequest domain = new InsurancePolicyRequest();
        InsurancePolicyResponseDTO responseDTO = new InsurancePolicyResponseDTO();

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        InsurancePolicyPersistenceMapper persistenceMapper = mock(InsurancePolicyPersistenceMapper.class);
        InsurancePolicyMapper dtoMapper = mock(InsurancePolicyMapper.class);

        when(repository.findById(policyId)).thenReturn(Optional.of(entity));
        when(persistenceMapper.toDomain(entity)).thenReturn(domain);
        when(dtoMapper.toResponseDTO(domain)).thenReturn(responseDTO);

        GetInsurancePolicyByIdService service = new GetInsurancePolicyByIdService(repository, persistenceMapper, dtoMapper);
        InsurancePolicyResponseDTO result = service.execute(policyId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseDTO, result);
    }

    @Test
    void exceptionIsThrownWhenPolicyNotFound() {
        UUID policyId = UUID.randomUUID();

        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        InsurancePolicyPersistenceMapper persistenceMapper = mock(InsurancePolicyPersistenceMapper.class);
        InsurancePolicyMapper dtoMapper = mock(InsurancePolicyMapper.class);

        when(repository.findById(policyId)).thenReturn(Optional.empty());

        GetInsurancePolicyByIdService service = new GetInsurancePolicyByIdService(repository, persistenceMapper, dtoMapper);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.execute(policyId));
        Assertions.assertTrue(exception.getMessage().contains(policyId.toString()));
    }
}