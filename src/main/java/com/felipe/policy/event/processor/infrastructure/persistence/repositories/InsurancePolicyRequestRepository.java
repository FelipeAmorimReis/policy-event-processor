package com.felipe.policy.event.processor.infrastructure.persistence.repositories;

import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsurancePolicyRequestRepository extends JpaRepository<InsurancePolicyRequestEntity, UUID> {
    List<InsurancePolicyRequestEntity> findByCustomerId(UUID customerId);
}