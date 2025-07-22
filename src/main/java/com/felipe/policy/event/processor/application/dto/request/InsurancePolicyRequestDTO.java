package com.felipe.policy.event.processor.application.dto.request;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.RISK_KEY_MAXIMO;
import static com.felipe.policy.event.processor.application.constants.MessageConstants.RISK_KEY_MINIMAL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequestDTO {
    @NotNull
    private UUID customerId;
    @NotNull
    private Long productId;
    @NotNull
    private InsuranceCategory category;
    @NotNull
    private String salesChannel;
    @NotNull
    private String paymentMethod;
    @NotNull
    private BigDecimal totalMonthlyPremiumAmount;
    @NotNull
    private BigDecimal insuredAmount;
    @NotNull
    private Map<String, BigDecimal> coverages;
    @NotNull
    private List<String> assistances;

    @Min(value = 1, message = RISK_KEY_MINIMAL)
    @Max(value = 4, message = RISK_KEY_MAXIMO)
    private Integer riskKey; // usado EXCLUSIVAMENTE apenas para controlar a classificação de risco simulada no WireMock
}
