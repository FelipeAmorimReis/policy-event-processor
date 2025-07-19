package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.exception.BusinessException;
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
import java.util.List;

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
        // 1. Mapear DTO -> Domínio
        InsurancePolicyRequest domain = mapper.toDomain(dto);

        domain.setCreatedAt(Instant.now());
        domain.setStatus(InsuranceRequestStatus.RECEIVED);
        domain.setHistory(List.of(new StatusHistory(domain.getStatus(), domain.getCreatedAt())));

        // 2. Chamada à API de fraude (mock)
        FraudAnalysisResponseDTO fraudResponse = fraudAnalysisClient.analyze(domain);
        RiskClassification classification = fraudResponse.getClassification();

        // 3. Validação de risco com contexto de estratégia
        boolean isValid = riskValidationContext.validate(
                classification,
                domain.getCategory(),
                domain.getInsuredAmount().doubleValue()
        );

        if (!isValid) {
            log.warn("Solicitação com risco inválido: {}", classification);
            throw new BusinessException("Risk validation failed");
        }

        // 4. Persistir (Domínio -> Entidade)
        InsurancePolicyRequestEntity entity = persistenceMapper.toEntity(domain);
        InsurancePolicyRequestEntity savedEntity = repository.save(entity);

        // 5. Publicar evento
        log.info("Simulando envio de evento para Kafka: {}", savedEntity);
        // eventPublisher.publish(savedEntity); // Ative quando Kafka estiver ok

        // 6. Retornar resposta (Entidade → Domínio → DTO de resposta)
        return mapper.toResponseDTO(persistenceMapper.toDomain(savedEntity));
    }
}