package com.felipe.policy.event.processor.domain.enums;

public enum InsuranceRequestStatus {
    RECEIVED,
    VALIDATED,
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,

    /** Criando além do escopo, pois o estado ISSUED, embora não listado,
        é citado na regra de negócio como impeditivo para cancelamento. */
    ISSUED
}
