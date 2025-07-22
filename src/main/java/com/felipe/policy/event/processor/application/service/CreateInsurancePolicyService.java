package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.felipe.policy.event.processor.application.constants.MessageConstants.LOG_KAFKA_EVENT_PUBLISHED;

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
        InsurancePolicyRequest domain = mapper.toDomain(dto);
        domain.setCreatedAt(Instant.now());
        domain.updateStatus(InsuranceRequestStatus.RECEIVED);

        // Salvar e publicar após RECEIVED
        InsurancePolicyRequestEntity entity = repository.save(persistenceMapper.toEntity(domain));
        publishStatus(entity);

        FraudAnalysisResponseDTO fraudResponse = fraudAnalysisClient.analyze(domain);
        RiskClassification classification = fraudResponse.getClassification();

        if (riskValidationContext.validate(classification, domain.getCategory(), domain.getInsuredAmount().doubleValue())) {
            entity.setStatus(InsuranceRequestStatus.VALIDATED);
            entity.getHistory().add(new StatusHistoryEntity(InsuranceRequestStatus.VALIDATED, Instant.now()));
            entity = repository.save(entity);
            publishStatus(entity);

            entity.setStatus(InsuranceRequestStatus.PENDING);
            entity.getHistory().add(new StatusHistoryEntity(InsuranceRequestStatus.PENDING, Instant.now()));
            entity = repository.save(entity);
            publishStatus(entity);
        } else {
            entity.setStatus(InsuranceRequestStatus.REJECTED);
            entity.setFinishedAt(Instant.now());
            entity.getHistory().add(new StatusHistoryEntity(InsuranceRequestStatus.REJECTED, Instant.now()));
            entity = repository.save(entity);
            publishStatus(entity);
        }

        return mapper.toResponseDTO(persistenceMapper.toDomain(entity));
    }

    private void publishStatus(InsurancePolicyRequestEntity entity) {
        log.info(LOG_KAFKA_EVENT_PUBLISHED, entity.getId(), entity.getStatus());
        eventPublisher.publish(entity);
    }
}