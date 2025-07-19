package com.felipe.policy.event.processor.infrastructure.kafka.producers;

import com.felipe.policy.event.processor.application.dto.InsuranceEventDTO;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Builder
@Data
@RequiredArgsConstructor
public class InsurancePolicyEventPublisher {

    private final KafkaTemplate<String, InsuranceEventDTO> kafkaTemplate;

    public void publish(InsurancePolicyRequestEntity entity) {
        InsuranceEventDTO event = InsuranceEventDTO.builder()
                .orderId(entity.getId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus())
                .timestamp(entity.getCreatedAt())
                .build();

        kafkaTemplate.send("insurance-policy-created", event);
    }
}