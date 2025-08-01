package com.felipe.policy.event.processor.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReprocessPolicyResponseDTO {
    private String message;
    private String status;
    private Instant timestamp;
}
