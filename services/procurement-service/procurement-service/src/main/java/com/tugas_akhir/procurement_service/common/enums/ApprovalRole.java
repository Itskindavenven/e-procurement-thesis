package com.tugas_akhir.procurement_service.common.enums;

/**
 * Role of the approver in the approval process
 */
public enum ApprovalRole {
    /**
     * Supervisor - first level approval
     */
    SUPERVISOR,

    /**
     * Admin - escalated approval (after 48h)
     */
    ADMIN,

    /**
     * Finance - additional approval for budget-related matters
     */
    FINANCE
}
