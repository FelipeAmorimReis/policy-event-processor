package com.felipe.policy.event.processor.domain.entities;

import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import lombok.Value;

import java.time.Instant;


@Value // Tornando classe imutavel
public class StatusHistory {

    private InsuranceRequestStatus status;
    private Instant timestamp;

    public StatusHistory(InsuranceRequestStatus status, Instant timestamp) {
        if (status == null || timestamp == null) {
            throw new IllegalArgumentException("Status and timestamp must not be null.");
        }
        this.status = status;
        this.timestamp = timestamp;
    }
}