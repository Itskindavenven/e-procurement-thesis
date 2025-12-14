# Procurement Service Documentation

## 1. Overview & System Summary
The **Procurement Service** is a core microservice within the e-Procurement ecosystem. It manages the entire lifecycle of goods and service procurement, from request creation (PR) to approval, ordering (PO), delivery/execution, and final acceptance. It now includes an embedded **Inventory Module** to handle stock management and mutations internally.

Key capabilities:
*   **Procurement Request (PR)**: Creation, Review, and Approval workflows.
*   **Procurement Order (PO)**: Generation and Vendor management.
*   **Goods Receiving**: Tracking deliveries and updating inventory.
*   **Service & Termin**: Monitoring service progress and termin-based payments.
*   **Inventory Management**: Internal tracking of SKU stock levels and mutations.

## 2. Architecture
The service follows a **Domain-Driven Design (DDD)** architecture, organized by business domains rather than technical layers.

```mermaid
graph TD
    Client[Web/Mobile Client] -->|HTTP| APIGateway
    APIGateway -->|HTTP| PC[Procurement Controller]
    
    subgraph "Procurement Service"
        PC --> PS[Procurement Service]
        PS --> Repo[Postgres Repository]
        
        IS[Inventory Service (Internal)] --> Repo
        
        EventProd[Kafka Producer] --> Kafka{Kafka}
        EventCons[Kafka Consumer] --> Kafka
    end
    
    subgraph "External Services"
        VS[Vendor Service]
        NS[Notification Service]
        FS[Finance Service]
        AS[Auth Service]
        
        PS -->|Feign| VS
        PS -->|Feign| NS
        PS -->|Feign| FS
        PS -->|Feign| AS
    end
```

## 3. Responsibilities
*   **Procurement Management**: Handling PR submissions, priority setting, and approvals.
*   **Order Fulfillment**: Managing deliveries for goods and progress updates for services.
*   **Inventory Control**: Auto-updating stock upon receiving and managing manual adjustments.
*   **Audit & Reporting**: Maintaining a full history of actions and generating procurement reports.

## 4. Tech Stack
*   **Language**: Java 22
*   **Framework**: Spring Boot 4.0.0
*   **Database**: PostgreSQL 15+
*   **Messaging**: Apache Kafka 3.x
*   **Caching**: Redis 7+ (Lettuce client)
*   **Containerization**: Docker
*   **Testing**: JUnit 5, Mockito, Testcontainers
*   **API Documentation**: Swagger/OpenAPI 3
*   **Mapping**: MapStruct 1.6.3

## 5. Project Structure
The project is structured by **Domain** (DDD):

```
com.tugas_akhir.procurement_service
├── common/                      # Shared utilities, configs, base classes
│   ├── base/                    # Base entities, DTOs
│   ├── config/                  # Spring configurations
│   ├── enums/                   # Shared enumerations
│   ├── exception/               # Global exception handling
│   └── util/                    # Utility classes
├── domain/
│   ├── procurementrequest/      # PR creation and management
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   └── mapper/
│   ├── approval/                # Approval workflow
│   ├── purchaseorder/           # PO generation
│   ├── receiving/               # Goods receiving
│   ├── serviceprogress/         # Service execution tracking
│   ├── servicetermin/           # Payment milestones
│   ├── inventory/               # Stock management (embedded)
│   ├── dashboard/               # Analytics and reporting
│   ├── audit/                   # Activity logging
│   ├── deliverydetail/          # Delivery information
│   └── additionaldocument/      # Document management
├── client/                      # Feign clients for external services
├── event/                       # Kafka producers/consumers
│   ├── producer/
│   └── consumer/
└── ProcurementServiceApplication.java
```

## 6. Environment Configuration
**File**: `src/main/resources/application.properties`

| Key | Default Value | Description |
| :--- | :--- | :--- |
| `server.port` | `8083` | Service Port |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/procurement_db` | DB Connection |
| `spring.kafka.bootstrap-servers` | `localhost:9092` | Kafka Broker |
| `spring.data.redis.host` | `localhost` | Redis Host |

## 7. API Specification
*Based on OpenAPI/Swagger*

### Procurement Request
*   `POST /api/v1/procurement-requests/draft`: Create a new draft PR.
*   `POST /api/v1/procurement-requests/{id}/submit`: Submit a draft PR for approval.
*   `GET /api/v1/procurement-requests/{id}`: Get PR details.

### Inventory
*   `GET /api/v1/inventory`: List all inventory items.
*   `POST /api/v1/inventory/manual-mutation`: Manually adjust stock (e.g., loss/damage).

## 8. Database Schema
Major entities include:

*   **ProcurementRequest**: `id`, `description`, `status`, `operator_id`, `request_date`.
*   **ProcurementItem**: `id`, `request_id`, `sku`, `qty`, `price`.
*   **InventoryItem**: `id`, `sku`, `qty_hand`, `min_threshold`.
*   **StockMutation**: `id`, `sku`, `qty_change`, `type` (IN/OUT), `reference`.

## 9. Kafka Events

### Produced Events
*   `procurement.submitted`: Triggered when PR is submitted.
*   `procurement.approved`: Triggered when Supervisor approves PR.
*   `inventory.stock.alert`: Triggered when stock falls below threshold.

### Consumed Events
*   `receiving.completed`: Triggers internal inventory stock update.
*   `vendor.termin.status`: Updates internal termin status based on Vendor actions.

## 10. Redis Cache Strategy
*   **Usage**: Caching reference data (e.g., configurations) or user session data if needed.
*   **TTL**: Default 1 hour for standard keys.

## 11. Error Handling
Global Exception Handling via `GlobalExceptionHandler` ensures consistent JSON responses:
```json
{
  "success": false,
  "message": "Error description",
  "errorCode": "PR-404",
  "timestamp": 123456789
}
```

## 12. Logging & Monitoring
*   **Logging**: SLF4J with Logback.
    *   `INFO` for business flows.
    *   `DEBUG` for `com.tugas_akhir.procurementservice` during dev.
*   **Monitoring**: Spring Actuator enabled (`/actuator/health`, `/actuator/prometheus`).

## 13. Security Considerations
*   **Authentication**: Validates JWT tokens passed from API Gateway.
*   **Validation**: Input validation using `jakarta.validation` (@Valid) on DTOs.
*   **Isolation**: Internal inventory logic is protected from direct external access except via defined endpoints.

## 14. Integration Points
*   **Vendor Service**: Validates vendor existence and fetches catalog data.
*   **Notification Service**: Sends email/push notifications for approvals and alerts.
*   **Finance Service**: Notifies when payment terms are ready.

## 15. Deployment Guide
**Local**:
1.  Start Postgres & Kafka (docker-compose).
2.  Run `mvn spring-boot:run`.

**Docker**:
```dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 16. Testing Strategy
*   **Unit Tests**: JUnit 5 + Mockito. Focus on Service logic and Domain constraints.
*   **Integration Tests**: SpringBootTest + Testcontainers (Postgres, Kafka). Focus on Controller -> DB flow and Event processing.

## 17. Troubleshooting
*   **DB Connection Failures**: Check `spring.datasource.url` and ensure Postgres container is healthy.
*   **Kafka Timeouts**: Ensure `spring.kafka.bootstrap-servers` is reachable.
*   **Build Errors**: Run `mvn clean install -U` to force dependency updates.
