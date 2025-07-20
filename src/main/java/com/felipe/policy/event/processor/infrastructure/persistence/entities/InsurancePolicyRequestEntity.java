package com.felipe.policy.event.processor.infrastructure.persistence.entities;

import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "insurance_policy_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequestEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private UUID customerId;
    private Long productId;

    @Enumerated(EnumType.STRING)
    private InsuranceCategory category;

    private String salesChannel;
    private String paymentMethod;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;

    @ElementCollection
    private Map<String, BigDecimal> coverages;

    @ElementCollection
    private List<String> assistances;

    private Instant createdAt;
    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    private InsuranceRequestStatus status;

    private Integer riskKey; //usado EXCLUSIVAMENTE apenas para controlar a classificação de risco simulada no WireMock

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_request_id")
    private List<StatusHistoryEntity> history;
}