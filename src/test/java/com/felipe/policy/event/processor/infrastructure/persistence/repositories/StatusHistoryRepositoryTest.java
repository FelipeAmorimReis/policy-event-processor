package com.felipe.policy.event.processor.infrastructure.persistence.repositories;

import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StatusHistoryRepositoryTest {

    private final StatusHistoryRepository repository = Mockito.mock(StatusHistoryRepository.class);

    @Test
    @DisplayName("Should find status history by valid ID")
    void shouldFindStatusHistoryByValidId() {
        Long validId = 1L;
        StatusHistoryEntity expectedEntity = new StatusHistoryEntity(/* valid data */);

        Mockito.when(repository.findById(validId)).thenReturn(Optional.of(expectedEntity));

        Optional<StatusHistoryEntity> actualEntity = repository.findById(validId);

        Assertions.assertTrue(actualEntity.isPresent());
        Assertions.assertEquals(expectedEntity, actualEntity.get());
    }

    @Test
    @DisplayName("Should return empty optional when ID does not exist")
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Long nonExistentId = 999L;

        Mockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<StatusHistoryEntity> actualEntity = repository.findById(nonExistentId);

        Assertions.assertFalse(actualEntity.isPresent());
    }

    @Test
    @DisplayName("Should throw exception when null ID is provided")
    void shouldThrowExceptionWhenNullIdProvided() {
        Mockito.when(repository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
    }
}