package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusHistoryPersistenceMapper {

    StatusHistoryEntity toEntity(StatusHistory domain);

    StatusHistory toDomain(StatusHistoryEntity entity);
}