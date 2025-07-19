package com.felipe.policy.event.processor;

import com.felipe.policy.event.processor.domain.entities.InsurancePolicyRequest;
import com.felipe.policy.event.processor.domain.enums.InsuranceCategory;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.InsurancePolicyRequestEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyMapper;
import com.felipe.policy.event.processor.infrastructure.persistence.mappers.InsurancePolicyPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class InsurancePolicyPersistenceMapperTest {

	@Autowired
	private InsurancePolicyPersistenceMapper mapper;

	@Test
	void devePreservarUUIDAoMapear() {
		// Arrange
		UUID uuid = UUID.randomUUID();

		InsurancePolicyRequest domain = InsurancePolicyRequest.builder()
				.id(uuid)
				.customerId(UUID.randomUUID())
				.productId(123L)
				.category(InsuranceCategory.LIFE)
				.salesChannel("online")
				.paymentMethod("credit_card")
				.totalMonthlyPremiumAmount(BigDecimal.valueOf(100.0))
				.insuredAmount(BigDecimal.valueOf(10000.0))
				.coverages(Map.of("death", BigDecimal.valueOf(5000)))
				.assistances(List.of("funeral"))
				.createdAt(Instant.now())
				.status(InsuranceRequestStatus.RECEIVED)
				.history(new ArrayList<>())
				.build();

		// Act
		InsurancePolicyRequestEntity entity = mapper.toEntity(domain);

		// Assert
		assertEquals(uuid, entity.getId(), "UUID deve ser preservado ao mapear para a entidade");
	}
}
