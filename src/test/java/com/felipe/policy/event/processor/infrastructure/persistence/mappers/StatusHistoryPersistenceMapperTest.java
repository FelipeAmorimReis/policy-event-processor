package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StatusHistoryPersistenceMapperTest {

    private final StatusHistoryPersistenceMapper mapper = Mockito.mock(StatusHistoryPersistenceMapper.class);

    @Test
    @DisplayName("Should map domain to entity successfully when valid domain is provided")
    void shouldMapDomainToEntitySuccessfully() {
        StatusHistory validDomain = new StatusHistory(/* valid data */);
        StatusHistoryEntity expectedEntity = new StatusHistoryEntity(/* expected data */);

        Mockito.when(mapper.toEntity(validDomain)).thenReturn(expectedEntity);

        StatusHistoryEntity actualEntity = mapper.toEntity(validDomain);

        Assertions.assertEquals(expectedEntity, actualEntity);
    }

    @Test
    @DisplayName("Should map entity to domain successfully when valid entity is provided")
    void shouldMapEntityToDomainSuccessfully() {
        StatusHistoryEntity validEntity = new StatusHistoryEntity(/* valid data */);
        StatusHistory expectedDomain = new StatusHistory(/* expected data */);

        Mockito.when(mapper.toDomain(validEntity)).thenReturn(expectedDomain);

        StatusHistory actualDomain = mapper.toDomain(validEntity);

        Assertions.assertEquals(expectedDomain, actualDomain);
    }

    @Test
    @DisplayName("Should throw exception when null domain is provided for entity mapping")
    void shouldThrowExceptionWhenNullDomainProvidedForEntityMapping() {
        Mockito.when(mapper.toEntity(null)).thenThrow(new IllegalArgumentException("Domain cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should throw exception when null entity is provided for domain mapping")
    void shouldThrowExceptionWhenNullEntityProvidedForDomainMapping() {
        Mockito.when(mapper.toDomain(null)).thenThrow(new IllegalArgumentException("Entity cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(null));
    }
}