# Procurement Service Analysis

This document provides a deep dive into the business logic, workflows, and logical boundaries of the `procurement-service`.

## 1. Core Responsibilities
The `procurement-service` is the operational heart of the system. It is responsible for:
*   **Request Management**: Handling the state machine of a Procurement Request (PR).
*   **Workflow Enforcement**: Ensuring requests pass through correct approval gates.
*   **Stock Integration**: Linking procurement with inventory.
*   **Receiving**: Validating that what was ordered is what arrived.

## 2. Domain breakdown

### A. Procurement Request Domain
**Key Logic**:
*   **Draft State**: Allows partial saves. No validation required until submission.
*   **Submission**: Locks the request and triggers the first level of approval.
*   **Cost Calculation**: Aggregates item costs. *Gap*: Tax calculation logic needs verification.

### B. Audit & Approval Domain
**Key Logic**:
*   **Role-Based Access**: Supervisors can only see requests routed to them.
*   **Decision Recording**: Every action (Approve/Reject) requires a timestamp and actor ID.
*   **PO Generation**: Approval is not just a status change; it triggers the creation of a `ProcurementOrder` entity. This is a critical transition point.

### C. Receiving Domain
**Key Logic**:
*   **3-Way Match**: The system supports the logical start of a 3-way match (PO vs. Delivery Note). The Invoice matching likely lives in `payment-service`.
*   **Status Updates**: Receiving goods automatically updates the PO status to `RECEIVED` or `PARTIALLY_RECEIVED`.

## 3. Critical Interactions

### Internal (In-Memory/Database)
*   **PR to PO**: A 1:1 or 1:N relationship. Currently implemented as a direct transformation service.
*   **PO to GRN**: Goods Receiving Notes link back to PO Line Items.

### External (Event/API)
*   **Admin Service**:
    *   *Input*: Standardized Item IDs, User Roles.
    *   *Output*: None (Admin reads from Procurement DB via API or events).
*   **Inventory Service**:
    *   *Output*: `GOODS_RECEIVED` event triggers stock increase in Inventory.
*   **Notification Service**:
    *   *Output*: `PR_SUBMITTED`, `PR_APPROVED`, `PR_REJECTED` events trigger emails.

## 4. Identified Gaps & Observations
1.  **Budget Checking**: There is no explicit "Budget Service" check mentioned in the controllers. Approvals seem to be purely human-decision based without automated budget caps.
2.  **Vendor Portal Integration**: While we capture `vendorId`, the direct link for the Vendor to *acknowledge* the PO is simplistic or handled via email (Notification Service).
3.  **Complex Approvals**: The current controller supports a single "Supervisor". Multi-level approval (e.g., Manager -> Director > CFO) based on value is not clearly visible in the top-level API.
