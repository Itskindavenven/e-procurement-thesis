# Admin Service Architecture

## 1. Overview
The **Admin Service** is a centralized management system built with **Spring Boot 3** and **Java 21**. It serves as the "Source of Truth" for organizational structure, master data, and user roles within the e-Procurement ecosystem. It follows a **Modular Monolith** architecture where features are packaged by domain.

## 2. Technical Stack
*   **Framework**: Spring Boot 3.x
*   **Language**: Java 21
*   **Build Tool**: Maven
*   **Database**: PostgreSQL (presumed from typical stack)
*   **Security**: Spring Security + JWT (OAuth2 Resource Server)
*   **Messaging**: Kafka (for event publishing)

## 3. Package Structure
The application code is organized by **feature** (domain) rather than by layer. Each package contains its own Controller, Service, Repository, DTOs, and Entities.

```
com.tugas_akhir.admin_service
├── audit           # System audit logging
├── calendar        # Working days and monthly cutoffs
├── employee        # Employee and Role management
├── lokasi          # Location/Site management and staff assignment
├── masterdata      # Items, Categories, UOMs
├── notification    # Notification templates and logs
├── procurement     # Administrative oversight for procurement (Corrections, Flags)
├── vendor          # Vendor registration verification and management
└── config          # Global configurations
```

## 4. Layered Design
Within each feature package, the standard 3-layer architecture is enforced:

1.  **Controller Layer** (`*.controller`):
    *   Handles HTTP requests.
    *   Validates input using `Jakarta Validation` (`@Valid`, `@NotNull`).
    *   Enforces security using `@PreAuthorize("hasRole('ADMIN')")`.
    *   Returns `ResponseEntity<DTO>`.

2.  **Service Layer** (`*.service`):
    *   Contains business logic.
    *   Transactional boundaries.
    *   Maps Entities to DTOs.

3.  **Repository Layer** (`*.repository`):
    *   Interfaces with the database using Spring Data JPA.

## 5. Security & Access Control
*   **RBAC**: Access to all endpoints is strictly limited to users with the `ADMIN` role.
*   **Method Level Security**: `@PreAuthorize` annotations are used on every controller method to ensure granular security.

## 6. Integration Patterns
*   **Event-Driven**: The Admin Service acts as a producer for Master Data and Correction events (Kafka) to sync data with the Procurement Service.
*   **Direct API**: Provides REST APIs for frontend administration panels.
