package com.tugas_akhir.procurement_service.common.enums;

/**
 * Status of service progress reporting
 */
public enum ServiceProgressStatus {
    /**
     * Progress report submitted by vendor, awaiting review
     */
    SUBMITTED,

    /**
     * Progress report approved by supervisor
     */
    APPROVED,

    /**
     * Progress report rejected, needs revision
     */
    REJECTED,

    /**
     * Progress report revised and resubmitted
     */
    REVISED,

    /**
     * Service completion verified by operator
     */
    VERIFIED
}
