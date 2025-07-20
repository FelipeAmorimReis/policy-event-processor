package com.felipe.policy.event.processor.application.usecases;

import java.util.UUID;

public interface CancelInsurancePolicyUseCase {
    void execute(UUID id);
}