package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.StatusHistoryDTO;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;

import java.util.List;
import java.util.stream.Collectors;

public class StatusHistoryMapper {

    public static StatusHistoryDTO toDto(StatusHistory status) {
        return new StatusHistoryDTO(status.getStatus(), status.getTimestamp());
    }

    public static List<StatusHistoryDTO> toDtoList(List<StatusHistory> historyList) {
        return historyList.stream()
                .map(StatusHistoryMapper::toDto)
                .collect(Collectors.toList());
    }
}