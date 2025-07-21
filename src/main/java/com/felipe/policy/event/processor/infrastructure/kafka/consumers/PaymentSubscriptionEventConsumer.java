package com.felipe.policy.event.processor.infrastructure.kafka.consumers;

import com.felipe.policy.event.processor.application.dto.request.PaymentSubscriptionResultEventDTO;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import com.felipe.policy.event.processor.infrastructure.kafka.producers.InsurancePolicyEventPublisher;
import com.felipe.policy.event.processor.infrastructure.persistence.entities.StatusHistoryEntity;
import com.felipe.policy.event.processor.infrastructure.persistence.repositories.InsurancePolicyRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentSubscriptionEventConsumer {

    private final InsurancePolicyRequestRepository repository;
    private final InsurancePolicyEventPublisher eventPublisher;

    @KafkaListener(topics = "payment-subscription-result", groupId = "insurance-service")
    public void consume(@Payload PaymentSubscriptionResultEventDTO event) {
        log.info("Evento recebido de pagamento/subscrição: {}", event);

        repository.findById(event.getOrderId()).ifPresent(policy -> {
            InsuranceRequestStatus current = policy.getStatus();

            if (current == InsuranceRequestStatus.CANCELLED) {
                log.warn("Apólice {} está cancelada. Ignorando evento.", policy.getId());
                return;
            }

            if (current != InsuranceRequestStatus.PENDING) {
                log.warn("Apólice {} não está em estado PENDING. Ignorando evento.", policy.getId());
                return;
            }

            if (event.getAnalyzedAt().isBefore(policy.getCreatedAt())) {
                log.error("Evento inválido: analyzedAt < createdAt da apólice {}. Ignorando.", policy.getId());
                return;
            }
            
            boolean pagamentoOk = "PAID".equalsIgnoreCase(event.getPaymentStatus());
            boolean subscricaoOk = "AUTHORIZED".equalsIgnoreCase(event.getSubscriptionStatus());

            InsuranceRequestStatus newStatus = (pagamentoOk && subscricaoOk)
                    ? InsuranceRequestStatus.APPROVED
                    : InsuranceRequestStatus.REJECTED;

            policy.setStatus(newStatus);
            policy.getHistory().add(new StatusHistoryEntity(newStatus, Instant.now()));

            repository.save(policy);

            // Publicar evento com o novo status
            eventPublisher.publish(policy);
            log.info("Status da apólice {} atualizado para {} com sucesso.", policy.getId(), newStatus);
        });
    }

}
