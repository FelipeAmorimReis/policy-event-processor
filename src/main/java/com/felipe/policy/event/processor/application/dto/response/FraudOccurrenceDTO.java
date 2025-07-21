package com.felipe.policy.event.processor.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudOccurrenceDTO {
    private UUID id;
    private Long productId;
    private String type;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
