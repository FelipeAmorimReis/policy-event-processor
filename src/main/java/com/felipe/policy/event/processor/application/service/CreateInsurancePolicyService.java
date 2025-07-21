package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.entities.StatusHistory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateInsurancePolicyService implements CreateInsurancePolicyUseCase {

    private final InsurancePolicyMapper mapper;
    private final InsurancePolicyPersistenceMapper persistenceMapper;
    private final InsurancePolicyRequestRepository repository;
    private final FraudAnalysisClient fraudAnalysisClient;
    private final RiskValidationContext riskValidationContext;
    private final InsurancePolicyEventPublisher eventPublisher;

    @Override
    public InsurancePolicyResponseDTO execute(InsurancePolicyRequestDTO dto) {
        // 1. Mapea DTO → Domínio
        InsurancePolicyRequest domain = mapper.toDomain(dto);
        Instant now = Instant.now();

        domain.setCreatedAt(now);
        updateStatus(domain, InsuranceRequestStatus.RECEIVED, now);

        // 2. Analisa risco (via mock API)
        FraudAnalysisResponseDTO fraudResponse = fraudAnalysisClient.analyze(domain);
        RiskClassification classification = fraudResponse.getClassification();

        // 3. Validar risco com strategy
        boolean isValid = riskValidationContext.validate(
                classification,
                domain.getCategory(),
                domain.getInsuredAmount().doubleValue()
        );

        if (isValid) {
            updateStatus(domain, InsuranceRequestStatus.VALIDATED);
            updateStatus(domain, InsuranceRequestStatus.PENDING);
        } else {
            updateStatus(domain, InsuranceRequestStatus.REJECTED);
        }

        // 4. Persiste dados
        InsurancePolicyRequestEntity entity = persistenceMapper.toEntity(domain);
        InsurancePolicyRequestEntity savedEntity = repository.save(entity);

        // 5. Publica evento (Kafka)
        eventPublisher.publish(savedEntity);

        // 6. Mapea retorno
        return mapper.toResponseDTO(persistenceMapper.toDomain(savedEntity));
    }

    private void updateStatus(InsurancePolicyRequest domain, InsuranceRequestStatus newStatus) {
        updateStatus(domain, newStatus, Instant.now());
    }

    private void updateStatus(InsurancePolicyRequest domain, InsuranceRequestStatus newStatus, Instant timestamp) {
        if (domain.getHistory() == null) {
            domain.setHistory(new ArrayList<>());
        }
        domain.setStatus(newStatus);
        domain.getHistory().add(new StatusHistory(newStatus, timestamp));
    }
}