package com.felipe.policy.event.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.felipe.policy.event.processor")
@EnableJpaRepositories(basePackages = "com.felipe.policy.event.processor.infrastructure.persistence.repositories")
@EntityScan(basePackages = "com.felipe.policy.event.processor.infrastructure.persistence.entities")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
