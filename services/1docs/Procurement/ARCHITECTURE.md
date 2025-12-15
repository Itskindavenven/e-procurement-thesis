# Procurement Service Architecture

## 1. Data Entities

### 1.1 Core Entities (Table 4.4.5.1)

| Entity | Description | Fields |
| :--- | :--- | :--- |
| **ProcurementRequest** | Stores core information of a procurement request submitted by an Operator. | `idRequest`, `idOperator`, `idVendor`, `description`, `priority` (normal/urgent/critical), `status` (draft/submitted/approved/rejected/returned/expired), `deadline_date`, `timestamp` |
| **ProcurementItem** | List of items selected by Operator from the chosen Vendor's catalog. | `idItem`, `idRequest`, `idCatalogVendor`, `item_name`, `quantity`, `unit_price`, `vat` (ppn), `subtotal` |
| **DeliveryDetail** | Information regarding the delivery of goods or services. | `idDelivery`, `idRequest`, `delivery_address`, `operator_location`, `planned_delivery_date`, `delivery_notes` |
| **AdditionalDocument** | Supporting documents uploaded by the Operator during the request summary phase. | `idDocument`, `idRequest`, `fileName`, `fileType`, `fileSize`, `fileUrl` |
| **ApprovalRecord** | Log of decisions made by Supervisors or Admins regarding a request. | `idApproval`, `idRequest`, `idSupervisor`, `decision` (approve/reject/return), `feedback_notes`, `timestamp` |
| **ProcurementOrder** | The Purchase Order (PO) document generated after a PR is approved. | `idPO`, `idRequest`, `po_number`, `po_status`, `po_date` |

### 1.2 Extension Entities (Service Procurement)
*Inferred from Service Use Cases*

| Entity | Description | Fields |
| :--- | :--- | :--- |
| **ServiceScope** | Details of the service scope selected from the vendor. | `idScope`, `idRequest`, `scope_description`, `technical_specifications` |
| **ServiceTermin** | Payment terms and phases for service jobs. | `idTermin`, `idRequest`, `term_name` (e.g. DP), `percentage`, `amount`, `status` (Submitted/Reviewed/Completed) |
| **ServiceProgress** | Tracking of service execution. | `idProgress`, `idTermin`, `status` (Waiting/Completed/Verified), `report_url`, `confirmation_notes` |

## 2. High-Level Architecture

### 2.1 Service Interaction
*   **Vendor Service**: Source of Vendor Data, Catalogs, and Service Scopes.
*   **Inventory Service**: Updated upon "Goods Receiving" (Stock Increase) and "Service Confirmation" (if applicable).
*   **Finance Service**: Receives Budget Top-Up requests and Payment triggers upon "Service Confirmation" or "PO Acceptance".
*   **Notification Service**: Handles alerts to Operators (Status changes) and Supervisors (Approvals needed, Deadlines).
*   **Audit Service**: Logs all significant actions (Create, Submit, Approve, Reject, Receive).

### 2.2 Event-Driven Flows
*   **PR Submitted** -> Event published -> Notification to Supervisor.
*   **PR Approved** -> Event published -> `ProcurementOrder` created.
*   **PO Accepted (Goods)** -> Event published -> Inventory Stock updated.
*   **Service Verified** -> Event published -> Finance pending payment.
