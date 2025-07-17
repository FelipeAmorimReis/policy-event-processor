package com.felipe.policy.event.processor.application.dto;

import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAnalysisResponseDTO {
    private UUID orderId;
    private UUID customerId;
    private Instant analyzedAt;
    private RiskClassification classification;
    private List<FraudOccurrenceDTO> occurrences;
}
