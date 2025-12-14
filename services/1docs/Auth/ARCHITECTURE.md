# Auth Service Architecture

## 1. Overview
The **Auth Service** is the central component for Identity and Access Management (IAM) in the e-Procurement ecosystem. It handles user authentication, token generation, and secure user management. It uses **Spring Security** with **OAuth2/JWT** standards.

## 2. Technical Stack
*   **Framework**: Spring Boot 3.x
*   **Language**: Java 21
*   **Security**: Spring Security, JJWT (Java JWT)
*   **Database**: PostgreSQL (User/Role storage)
*   **Messaging**: Kafka (Syncing user events to `admin-service`, Audit logs)
*   **Caching**: Redis (Token Blacklist, Rate Limiting - implied by `TokenService` references)

## 3. Package Structure
The code is structured by **layer** and **feature** mix:

```
com.tugas_akhir.auth_service
├── config          # Security and App configuration
├── controller      # Auth, Admin, Vendor endpoints
├── domain          # User, Role, PasswordReset Request entities
├── dto             # Request/Response Data Transfer Objects
├── integration     # Kafka Publishers (Audit, MasterData, Notification)
├── repository      # JPA Repositories
├── security        # JWT Utilities and Filters
└── service         # Core business logic (AuthService, AdminService, VendorService)
```

## 4. Key Components

### A. Authentication Engine (`AuthService`)
*   **Login**: Validates credentials via `AuthenticationManager`.
*   **Token Issue**: Generates Access (Short-lived) and Refresh (Long-lived) tokens using `JwtUtil`.
*   **Token Rotation**: Revokes old Refresh Tokens upon use.
*   **Blacklisting**: Logout requests blacklist the Access Token in Redis to prevent reuse.

### B. User Management (`AdminService`)
*   **Role Management**: Manages User-Role assignments (e.g., OPERATOR, SUPERVISOR).
*   **Lifecycle**: Handles creation, updates, and soft-delete (deactivation) of users.
*   **Synchronization**: Publishes events (`USER_CREATED`, `USER_UPDATED`) to Kafka so the `admin-service` and others can maintain a local mapping of User IDs to Names/Employees.

### C. Security integration
*   **Request Filter**: Intercepts HTTP requests to validate the `Authorization: Bearer` header.
*   **Roles**: Authorities are extracted from the JWT `roles` claim and mapped to Spring Security's `GrantedAuthority`.

## 5. Event Driven Architecture
The Auth Service is primarily a **Producer** of events:
*   **Topic**: `audit.events` (Login, Logout, Reset Password)
*   **Topic**: `masterdata.user.sync` (User Created/Updated/Deactivated)
