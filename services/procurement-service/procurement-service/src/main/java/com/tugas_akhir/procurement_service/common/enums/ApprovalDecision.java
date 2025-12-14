package com.tugas_akhir.procurement_service.common.enums;

/**
 * Decision made by Supervisor during approval process
 */
public enum ApprovalDecision {
    /**
     * Approve the procurement request - allows PO creation
     */
    APPROVE,

    /**
     * Reject the procurement request - terminates the process
     */
    REJECT,

    /**
     * Return the PR to Operator for revision
     */
    RETURN,

    /**
     * Provide feedback/comments without changing status
     */
    FEEDBACK_ONLY
}
