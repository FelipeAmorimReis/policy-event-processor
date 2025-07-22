package com.felipe.policy.event.processor.domain.entities;

import com.felipe.policy.event.processor.application.exceptions.InvalidPolicyStatusException;
import com.felipe.policy.event.processor.application.exceptions.InvalidRequestException;
import com.felipe.policy.event.processor.domain.enums.InsuranceRequestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class InsurancePolicyRequestTest {

    @Test
    @DisplayName("Should update status successfully when new status is valid")
    void shouldUpdateStatusSuccessfullyForValidNewStatus() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .status(InsuranceRequestStatus.PENDING)
                .build();

        request.updateStatus(InsuranceRequestStatus.APPROVED);

        Assertions.assertEquals(InsuranceRequestStatus.APPROVED, request.getStatus());
        Assertions.assertNotNull(request.getFinishedAt());
        Assertions.assertEquals(1, request.getHistory().size());
    }

    @Test
    @DisplayName("Should throw exception when new status is null")
    void shouldThrowExceptionWhenNewStatusIsNull() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .status(InsuranceRequestStatus.PENDING)
                .build();

        Assertions.assertThrows(InvalidRequestException.class, () -> request.updateStatus(null));
    }

    @Test
    @DisplayName("Should throw exception when trying to update final status")
    void shouldThrowExceptionWhenUpdatingFinalStatus() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .status(InsuranceRequestStatus.APPROVED)
                .build();

        Assertions.assertThrows(InvalidPolicyStatusException.class, () -> request.updateStatus(InsuranceRequestStatus.PENDING));
    }

    @Test
    @DisplayName("Should initialize history if null when updating status")
    void shouldInitializeHistoryIfNullWhenUpdatingStatus() {
        InsurancePolicyRequest request = InsurancePolicyRequest.builder()
                .id(UUID.randomUUID())
                .status(InsuranceRequestStatus.PENDING)
                .history(null)
                .build();

        request.updateStatus(InsuranceRequestStatus.APPROVED);

        Assertions.assertNotNull(request.getHistory());
        Assertions.assertEquals(1, request.getHistory().size());
    }
}