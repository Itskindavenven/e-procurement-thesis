# Audit Service - Architecture

## High-Level Diagram

```mermaid
graph TD
    Client[Web/Mobile Client] -->|HTTP GET| Gateway
    Gateway -->|Forward| AuditController

    subgraph "External Services"
        Auth[Auth Service]
        Pro[Procurement Service]
        Ven[Vendor Service]
    end

    Pro -->|Publish Event| Kafka
    Ven -->|Publish Event| Kafka
    Auth -->|Publish Event| Kafka

    subgraph "Audit Service"
        AuditListener[Kafka Consumer] -->|Consume| EventProcessor
        EventProcessor -->|Map| AuditRepo
        AuditController -->|Query| AuditRepo
        AuditRepo[(Audit Database)]
    end

    Kafka[Message Broker (Kafka)] --> AuditListener
```

## Component Description

### 1. Audit Listener (Kafka Consumer)
*   **Role**: Subscribes to relevant topics (e.g., `*.events`, `audit.logs`).
*   **Responsibility**:
    *   Listens for domain events from other microservices.
    *   Filters events that require auditing.
    *   Normalizes diverse event structures into a standard `AuditLog` format.

### 2. Event Processor
*   **Role**: Business logic for auditing.
*   **Responsibility**:
    *   Enriches data if necessary (though ideally, events should be self-contained).
    *   Handles data masking (removing sensitive PII if configured).

### 3. Audit Repository
*   **Role**: Persistence layer.
*   **Technology**: MongoDB (Recommended for flexible JSON structure of "changes") or PostgreSQL (JSONB).
*   **Responsibility**: Efficient storage and retrieval.

### 4. Audit Controller
*   **Role**: REST API for retrieving logs.
*   **Responsibility**:
    *   Implements read-only endpoints.
    *   Enforces strict RBAC (Read-Only access for most, Export for Admins).

## Data Model

### AuditLog Entity
```json
{
  "id": "uuid",
  "timestamp": "ISO-8601",
  "actorId": "user-uuid",
  "actorName": "String",
  "ipAddress": "192.168.1.1",
  "action": "CREATE | UPDATE | DELETE | LOGIN | APPROVE",
  "module": "PROCUREMENT | VENDOR | AUTH",
  "resourceId": "target-entity-uuid",
  "resourceType": "PurchaseOrder",
  "changes": [
    {
      "fieldName": "status",
      "oldValue": "PENDING",
      "newValue": "APPROVED"
    }
  ],
  "metadata": {
    "userAgent": "Mozilla/..."
  }
}
```
