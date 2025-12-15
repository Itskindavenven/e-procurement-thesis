# User Service - Architecture

## High-Level Diagram

The User Service is the master of identity data, but login credentials are handled by the Auth Service. They sync via Events.

```mermaid
graph TD
    Client[Client App] -->|HTTP| Gateway
    Gateway -->|Forward| UserController

    subgraph "Event Bus (Kafka)"
        Events[Events]
    end

    subgraph "User Service"
        UserController -->|CRUD| UserRepo[(User DB)]
        ServiceLogic -->|Publish: user.created, user.updated| Events
        
        Listener[Vendor Event Consumer] -->|Consume: vendor.registered| ServiceLogic
    end
    
    subgraph "Downstream Consumers"
        Events -->|Consume| Auth[Auth Service (Create Login)]
        Events -->|Consume| Workflow[Workflow Service (Assign Approver)]
    end
```

## Data Model

### User Entity
*   `id`: UUID
*   `email`: String (Unique)
*   `fullName`: String
*   `employeeId`: String (Optional)
*   `departmentId`: UUID
*   `type`: `INTERNAL` | `VENDOR`
*   `status`: `ACTIVE` | `INACTIVE`
*   `createdAt`: Timestamp

### Role Entity
*   `id`: UUID
*   `name`: String (`ROLE_ADMIN`, `ROLE_OPERATOR`)
*   `permissions`: String[] (`READ_PO`, `APPROVE_PO`)
