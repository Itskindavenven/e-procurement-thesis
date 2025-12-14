package com.tugas_akhir.procurement_service.common.enums;

/**
 * Status of a Purchase Order
 */
public enum POStatus {
    /**
     * PO has been created from approved PR
     */
    CREATED,

    /**
     * PO is currently in delivery/execution
     */
    IN_DELIVERY,

    /**
     * Goods/services have been delivered
     */
    DELIVERED,

    /**
     * Goods/services have been received and accepted by operator
     */
    ACCEPTED,

    /**
     * PO is fully completed and closed
     */
    COMPLETED,

    /**
     * PO has been cancelled
     */
    CANCELLED
}
