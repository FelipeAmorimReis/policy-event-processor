package com.felipe.policy.event.processor.application.dto;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyResponseDTO {
    private UUID id;
    private UUID customerId;
    private Long productId;
    private InsuranceCategory category;
    private String salesChannel;
    private String paymentMethod;
    private InsuranceRequestStatus status;
    private Instant createdAt;
    private Instant finishedAt;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private List<StatusHistoryDTO> history;
}
