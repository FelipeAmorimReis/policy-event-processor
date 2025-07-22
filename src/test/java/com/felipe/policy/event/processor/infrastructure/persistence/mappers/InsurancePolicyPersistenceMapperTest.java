package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyPersistenceMapperTest {

    private final InsurancePolicyPersistenceMapper mapper = Mockito.mock(InsurancePolicyPersistenceMapper.class);

    @Test
    @DisplayName("Should map domain to entity successfully when valid domain is provided")
    void shouldMapDomainToEntitySuccessfully() {
        InsurancePolicyRequest validDomain = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .productId(123L)
                .build();
        InsurancePolicyRequestEntity expectedEntity = new InsurancePolicyRequestEntity(/* expected data */);

        Mockito.when(mapper.toEntity(validDomain)).thenReturn(expectedEntity);

        InsurancePolicyRequestEntity actualEntity = mapper.toEntity(validDomain);

        Assertions.assertEquals(expectedEntity, actualEntity);
    }

    @Test
    @DisplayName("Should map entity to domain successfully when valid entity is provided")
    void shouldMapEntityToDomainSuccessfully() {
        InsurancePolicyRequestEntity validEntity = new InsurancePolicyRequestEntity(/* valid data */);
        InsurancePolicyRequest expectedDomain = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .productId(123L)
                .build();

        Mockito.when(mapper.toDomain(validEntity)).thenReturn(expectedDomain);

        InsurancePolicyRequest actualDomain = mapper.toDomain(validEntity);

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