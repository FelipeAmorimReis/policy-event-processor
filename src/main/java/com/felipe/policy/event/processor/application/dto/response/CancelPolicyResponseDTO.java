package com.felipe.policy.event.processor.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelPolicyResponseDTO {
    private String message;
    private String status;
    private Instant timestamp;
}