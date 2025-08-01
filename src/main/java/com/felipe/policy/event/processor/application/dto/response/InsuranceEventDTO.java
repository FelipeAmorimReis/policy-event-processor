package com.felipe.policy.event.processor.application.dto.response;

import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceEventDTO {
    private UUID orderId;
    private UUID customerId;
    private InsuranceRequestStatus status;
    private Instant timestamp;
}