package com.felipe.policy.event.processor.infrastructure.persistence.mappers;

import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StatusHistoryPersistenceMapper.class})
public interface InsurancePolicyPersistenceMapper {

    InsurancePolicyRequestEntity toEntity(InsurancePolicyRequest domain);

    InsurancePolicyRequest toDomain(InsurancePolicyRequestEntity entity);
}