package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;

import java.util.stream.Collectors;

public class InsurancePolicyRequestEntityMapper {
    public static InsurancePolicyRequestEntity toEntity(InsurancePolicyRequest domain) {
        return InsurancePolicyRequestEntity.builder()
                .id(domain.getId())
                .customerId(domain.getCustomerId())
                .productId(domain.getProductId())
                .category(domain.getCategory())
                .salesChannel(domain.getSalesChannel())
                .paymentMethod(domain.getPaymentMethod())
                .totalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount())
                .insuredAmount(domain.getInsuredAmount())
                .coverages(domain.getCoverages())
                .assistances(domain.getAssistances())
                .createdAt(domain.getCreatedAt())
                .finishedAt(domain.getFinishedAt())
                .status(domain.getStatus())
                .history(domain.getHistory().stream()
                        .map(StatusHistoryEntityMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public static InsurancePolicyRequest toDomain(InsurancePolicyRequestEntity entity) {
        return InsurancePolicyRequest.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .category(entity.getCategory())
                .salesChannel(entity.getSalesChannel())
                .paymentMethod(entity.getPaymentMethod())
                .totalMonthlyPremiumAmount(entity.getTotalMonthlyPremiumAmount())
                .insuredAmount(entity.getInsuredAmount())
                .coverages(entity.getCoverages())
                .assistances(entity.getAssistances())
                .createdAt(entity.getCreatedAt())
                .finishedAt(entity.getFinishedAt())
                .status(entity.getStatus())
                .history(entity.getHistory().stream()
                        .map(StatusHistoryEntityMapper::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}
