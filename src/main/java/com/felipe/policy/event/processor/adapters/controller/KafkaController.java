package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.response.KafkaEventResponseDTO;
import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<String, PaymentSubscriptionResultEventDTO> kafkaTemplate;

    @PostMapping("/simulate-payment-result")
    public ResponseEntity<KafkaEventResponseDTO> simulatePaymentResult(@RequestBody PaymentSubscriptionResultEventDTO event) {
        kafkaTemplate.send("payment-subscription-result", event);

        KafkaEventResponseDTO response = KafkaEventResponseDTO.builder()
                .orderId(event.getOrderId())
                .status("EVENT_SENT")
                .message("Evento enviado com sucesso para o Kafka.")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
