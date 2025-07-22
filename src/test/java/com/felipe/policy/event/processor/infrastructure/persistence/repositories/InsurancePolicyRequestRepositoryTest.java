package com.felipe.policy.event.processor.infrastructure.persistence.repositories;

import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyRequestRepositoryTest {

    private final InsurancePolicyRequestRepository repository = Mockito.mock(InsurancePolicyRequestRepository.class);

    @Test
    @DisplayName("Should find insurance policy request by valid ID")
    void shouldFindInsurancePolicyRequestByValidId() {
        UUID validId = UUID.randomUUID();
        InsurancePolicyRequestEntity expectedEntity = new InsurancePolicyRequestEntity(/* valid data */);

        Mockito.when(repository.findById(validId)).thenReturn(Optional.of(expectedEntity));

        Optional<InsurancePolicyRequestEntity> actualEntity = repository.findById(validId);

        Assertions.assertTrue(actualEntity.isPresent());
        Assertions.assertEquals(expectedEntity, actualEntity.get());
    }

    @Test
    @DisplayName("Should return empty optional when ID does not exist")
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        Mockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<InsurancePolicyRequestEntity> actualEntity = repository.findById(nonExistentId);

        Assertions.assertFalse(actualEntity.isPresent());
    }

    @Test
    @DisplayName("Should throw exception when null ID is provided")
    void shouldThrowExceptionWhenNullIdProvided() {
        Mockito.when(repository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
    }
}