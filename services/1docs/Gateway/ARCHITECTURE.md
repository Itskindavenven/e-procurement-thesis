# Gateway Service - Architecture

## High-Level Diagram

```mermaid
graph LR
    Client[Client] -->|HTTPS| Gateway[API Gateway (Spring Cloud)]
    
    subgraph "Internal Network"
        Gateway -->|/api/auth| Auth[Auth Service]
        Gateway -->|/api/procurement| Proc[Procurement Service]
        Gateway -->|/api/vendor| Vend[Vendor Service]
        Gateway -->|/api/audit| Audit[Audit Service]
    end

    Gateway -->|Discovery| Eureka[Service Discovery]
```

## Component Description

### 1. Spring Cloud Gateway
*   **Role**: The single entry point for all external traffic.
*   **Technology**: Spring Cloud Gateway (Reactive/Netty).
*   **Responsibility**:
    *   **Routing**: Forwards requests to appropriate microservices.
    *   **Security Integration**: Validates JWTs (optional, typically offloaded to Auth Service or using a shared Auth Filter).
    *   **Resilience**: Circuit Breaking (Resilience4j) and Timeouts.
    *   **Cross-Cutting Concerns**: CORs configuration, Request Logging.

### 2. Global Filters
*   **Request Logging Filter**: Logs incoming request ID, IP, and method.
*   **Auth Filter**: Checks for `Authorization: Bearer` header presence (detailed validation in services).

## Design Decisions
*   **Reactive Stack**: Uses WebFlux for non-blocking I/O to handle high concurrency.
*   **Stateless**: No session storage on Gateway; purely token-based.
