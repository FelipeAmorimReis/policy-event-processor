package com.felipe.policy.event.processor.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSubscriptionResultEventDTO {
    private UUID orderId;
    private String paymentStatus;
    private String subscriptionStatus;
    private Instant analyzedAt;
}
