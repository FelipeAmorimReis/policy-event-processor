package com.felipe.policy.event.processor.domain.entities;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequest {

    private UUID id;
    private UUID customerId;
    private UUID productId;
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
    private List<StatusHistory> history;

    public InsurancePolicyRequest(UUID customerId, UUID productId, InsuranceCategory category,
                                  String salesChannel, String paymentMethod,
                                  BigDecimal totalMonthlyPremiumAmount, BigDecimal insuredAmount,
                                  Map<String, BigDecimal> coverages, List<String> assistances) {

        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.productId = productId;
        this.category = category;
        this.salesChannel = salesChannel;
        this.paymentMethod = paymentMethod;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.insuredAmount = insuredAmount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.createdAt = Instant.now();
        this.status = InsuranceRequestStatus.RECEIVED;
        this.history = new ArrayList<>();
        this.history.add(new StatusHistory(this.status, this.createdAt));
    }

    public void updateStatus(InsuranceRequestStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status must not be null.");
        }

        if (this.status == InsuranceRequestStatus.APPROVED ||
                this.status == InsuranceRequestStatus.REJECTED ||
                this.status == InsuranceRequestStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status after final state.");
        }

        this.status = newStatus;
        Instant now = Instant.now();
        this.history.add(new StatusHistory(newStatus, now));

        if (newStatus == InsuranceRequestStatus.APPROVED ||
                newStatus == InsuranceRequestStatus.REJECTED ||
                newStatus == InsuranceRequestStatus.CANCELLED) {
            this.finishedAt = now;
        }
    }

}
