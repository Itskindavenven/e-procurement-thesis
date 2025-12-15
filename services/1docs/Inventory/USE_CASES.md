# Inventory Service - Use Cases

## Overview
The Inventory Service tracks current stock levels, goods receipts, and goods issuance. It uses EDA to automatically update stock based on Procurement and Vendor events.

## Actors
*   **Warehouse Staff**: Receives goods.
*   **System**: Updates stock.
*   **User**: Checks availability.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-INV-001 | Receive Goods (Inbound) | Wh Staff | Medium |
| UC-INV-002 | Issue Goods (Outbound) | Wh Staff | Medium |
| UC-INV-003 | Check Stock Level | User | Low |
| UC-INV-004 | Stock Low Alert | System | Low |

---

## Detailed Use Cases

### UC-INV-001: Receive Goods
*   **Trigger**: Procurement Service emits `po.approved` (Planned) OR Vendor delivers goods (Actual).
*   **Flow**:
    1.  Staff scans Delivery Note (Delivery ID).
    2.  Service validates against PO.
    3.  Staff confirms quantity received.
    4.  Service updates `StockLevel` (+Qty).
    5.  Service publishes `inventory.stock.updated`.

### UC-INV-004: Stock Low Alert
*   **Trigger**: Stock level drops below `minThreshold`.
*   **Flow**:
    1.  Service detects threshold breach.
    2.  Service publishes `inventory.low_stock` event.
    3.  Notification Service sends alert to Procurement Manager.
    4.  (Optional) Procurement Service auto-creates Draft Request.
