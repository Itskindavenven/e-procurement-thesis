# Procurement Service Architecture

## 1. Overview
The **Procurement Service** is the core business engine of the e-Procurement system. It handles the complete lifecycle of procurement, from the initial request (PR) by operators, through supervisor approval workflows, to the creation of Purchase Orders (PO), and finally the receiving of goods/services.

## 2. Technical Stack
*   **Framework**: Spring Boot 3.x
*   **Language**: Java 21
*   **Database**: PostgreSQL (Operational Data)
*   **Messaging**: Kafka (Event Driven Architecture)
*   **Documentation**: OpenAPI (Swagger)

## 3. Package Structure
The service is structured by **Domain-Driven Design (DDD)** principles, grouped by functional domains:

```
com.tugas_akhir.procurement_service
├── common          # Shared enums, utilities, and base classes
├── config          # Application configuration
├── domain
│   ├── additionaldocument  # Attachments management
│   ├── audit               # Approval workflows and Audit trails
│   ├── dashboard           # Analytics for Operators and Supervisors
│   ├── delivery            # Delivery tracking
│   ├── inventory           # Stock integration
│   ├── procurementorder    # PO generation and management
│   ├── procurementrequest  # PR creation and lifecycle (Main Domain)
│   ├── receiving           # Goods Receiving notes (GRN)
│   ├── reporting           # Reporting service
│   ├── serviceProgress     # Tracking service-based procurement
│   └── termin              # Payment terms
├── event           # Kafka Producers and Consumers
└── integration     # Feign Clients for external services
```

## 4. Key Domains

### A. Procurement Request (PR)
*   **Role**: Manages the creation and editing of requests.
*   **Flow**: Draft -> Submitted -> Approved/Rejected.
*   **Actors**: Operators (Create), Supervisors (Approve).
*   **Entities**: `ProcurementRequest`, `ProcurementItem`.

### B. Approval Workflow (Audit Domain)
*   **Role**: Handles the decision-making process.
*   **Features**:
    *   **Approve**: Moves PR to "APPROVED" and triggers PO creation.
    *   **Reject**: Terminates the PR.
    *   **Return**: Sends PR back to Operator for revision.
    *   **Feedback**: Adds comments without changing status.

### C. Receiving
*   **Role**: Closes the loop on physical goods.
*   **Flow**: Validates incoming goods against the PO.

### D. Dashboards
*   **Role**: Provides aggregated data for UI.
*   **Operator**: View own requests, pending actions.
*   **Supervisor**: View team's requests, pending approvals.

## 5. Integration Patterns
*   **Kafka Consumer**: Listens for Admin corrections (`procurement.metadata.updated`) and investigation flags.
*   **Kafka Producer**: Publishes events for status changes (e.g., `PR_APPROVED`, `PO_CREATED`) to trigger notifications and audit logs.
*   **Feign Clients**: (Likely used) to fetch User details from Auth/Admin services if not effectively cached.
