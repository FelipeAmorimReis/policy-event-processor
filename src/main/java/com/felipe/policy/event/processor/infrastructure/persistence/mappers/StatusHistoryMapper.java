package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.response.StatusHistoryDTO;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {
    StatusHistoryDTO toDto(StatusHistory domain);

    StatusHistory toDomain(StatusHistoryDTO dto);
}