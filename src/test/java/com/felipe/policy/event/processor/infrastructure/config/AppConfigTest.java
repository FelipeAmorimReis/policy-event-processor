package com.felipe.policy.event.processor.infrastructure.config;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    @DisplayName("Should create a non-null RestTemplate bean")
    void shouldCreateNonNullRestTemplateBean() {
        RestTemplate restTemplate = appConfig.restTemplate();

        Assertions.assertNotNull(restTemplate);
    }

    @Test
    @DisplayName("Should create a new instance of RestTemplate each time")
    void shouldCreateNewInstanceOfRestTemplateEachTime() {
        RestTemplate restTemplate1 = appConfig.restTemplate();
        RestTemplate restTemplate2 = appConfig.restTemplate();

        Assertions.assertNotSame(restTemplate1, restTemplate2);
    }
}