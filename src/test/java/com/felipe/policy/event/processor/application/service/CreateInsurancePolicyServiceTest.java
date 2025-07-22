package com.felipe.policy.event.processor.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.FraudAnalysisResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.infrastructure.clients.fraud.FraudAnalysisClient;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CreateInsurancePolicyServiceTest {

    @Test
    void policyIsCreatedAndValidatedSuccessfully() {
        InsurancePolicyRequestDTO dto = new InsurancePolicyRequestDTO();
        InsurancePolicyRequest domain = new InsurancePolicyRequest();
        domain.setHistory(new ArrayList<>());
        domain.setInsuredAmount(BigDecimal.valueOf(1000)); // Initialize insuredAmount

        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setHistory(new ArrayList<>());
        entity.setStatus(InsuranceRequestStatus.RECEIVED);

        InsurancePolicyMapper mapper = mock(InsurancePolicyMapper.class);
        InsurancePolicyPersistenceMapper persistenceMapper = mock(InsurancePolicyPersistenceMapper.class);
        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        FraudAnalysisClient fraudAnalysisClient = mock(FraudAnalysisClient.class);
        RiskValidationContext riskValidationContext = mock(RiskValidationContext.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(mapper.toDomain(dto)).thenReturn(domain);
        when(persistenceMapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(fraudAnalysisClient.analyze(domain)).thenReturn(
                new FraudAnalysisResponseDTO(UUID.randomUUID(), UUID.randomUUID(), Instant.now(), RiskClassification.REGULAR, new ArrayList<>())
        );
        when(riskValidationContext.validate(RiskClassification.REGULAR, domain.getCategory(), domain.getInsuredAmount().doubleValue())).thenReturn(true);
        when(mapper.toResponseDTO(any())).thenReturn(new InsurancePolicyResponseDTO());

        CreateInsurancePolicyService service = new CreateInsurancePolicyService(mapper, persistenceMapper, repository, fraudAnalysisClient, riskValidationContext, eventPublisher);
        InsurancePolicyResponseDTO response = service.execute(dto);

        Assertions.assertNotNull(response);
        verify(eventPublisher, times(3)).publish(entity);
    }

    @Test
    void policyIsRejectedDueToHighRisk() {
        InsurancePolicyRequestDTO dto = new InsurancePolicyRequestDTO();
        InsurancePolicyRequest domain = new InsurancePolicyRequest();
        domain.setHistory(new ArrayList<>());
        domain.setInsuredAmount(BigDecimal.valueOf(1000)); // Initialize insuredAmount

        InsurancePolicyRequestEntity entity = new InsurancePolicyRequestEntity();
        entity.setHistory(new ArrayList<>());
        entity.setStatus(InsuranceRequestStatus.RECEIVED);

        InsurancePolicyMapper mapper = mock(InsurancePolicyMapper.class);
        InsurancePolicyPersistenceMapper persistenceMapper = mock(InsurancePolicyPersistenceMapper.class);
        InsurancePolicyRequestRepository repository = mock(InsurancePolicyRequestRepository.class);
        FraudAnalysisClient fraudAnalysisClient = mock(FraudAnalysisClient.class);
        RiskValidationContext riskValidationContext = mock(RiskValidationContext.class);
        InsurancePolicyEventPublisher eventPublisher = mock(InsurancePolicyEventPublisher.class);

        when(mapper.toDomain(dto)).thenReturn(domain);
        when(persistenceMapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(fraudAnalysisClient.analyze(domain)).thenReturn(
                new FraudAnalysisResponseDTO(UUID.randomUUID(), UUID.randomUUID(), Instant.now(), RiskClassification.HIGH_RISK, new ArrayList<>())
        );
        when(riskValidationContext.validate(RiskClassification.HIGH_RISK, domain.getCategory(), domain.getInsuredAmount().doubleValue())).thenReturn(false);
        when(mapper.toResponseDTO(any())).thenReturn(new InsurancePolicyResponseDTO());

        CreateInsurancePolicyService service = new CreateInsurancePolicyService(mapper, persistenceMapper, repository, fraudAnalysisClient, riskValidationContext, eventPublisher);
        InsurancePolicyResponseDTO response = service.execute(dto);

        Assertions.assertNotNull(response);
        verify(eventPublisher, times(2)).publish(entity);
    }
}