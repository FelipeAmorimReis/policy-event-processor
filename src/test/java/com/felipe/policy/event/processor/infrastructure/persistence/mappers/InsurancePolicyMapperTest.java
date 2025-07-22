package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyMapperTest {

    private final InsurancePolicyMapper mapper = Mockito.mock(InsurancePolicyMapper.class);

    @Test
    @DisplayName("Should map DTO to domain successfully when valid DTO is provided")
    void shouldMapDtoToDomainSuccessfully() {
        InsurancePolicyRequestDTO validDto = new InsurancePolicyRequestDTO(/* valid data */);
        InsurancePolicyRequest expectedDomain = new InsurancePolicyRequest(/* expected data */);

        Mockito.when(mapper.toDomain(validDto)).thenReturn(expectedDomain);

        InsurancePolicyRequest actualDomain = mapper.toDomain(validDto);

        Assertions.assertEquals(expectedDomain, actualDomain);
    }

    @Test
    @DisplayName("Should map domain to response DTO successfully when valid domain is provided")
    void shouldMapDomainToResponseDtoSuccessfully() {
        InsurancePolicyRequest validDomain = new InsurancePolicyRequest(/* valid data */);
        InsurancePolicyResponseDTO expectedResponseDto = new InsurancePolicyResponseDTO(/* expected data */);

        Mockito.when(mapper.toResponseDTO(validDomain)).thenReturn(expectedResponseDto);

        InsurancePolicyResponseDTO actualResponseDto = mapper.toResponseDTO(validDomain);

        Assertions.assertEquals(expectedResponseDto, actualResponseDto);
    }

    @Test
    @DisplayName("Should throw exception when null DTO is provided for domain mapping")
    void shouldThrowExceptionWhenNullDtoProvidedForDomainMapping() {
        Mockito.when(mapper.toDomain(null)).thenThrow(new IllegalArgumentException("DTO cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should throw exception when null domain is provided for response DTO mapping")
    void shouldThrowExceptionWhenNullDomainProvidedForResponseDtoMapping() {
        Mockito.when(mapper.toResponseDTO(null)).thenThrow(new IllegalArgumentException("Domain cannot be null"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.toResponseDTO(null));
    }
}