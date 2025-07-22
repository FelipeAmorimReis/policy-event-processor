package com.felipe.policy.event.processor.application.factories;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.KafkaEventResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PolicyResponseFactoryTest {

    @Test
    void cancelResponseIsBuiltCorrectly() {
        PolicyResponseFactory factory = new PolicyResponseFactory();
        CancelPolicyResponseDTO response = factory.buildCancelResponse();
        Assertions.assertEquals("Policy cancellation succeeded.", response.getMessage());
        Assertions.assertEquals("CANCELLED", response.getStatus());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void reprocessAcceptedResponseIsBuiltCorrectly() {
        PolicyResponseFactory factory = new PolicyResponseFactory();
        ReprocessPolicyResponseDTO response = factory.buildReprocessAccepted();
        Assertions.assertEquals("Policy successfully sent for reprocessing.", response.getMessage());
        Assertions.assertEquals("REPROCESSING_ACCEPTED", response.getStatus());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void reprocessRejectedResponseIsBuiltCorrectly() {
        PolicyResponseFactory factory = new PolicyResponseFactory();
        String currentStatus = "Pending";
        ReprocessPolicyResponseDTO response = factory.buildReprocessRejected(currentStatus);
        Assertions.assertEquals("Reprocessing denied. Policy is in invalid state: Pending", response.getMessage());
        Assertions.assertEquals("REPROCESSING_REJECTED", response.getStatus());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void kafkaSuccessResponseIsBuiltCorrectly() {
        PolicyResponseFactory factory = new PolicyResponseFactory();
        KafkaEventResponseDTO response = factory.buildKafkaSuccess();
        Assertions.assertEquals("Kafka | Event successfully sent to topic.", response.getStatus());
        Assertions.assertEquals("EVENT_SENT", response.getMessage());
        Assertions.assertNotNull(response.getTimestamp());
    }
}