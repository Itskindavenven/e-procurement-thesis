# Reporting Service - Architecture

## High-Level Diagram

The Reporting Service implements a **CQRS (Command Query Responsibility Segregation)** pattern. It maintains its own read-optimized database, populated by listening to domain events.

```mermaid
graph TD
    Client[Web Client] -->|HTTP GET: Charts| Gateway
    Gateway -->|Forward| ReportController

    subgraph "Event Sources"
        Proc[Procurement] -->|po.approved| Kafka
        Ven[Vendor] -->|invoice.submitted| Kafka
        Fin[Finance] -->|payment.processed| Kafka
    end

    subgraph "Reporting Service"
        Listener[Event Consumer]
        Aggregator[Data Aggregator]
        ReadDB[(Analytics DB / MongoDB)]
        
        Kafka -->|Consume| Listener
        Listener -->|Process| Aggregator
        Aggregator -->|Update stats| ReadDB
        
        ReportController -->|Query| ReadDB
    end
```

## Data Model (Read Models)

### MonthlySpend
*   `id`: `2025-12`
*   `totalAmount`: Decimal
*   `byCategory`: Map<String, Decimal>
*   `byDepartment`: Map<String, Decimal>

### VendorStats
*   `vendorId`: UUID
*   `totalOrders`: Integer
*   `onTimeDeliveryRate`: Percentage
*   `averageRating`: Float
