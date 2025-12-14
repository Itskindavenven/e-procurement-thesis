package com.tugas_akhir.procurement_service.common.enums;

/**
 * Status of service termin (milestone/payment installment)
 */
public enum TerminStatus {
    /**
     * Termin submitted by vendor, awaiting operator review
     */
    SUBMITTED_BY_VENDOR,

    /**
     * Operator requests clarification from vendor
     */
    CLARIFICATION_REQUESTED,

    /**
     * Operator requests revision from vendor
     */
    REVISION_REQUESTED,

    /**
     * Operator reviewed and accepted termin, awaiting supervisor approval
     */
    REVIEWED_BY_OPERATOR,

    /**
     * Termin has been reviewed (general status)
     */
    REVIEWED,

    /**
     * Supervisor approved the termin for payment processing
     */
    APPROVED_BY_SUPERVISOR,

    /**
     * Termin rejected by supervisor
     */
    REJECTED,

    /**
     * Termin has been revised
     */
    REVISED
}
