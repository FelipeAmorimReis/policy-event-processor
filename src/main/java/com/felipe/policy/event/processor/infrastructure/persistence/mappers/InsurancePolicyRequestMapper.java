package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;

public class InsurancePolicyRequestMapper {
    public static InsurancePolicyRequest toEntity(InsurancePolicyRequestDTO dto) {
        return InsurancePolicyRequest.builder()
                .customerId(dto.getCustomerId())
                .productId(dto.getProductId())
                .category(dto.getCategory())
                .salesChannel(dto.getSalesChannel())
                .paymentMethod(dto.getPaymentMethod())
                .totalMonthlyPremiumAmount(dto.getTotalMonthlyPremiumAmount())
                .insuredAmount(dto.getInsuredAmount())
                .coverages(dto.getCoverages())
                .assistances(dto.getAssistances())
                .build();
    }

    public static InsurancePolicyResponseDTO toResponseDTO(InsurancePolicyRequest entity) {
        return InsurancePolicyResponseDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .category(entity.getCategory())
                .salesChannel(entity.getSalesChannel())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .finishedAt(entity.getFinishedAt())
                .totalMonthlyPremiumAmount(entity.getTotalMonthlyPremiumAmount())
                .insuredAmount(entity.getInsuredAmount())
                .coverages(entity.getCoverages())
                .assistances(entity.getAssistances())
                .history(StatusHistoryMapper.toDtoList(entity.getHistory()))
                .build();
    }
}
