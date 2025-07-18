package com.felipe.policy.event.processor.infrastructure.persistence.repositories;

import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistoryEntity, Long> {
}