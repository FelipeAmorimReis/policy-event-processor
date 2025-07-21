package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { StatusHistoryMapper.class })
public interface InsurancePolicyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "history", ignore = true)
    InsurancePolicyRequest toDomain(InsurancePolicyRequestDTO dto);

    InsurancePolicyResponseDTO toResponseDTO(InsurancePolicyRequest domain);
}
