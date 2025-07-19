package com.felipe.policy.event.processor.application.service;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.RiskClassification;
import com.felipe.policy.event.processor.domain.services.risk.RiskValidationContext;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateInsurancePolicyService implements CreateInsurancePolicyUseCase {

    private final InsurancePolicyMapper mapper; // DTO ↔ Domain
    private final InsurancePolicyPersistenceMapper persistenceMapper; // Domain ↔ Entity
    private final InsurancePolicyRequestRepository repository;
    private final RiskValidationContext riskValidationContext;

    @Override
    public InsurancePolicyResponseDTO execute(InsurancePolicyRequestDTO dto) {
        // 1. Converte DTO → domínio
        InsurancePolicyRequest domain = mapper.toDomain(dto);

        // 2. Validação de risco
        InsuranceCategory category = domain.getCategory();
        double insuredAmount = domain.getInsuredAmount().doubleValue();
//        RiskClassification classification = /* lógica de classificação de risco */;
//
//        boolean isValid = riskValidationContext.validate(classification, category, insuredAmount);
//        if (!isValid) throw new BusinessException("Risk validation failed");

        // 3. Domínio → entidade
        InsurancePolicyRequestEntity entity = persistenceMapper.toEntity(domain);

        // 4. Salvar
        InsurancePolicyRequestEntity saved = repository.save(entity);

        // 5. Entidade → domínio novamente
        InsurancePolicyRequest savedDomain = persistenceMapper.toDomain(saved);

        // 6. Domínio → DTO de resposta
        return mapper.toResponseDTO(savedDomain);
    }
}