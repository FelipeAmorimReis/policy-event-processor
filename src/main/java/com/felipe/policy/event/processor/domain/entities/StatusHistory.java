package com.felipe.policy.event.processor.domain.entities;

import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Value;

import java.time.Instant;


@Value // Tornando classe imutavel
public class StatusHistory {

    @Enumerated(EnumType.STRING)
    private InsuranceRequestStatus status;
    private Instant timestamp;

    public StatusHistory(InsuranceRequestStatus status, Instant timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }
}