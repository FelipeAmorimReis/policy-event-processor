package com.felipe.policy.event.processor.application.constants;

public final class MessageConstants {

    // Risk Key Validation
    public static final String RISK_KEY_MINIMAL = "Validation failed: riskKey must be at least 1.";
    public static final String RISK_KEY_MAXIMO = "Validation failed: riskKey must be at most 4.";
    public static final String RISK_KEY_INVALID_PREFIX = "Validation failed: riskKey must be between 1 and 4. Provided: ";

    // Kafka
    public static final String EVENT_SENT_MESSAGE = "Kafka | Event successfully sent to topic.";
    public static final String EVENT_STATUS = "EVENT_SENT";
    public static final String LOG_KAFKA_EVENT_PUBLISHED = "Kafka | Event published for policy ID: {}, new status: {}";

    // Cancelamento
    public static final String CANCEL_SUCCESS_MESSAGE = "Policy cancellation succeeded.";
    public static final String CANCEL_STATUS = "CANCELLED";
    public static final String CANCEL_INVALID_STATUS = "Cancellation not allowed: policy is already APPROVED or REJECTED.";
    public static final String POLICY_NOT_FOUND_WITH_ID = "Cancel | No policy found with ID: {}";

    // Reprocessamento
    public static final String REPROCESS_ACCEPTED_MESSAGE = "Policy successfully sent for reprocessing.";
    public static final String REPROCESS_REJECTED_PREFIX = "Reprocessing denied. Policy is in invalid state: ";
    public static final String REPROCESS_STATUS_ACCEPTED = "REPROCESSING_ACCEPTED";
    public static final String REPROCESS_STATUS_REJECTED = "REPROCESSING_REJECTED";

    public static final String LOG_REPROCESS_STARTED = "Reprocess | Starting for policy ID: {}";
    public static final String LOG_POLICY_NOT_FOUND = "Reprocess | No policy found with ID: {}";
    public static final String LOG_REPROCESS_SENT = "Reprocess | Policy ID: {} sent for fraud validation.";
    public static final String LOG_REPROCESS_REJECTED = "Reprocess | Denied. Policy ID: {}, Status: {}";
    public static final String LOG_POLICY_STATUS_UPDATED = "Reprocess | Status updated to PENDING. Policy ID: {}";

    // Exception Logs
    public static final String LOG_BUSINESS_EXCEPTION = "Exception | Business rule violated: {}";
    public static final String LOG_ENTITY_NOT_FOUND = "Exception | Entity not found: {}";
    public static final String LOG_UNEXPECTED_ERROR = "Exception | Unexpected error occurred.";

    // InsurancePolicyRequest (domínio)
    public static final String STATUS_CANNOT_BE_NULL = "Domain | New status must not be null.";
    public static final String STATUS_CANNOT_CHANGE_AFTER_FINAL = "Domain | Cannot change status after reaching a final state.";

    // RiskValidationContext / Input Validation
    public static final String INVALID_RISK_CLASSIFICATION_PREFIX = "Validation | Invalid risk classification: ";
    public static final String RISK_CLASSIFICATION_REQUIRED = "Validation | Risk classification must not be null.";
    public static final String INSURANCE_CATEGORY_REQUIRED = "Validation | Insurance category must not be null.";
    public static final String INSURED_AMOUNT_NON_NEGATIVE = "Validation | Insured amount must be non-negative.";

    // FraudAnalysisClientHttp
    public static final String FRAUD_API_URL = "http://wiremock:8080/fraud/analyze";
    public static final String FRAUD_API_400_MESSAGE = "FraudAPI | Returned 400 Bad Request: invalid riskKey.";
    public static final String FRAUD_API_UNEXPECTED_ERROR = "FraudAPI | Unexpected error during fraud analysis.";

    // PaymentSubscriptionEventConsumer
    public static final String LOG_EVENT_RECEIVED = "Kafka | Event received: {}";
    public static final String LOG_POLICY_NOT_FOUND_KAFKA = "Kafka | No policy found for ID: {}";
    public static final String LOG_INVALID_POLICY_STATE = "Kafka | Invalid policy state for ID: {}";
    public static final String LOG_POLICY_CANCELLED = "Kafka | Policy already cancelled. ID: {}";
    public static final String LOG_POLICY_NOT_PENDING = "Kafka | Policy is not in PENDING state. ID: {}";
    public static final String LOG_EVENT_OUTDATED = "Kafka | Discarded event: analyzedAt is before policy createdAt. ID: {}";

    // Payment Status
    public static final String PAID = "PAID";
    public static final String AUTHORIZED = "AUTHORIZED";


}
