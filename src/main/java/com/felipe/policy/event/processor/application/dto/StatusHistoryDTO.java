package com.felipe.policy.event.processor.application.dto;

import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusHistoryDTO {
    private InsuranceRequestStatus status;
    private Instant timestamp;
}
