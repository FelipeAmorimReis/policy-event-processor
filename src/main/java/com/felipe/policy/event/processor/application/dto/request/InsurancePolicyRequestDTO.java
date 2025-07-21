package com.felipe.policy.event.processor.application.dto.request;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequestDTO {
    private UUID customerId;
    private Long productId;
    private InsuranceCategory category;
    private String salesChannel;
    private String paymentMethod;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;

    @Min(value = 1, message = "riskKey deve ser no mínimo 1")
    @Max(value = 4, message = "riskKey deve ser no máximo 4")
    private Integer riskKey; // usado EXCLUSIVAMENTE apenas para controlar a classificação de risco simulada no WireMock
}
