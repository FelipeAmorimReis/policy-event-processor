package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;

public class StatusHistoryEntityMapper {

    public static StatusHistoryEntity toEntity(StatusHistory domain) {
        return StatusHistoryEntity.builder()
                .status(domain.getStatus())
                .timestamp(domain.getTimestamp())
                .build();
    }

    public static StatusHistory toDomain(StatusHistoryEntity entity) {
        return new StatusHistory(entity.getStatus(), entity.getTimestamp());
    }
}