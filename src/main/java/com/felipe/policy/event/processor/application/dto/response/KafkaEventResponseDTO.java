package com.felipe.policy.event.processor.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaEventResponseDTO {
    private String status;
    private String message;
    private Instant timestamp;
}
