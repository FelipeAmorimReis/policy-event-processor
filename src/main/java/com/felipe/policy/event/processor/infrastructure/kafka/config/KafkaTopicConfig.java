package com.felipe.policy.event.processor.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic insurancePolicyEventsTopic() {
        return new NewTopic("insurance-policy-events", 1, (short) 1);
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Tenta no máximo 3 vezes, com 1 segundo entre elas
        FixedBackOff backOff = new FixedBackOff(1000L, 3);
        return new DefaultErrorHandler(backOff);
    }

}
