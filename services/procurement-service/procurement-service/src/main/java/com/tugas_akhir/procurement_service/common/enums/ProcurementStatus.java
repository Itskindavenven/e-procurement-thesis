package com.tugas_akhir.procurement_service.common.enums;

/**
 * Status of a procurement request throughout its lifecycle
 */
public enum ProcurementStatus {
    /**
     * PR is saved as draft, not yet submitted
     */
    DRAFT,

    /**
     * PR has been submitted for approval
     */
    SUBMITTED,

    /**
     * PR is currently being reviewed by Supervisor
     */
    IN_REVIEW,

    /**
     * PR has been approved by Supervisor
     */
    APPROVED,

    /**
     * PR has been rejected by Supervisor
     */
    REJECTED,

    /**
     * PR has been returned to Operator for revision
     */
    RETURNED,

    /**
     * PR has been escalated to Admin (after 48h no response)
     */
    ESCALATED,

    /**
     * PR has been cancelled by Operator
     */
    CANCELLED
}
