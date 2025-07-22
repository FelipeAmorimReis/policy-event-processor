package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.response.StatusHistoryDTO;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StatusHistoryMapperTest {

    private final StatusHistoryMapper mapper = Mockito.mock(StatusHistoryMapper.class);

    @Test
    @DisplayName("Should map domain to DTO successfully when valid domain is provided")
    void shouldMapDomainToDtoSuccessfully() {
        StatusHistory validDomain = new StatusHistory(/* valid data */);
        StatusHistoryDTO expectedDto = new StatusHistoryDTO(/* expected data */);

        Mockito.when(mapper.toDto(validDomain)).thenReturn(expectedDto);

        StatusHistoryDTO actualDto = mapper.toDto(validDomain);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Should map DTO to domain successfully when valid DTO is provided")
    void shouldMapDtoToDomainSuccessfully() {
        StatusHistoryDTO validDto = new StatusHistoryDTO(/* valid data */);
        StatusHistory expectedDomain = new StatusHistory(/* expected data */);

        Mockito.when(mapper.toDomain(validDto)).thenReturn(expectedDomain);

        StatusHistory actualDomain = mapper.toDomain(validDto);

        Assertions.assertEquals(expectedDomain, actualDomain);
    }

    @Test
    @DisplayName("Should throw exception when null domain is provided for DTO mapping")
    void shouldThrowExceptionWhenNullDomainProvidedForDtoMapping() {
        Mockito.when(mapper.toDto(null)).thenThrow(new IllegalArgumentException("Domain cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toDto(null));
    }

    @Test
    @DisplayName("Should throw exception when null DTO is provided for domain mapping")
    void shouldThrowExceptionWhenNullDtoProvidedForDomainMapping() {
        Mockito.when(mapper.toDomain(null)).thenThrow(new IllegalArgumentException("DTO cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(null));
    }
}