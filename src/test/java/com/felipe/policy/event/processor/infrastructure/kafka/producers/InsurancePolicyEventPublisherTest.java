package com.felipe.policy.event.processor.infrastructure.kafka.producers;

import com.felipe.policy.event.processor.application.dto.response.InsuranceEventDTO;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyEventPublisherTest {

    private final KafkaTemplate<String, InsuranceEventDTO> mockKafkaTemplate = Mockito.mock(KafkaTemplate.class);
    private final InsurancePolicyEventPublisher publisher = new InsurancePolicyEventPublisher(mockKafkaTemplate);

    @Test
    @DisplayName("Should publish event with correct data")
    void shouldPublishEventWithCorrectData() {
        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setId(UUID.randomUUID());
        entity.setCustomerId(UUID.randomUUID());
        entity.setStatus(InsuranceRequestStatus.APPROVED);
        entity.setCreatedAt(Instant.now());

        publisher.publish(entity);

        InsuranceEventDTO expectedEvent = InsuranceEventDTO.builder()
                .orderId(entity.getId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus())
                .timestamp(entity.getCreatedAt())
                .build();

        Mockito.verify(mockKafkaTemplate).send("insurance-policy-events", expectedEvent);
    }

    @Test
    @DisplayName("Should throw exception if KafkaTemplate fails")
    void shouldThrowExceptionIfKafkaTemplateFails() {
        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setId(UUID.randomUUID());
        entity.setCustomerId(UUID.randomUUID());
        entity.setStatus(InsuranceRequestStatus.APPROVED);
        entity.setCreatedAt(Instant.now());

        Mockito.doThrow(new RuntimeException("Kafka error")).when(mockKafkaTemplate).send(Mockito.anyString(), Mockito.any());

        Assertions.assertThrows(RuntimeException.class, () -> publisher.publish(entity));
    }
}