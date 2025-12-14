package com.tugas_akhir.procurement_service.common.enums;

/**
 * Status of individual PO (Purchase Order) items
 */
public enum POItemStatus {
    /**
     * Item is pending delivery from vendor
     */
    PENDING,

    /**
     * Item is partially received
     */
    PARTIALLY_RECEIVED,

    /**
     * Item is fully received
     */
    FULLY_RECEIVED,

    /**
     * Item delivery is cancelled
     */
    CANCELLED
}
