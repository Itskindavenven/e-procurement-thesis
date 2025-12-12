# Admin-Procurement Interaction Sequence Diagrams

This document visualizes the specific interaction flows between the **Admin Service** and **Procurement Service** for each distinct use case.

## 1. Use Case: Administrative Correction
**Scenario**: An Administrator needs to manually correct a field (e.g., misclassified category or typo) in an active Procurement Request that is locked for the Operator.

```mermaid
sequenceDiagram
    autonumber
    participant Admin as Admin User
    participant AdminSvc as Admin Service
    participant Kafka
    participant ProcSvc as Procurement Service
    participant ProcDB as Procurement DB

    Note over Admin, ProcDB: Administrative Data Correction Flow

    Admin->>AdminSvc: POST /api/admin/procurement/correction
    Note right of Admin: {procurementId, field: "category", newValue: "IT_ASSETS"}
    
    activate AdminSvc
    AdminSvc->>AdminSvc: Validate Admin Permissions
    AdminSvc->>AdminSvc: Log Correction in Admin DB
    AdminSvc->>Kafka: Publish "procurement.metadata.updated"
    Note right of AdminSvc: Payload: {prId, type: "CORRECTION", changes: {...}}
    AdminSvc-->>Admin: 200 OK (Correction Queued)
    deactivate AdminSvc

    activate Kafka
    Kafka->>ProcSvc: Consume "procurement.metadata.updated"
    deactivate Kafka

    activate ProcSvc
    ProcSvc->>ProcSvc: Validate Event Source
    ProcSvc->>ProcDB: Fetch ProcurementRequest
    ProcSvc->>ProcDB: Apply correction to entity
    ProcSvc->>ProcDB: Save updated entity
    Note right of ProcSvc: Audit log created internally
    deactivate ProcSvc
```

## 2. Use Case: Investigation Flagging
**Scenario**: An Administrator flags a specific Procurement Request as "Suspicious" or "Under Investigation" due to potential fraud or policy violations.

```mermaid
sequenceDiagram
    autonumber
    participant Admin as Admin User
    participant AdminSvc as Admin Service
    participant Kafka
    participant ProcSvc as Procurement Service
    participant AuditSvc as Audit Service

    Note over Admin, AuditSvc: Procurement Investigation Flagging Flow

    Admin->>AdminSvc: POST /api/admin/procurement/flag
    Note right of Admin: {procurementId, reason: "Price anomaly detected"}

    activate AdminSvc
    AdminSvc->>AdminSvc: Create Investigation Record (Status: OPEN)
    AdminSvc->>Kafka: Publish "procurement.investigation.flagged"
    AdminSvc-->>Admin: 200 OK (Flagged)
    deactivate AdminSvc

    par Distribute Flag Event
        Kafka->>ProcSvc: Consume "procurement.investigation.flagged"
        Kafka->>AuditSvc: Consume "procurement.investigation.flagged"
    end

    activate ProcSvc
    ProcSvc->>ProcSvc: Update PR Status (e.g., "UNDER_INVESTIGATION")
    ProcSvc->>ProcSvc: Freeze/Lock PR Modification
    deactivate ProcSvc

    activate AuditSvc
    AuditSvc->>AuditSvc: Log High-Priority Security Alert
    deactivate AuditSvc
```

## 3. Use Case: Master Data Synchronization
**Scenario**: An Administrator adds a new Standard Item to the Master Data catalog. The Procurement Service must become aware of this validity.

```mermaid
sequenceDiagram
    autonumber
    participant Admin as Admin User
    participant AdminSvc as Admin Service
    participant Kafka
    participant ProcSvc as Procurement Service
    participant ProcCache as Procurement Cache/Validation

    Note over Admin, ProcCache: Master Data Propagation Flow

    Admin->>AdminSvc: POST /api/admin/master-data
    Note right of Admin: {sku: "LPT-001", name: "Laptop Pro", price: 1500}

    activate AdminSvc
    AdminSvc->>AdminSvc: Save to Master DB
    AdminSvc->>Kafka: Publish "masterdata.item.created"
    AdminSvc-->>Admin: 201 Created
    deactivate AdminSvc

    activate Kafka
    Kafka->>ProcSvc: Consume "masterdata.item.created"
    deactivate Kafka

    activate ProcSvc
    ProcSvc->>ProcCache: Update Local Item Cache / Validation Table
    Note right of ProcSvc: Ensures Procurement Service can validate<br/>this Item ID in future PRs without<br/>calling Admin Service synchronously
    deactivate ProcSvc
```

## 4. Use Case: User Role & Hierarchy Validation
**Scenario**: An Operator submits a PR. The system must validate they belong to the correct department/hierarchy, which is managed by the Admin Service.

```mermaid
sequenceDiagram
    autonumber
    participant Operator
    participant ProcSvc as Procurement Service
    participant JWT as JWT Token
    participant AdminSvc as Admin Service (Source of Truth)

    Note over Operator, AdminSvc: Implicit Dependency via Token

    Operator->>ProcSvc: POST /api/operator/procurement/requests
    Note right of Operator: Authorization: Bearer <JWT>

    activate ProcSvc
    ProcSvc->>JWT: Extract Roles & Department ID
    Note right of ProcSvc: Admin Service ORIGINALLY defined these<br/>and Auth Service packed them into JWT
    
    ProcSvc->>ProcSvc: Validate Role ("ROLE_OPERATOR")
    
    alt If detailed hierarchy check needed
        ProcSvc->>AdminSvc: GET /api/admin/employees/{id}/hierarchy
        Note right of ProcSvc: Synchronous Feign Client Call (Optional)<br/>Only if JWT data is insufficient
        AdminSvc-->>ProcSvc: Return Supervisor & Dept Details
    end

    ProcSvc->>ProcSvc: Create & Save PR
    ProcSvc-->>Operator: 201 Created
    deactivate ProcSvc
```
