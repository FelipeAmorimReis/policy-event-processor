package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { StatusHistoryMapper.class })
public interface InsurancePolicyMapper {

    InsurancePolicyRequest toDomain(InsurancePolicyRequestDTO dto);

    InsurancePolicyResponseDTO toResponseDTO(InsurancePolicyRequest domain);
}
