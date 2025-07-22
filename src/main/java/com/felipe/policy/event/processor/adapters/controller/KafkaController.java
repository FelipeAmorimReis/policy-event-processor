package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.response.KafkaEventResponseDTO;
import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import com.felipe.policy.event.processor.application.factories.PolicyResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<String, PaymentSubscriptionResultEventDTO> kafkaTemplate;

    private final PolicyResponseFactory responseFactory;
    @PostMapping("/simulate-payment-result")
    public ResponseEntity<KafkaEventResponseDTO> simulatePaymentResult(@RequestBody PaymentSubscriptionResultEventDTO event) {
        kafkaTemplate.send("payment-subscription-result", event);
        KafkaEventResponseDTO response = responseFactory.buildKafkaSuccess();
        return ResponseEntity.ok(response);
    }
}
