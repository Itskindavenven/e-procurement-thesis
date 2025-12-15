# Inventory Service - Architecture

## High-Level Diagram

Inventory Service listens to Procurement events to anticipate incoming stock and emits events when stock changes.

```mermaid
graph TD
    Client[Warehouse App] -->|HTTP POST: Receive| Gateway
    Gateway -->|Forward| InvController

    subgraph "External Producers"
        Proc[Procurement Service] -->|Publish: po.approved| Kafka
    end

    subgraph "Inventory Service"
        Listener[Event Consumer]
        ServiceLogic[Inventory Manager]
        Repo[(Inventory DB)]
        
        Kafka -->|Consume| Listener
        Listener -->|Notify Plan| ServiceLogic
        
        InvController -->|Inbound/Outbound| ServiceLogic
        ServiceLogic -->|Update Stock| Repo
        ServiceLogic -->|Publish: stock.updated| Gen[Event Bus]
    end
```

## Data Model

### InventoryItem (Stock)
*   `id`: UUID
*   `sku`: String (Link to Master Data)
*   `locationId`: UUID
*   `quantity`: Integer
*   `reserved`: Integer (Committed but not shipped)
*   `minThreshold`: Integer

### TransactionHistory (Ledger)
*   `id`: UUID
*   `sku`: String
*   `change`: +10 / -5
*   `reason`: `PO_RECEIVABLE` | `ISSUANCE` | `ADJUSTMENT`
*   `referenceId`: PO-123 / DO-456
*   `timestamp`: Timestamp
