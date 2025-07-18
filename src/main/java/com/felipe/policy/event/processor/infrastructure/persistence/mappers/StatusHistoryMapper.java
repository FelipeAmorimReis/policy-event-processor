package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.StatusHistoryDTO;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {

    StatusHistoryDTO toDto(StatusHistory status);

    StatusHistory toDomain(StatusHistoryDTO dto);
}