package com.felipe.policy.event.processor.infrastructure.persistence.entities;

import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InsuranceRequestStatus status;

    private Instant timestamp;
}