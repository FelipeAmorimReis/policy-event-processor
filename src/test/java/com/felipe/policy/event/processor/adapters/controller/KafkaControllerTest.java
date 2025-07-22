package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import com.felipe.policy.event.processor.application.dto.response.KafkaEventResponseDTO;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaControllerTest {

    private final KafkaTemplate<String, PaymentSubscriptionResultEventDTO> kafkaTemplate = mock(KafkaTemplate.class);
    private final PolicyResponseFactory responseFactory = mock(PolicyResponseFactory.class);

    private final KafkaController controller = new KafkaController(kafkaTemplate, responseFactory);

    @Test
    @DisplayName("Simulate payment result successfully")
    void simulatePaymentResultSuccessfully() {
        PaymentSubscriptionResultEventDTO event = new PaymentSubscriptionResultEventDTO();
        KafkaEventResponseDTO responseDTO = new KafkaEventResponseDTO();
        when(responseFactory.buildKafkaSuccess()).thenReturn(responseDTO);

        ResponseEntity<KafkaEventResponseDTO> response = controller.simulatePaymentResult(event);

        assertEquals(ResponseEntity.ok(responseDTO), response);
        verify(kafkaTemplate, times(1)).send("payment-subscription-result", event);
        verify(responseFactory, times(1)).buildKafkaSuccess();
    }

    @Test
    @DisplayName("Simulate payment result with null event")
    void simulatePaymentResultWithNullEvent() {
        PaymentSubscriptionResultEventDTO event = null;
        KafkaEventResponseDTO responseDTO = new KafkaEventResponseDTO();
        when(responseFactory.buildKafkaSuccess()).thenReturn(responseDTO);

        ResponseEntity<KafkaEventResponseDTO> response = controller.simulatePaymentResult(event);

        assertEquals(ResponseEntity.ok(responseDTO), response);
        verify(kafkaTemplate, times(1)).send("payment-subscription-result", event);
        verify(responseFactory, times(1)).buildKafkaSuccess();
    }
}
