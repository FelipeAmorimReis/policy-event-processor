package com.felipe.policy.event.processor.domain.entities;

import com.felipe.policy.event.processor.application.exceptions.InvalidPolicyStatusException;
import com.felipe.policy.event.processor.application.exceptions.InvalidRequestException;
import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.STATUS_CANNOT_BE_NULL;
import static com.felipe.policy.event.processor.application.constants.MessageConstants.STATUS_CANNOT_CHANGE_AFTER_FINAL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequest {

    private UUID id;
    private UUID customerId;
    private Long productId;
    private InsuranceCategory category;
    private String salesChannel;
    private String paymentMethod;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private Instant createdAt;
    private Instant finishedAt;
    private InsuranceRequestStatus status;
    private Integer riskKey; // usado EXCLUSIVAMENTE apenas para controlar a classificação de risco simulada no WireMock
    private List<StatusHistory> history;

    public void updateStatus(InsuranceRequestStatus newStatus) {
        if (newStatus == null) {
            throw new InvalidRequestException(STATUS_CANNOT_BE_NULL);
        }

        if (this.status == InsuranceRequestStatus.APPROVED ||
                this.status == InsuranceRequestStatus.REJECTED ||
                this.status == InsuranceRequestStatus.CANCELLED) {
            throw new InvalidPolicyStatusException(STATUS_CANNOT_CHANGE_AFTER_FINAL);
        }

        this.status = newStatus;
        Instant now = Instant.now();
        getHistory().add(new StatusHistory(newStatus, now));

        if (newStatus == InsuranceRequestStatus.APPROVED ||
                newStatus == InsuranceRequestStatus.REJECTED ||
                newStatus == InsuranceRequestStatus.CANCELLED) {
            this.finishedAt = now;
        }
    }

    public List<StatusHistory> getHistory() {
        if (history == null) {
            history = new ArrayList<>();
        }
        return history;
    }
}
