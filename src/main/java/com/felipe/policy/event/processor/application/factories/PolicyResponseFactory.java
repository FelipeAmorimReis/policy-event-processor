package com.felipe.policy.event.processor.application.factories;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.KafkaEventResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.*;

@Component
public class PolicyResponseFactory {
    public CancelPolicyResponseDTO buildCancelResponse() {
        return CancelPolicyResponseDTO.builder()
                .message(CANCEL_SUCCESS_MESSAGE)
                .status(CANCEL_STATUS)
                .timestamp(Instant.now())
                .build();
    }

    public ReprocessPolicyResponseDTO buildReprocessAccepted() {
        return ReprocessPolicyResponseDTO.builder()
                .message(REPROCESS_ACCEPTED_MESSAGE)
                .status(REPROCESS_STATUS_ACCEPTED)
                .timestamp(Instant.now())
                .build();
    }

    public ReprocessPolicyResponseDTO buildReprocessRejected(String currentStatus) {
        return ReprocessPolicyResponseDTO.builder()
                .message(REPROCESS_REJECTED_PREFIX + currentStatus)
                .status(REPROCESS_STATUS_REJECTED)
                .timestamp(Instant.now())
                .build();
    }

    public KafkaEventResponseDTO buildKafkaSuccess() {
        return KafkaEventResponseDTO.builder()
                .status(EVENT_SENT_MESSAGE)
                .message(EVENT_STATUS)
                .timestamp(Instant.now())
                .build();
    }
}