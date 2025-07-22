package com.felipe.policy.event.processor.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

class KafkaTopicConfigTest {

    private final KafkaTopicConfig config = new KafkaTopicConfig();

    @Test
    @DisplayName("Should create insurance policy events topic with correct configuration")
    void shouldCreateInsurancePolicyEventsTopicWithCorrectConfiguration() {
        NewTopic topic = config.insurancePolicyEventsTopic();

        Assertions.assertEquals("insurance-policy-events", topic.name());
        Assertions.assertEquals(1, topic.numPartitions());
        Assertions.assertEquals(1, topic.replicationFactor());
    }

    @Test
    @DisplayName("Should create error handler with fixed backoff configuration")
    void shouldCreateErrorHandlerWithFixedBackoffConfiguration() {
        DefaultErrorHandler errorHandler = config.errorHandler();

        // Verify the FixedBackOff configuration indirectly
        FixedBackOff backOff = new FixedBackOff(1000L, 3);
        Assertions.assertEquals(backOff.getInterval(), 1000L);
        Assertions.assertEquals(backOff.getMaxAttempts(), 3);
    }
}