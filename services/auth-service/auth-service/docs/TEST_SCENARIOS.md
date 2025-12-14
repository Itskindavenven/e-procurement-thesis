# Auth Service Test Scenarios & Gap Analysis

## Overview
This document outlines the test scenarios for the `auth-service`, covering functional requirements, error handling, and integration points (mocked). It also includes a gap analysis between the requirements and the current implementation.

## Test Scenarios

### 1. Login Use Case

| ID | Scenario | Preconditions | Input | Steps | Expected Results |
|---|---|---|---|---|---|
| SC-001 | **Successful Login** | User exists, Status=ACTIVE | `POST /api/v1/auth/login` <br> `{username: "user", password: "password"}` | 1. Send Login Request | **HTTP 200 OK** <br> Body: `{accessToken: "...", refreshToken: "..."}` <br> **Redis**: `refresh:<token>` exists <br> **Kafka**: `audit.events` receives `LOGIN_SUCCESS` |
| SC-002 | **Login with Missing Fields** | N/A | `POST /api/v1/auth/login` <br> `{username: ""}` | 1. Send Invalid Request | **HTTP 400 Bad Request** <br> Body: `{"error": "Validation Error", "details": {...}}` |
| SC-003 | **Login with Non-Existing Account** | User does not exist | `POST /api/v1/auth/login` <br> `{username: "unknown", password: "pwd"}` | 1. Send Login Request | **HTTP 400 Bad Request** <br> Body: `{"message": "User not found"}` <br> *(Note: Current impl returns 400 for RuntimeException)* |
| SC-004 | **Login with Wrong Password** | User exists | `POST /api/v1/auth/login` <br> `{username: "user", password: "wrong"}` | 1. Send Login Request | **HTTP 401 Unauthorized** or **400 Bad Request** <br> *(Depends on Exception Handler for BadCredentialsException)* |
| SC-005 | **Login with Inactive Account** | User exists, Status=INACTIVE | `POST /api/v1/auth/login` <br> `{username: "inactive", password: "pwd"}` | 1. Send Login Request | **HTTP 400 Bad Request** <br> Body: `{"message": "User account is not active"}` |

### 2. Reset Password Use Case

| ID | Scenario | Preconditions | Input | Steps | Expected Results |
|---|---|---|---|---|---|
| SC-006 | **Request Reset Token** | User exists | `POST /api/v1/auth/reset/request` <br> `{email: "user@example.com"}` | 1. Send Request | **HTTP 200 OK** <br> **DB**: `password_reset_request` created <br> **Kafka**: `audit.events` -> `PASSWORD_RESET_REQUESTED` <br> **Email**: Mock called with token |
| SC-007 | **Request Reset - Email Not Found** | User does not exist | `POST /api/v1/auth/reset/request` <br> `{email: "unknown@example.com"}` | 1. Send Request | **HTTP 400 Bad Request** <br> Body: `{"message": "User not found"}` |
| SC-008 | **Confirm Reset - Success** | Valid token in DB | `POST /api/v1/auth/reset/confirm` <br> `{token: "valid-token", newPassword: "new", newPasswordConfirm: "new"}` | 1. Send Confirm Request | **HTTP 200 OK** <br> **DB**: User password hash updated, Token marked used <br> **Redis**: All user tokens revoked <br> **Kafka**: `audit.events` -> `PASSWORD_RESET_SUCCESS` |
| SC-009 | **Confirm Reset - Mismatch Passwords** | Valid token | `POST /api/v1/auth/reset/confirm` <br> `{token: "valid", newPassword: "a", newPasswordConfirm: "b"}` | 1. Send Confirm Request | **HTTP 400 Bad Request** <br> Body: `{"message": "Passwords do not match"}` |
| SC-010 | **Confirm Reset - Invalid/Expired Token** | Token expired or used | `POST /api/v1/auth/reset/confirm` <br> `{token: "expired", ...}` | 1. Send Confirm Request | **HTTP 400 Bad Request** <br> Body: `{"message": "Token expired or already used"}` |

### 3. Logout Use Case

| ID | Scenario | Preconditions | Input | Steps | Expected Results |
|---|---|---|---|---|---|
| SC-011 | **Successful Logout** | Valid Access & Refresh Token | `POST /api/v1/auth/logout` <br> Header: `Bearer <access>` <br> Body: `{refreshToken: "<refresh>"}` | 1. Send Logout Request | **HTTP 200 OK** <br> **Redis**: `blacklist:jwt:<jti>` created <br> **Redis**: `refresh:<token>` deleted <br> **Kafka**: `audit.events` -> `LOGOUT` |

### 4. Vendor Registration Use Case

| ID | Scenario | Preconditions | Input | Steps | Expected Results |
|---|---|---|---|---|---|
| SC-012 | **Vendor Registration Success** | Email unique | `POST /api/v1/vendor/register` <br> Multipart: `companyName`, `email`, `documents` | 1. Send Register Request | **HTTP 201 Created** <br> **DB**: User created (PENDING), VendorProfile created <br> **Kafka**: `audit.events` -> `VENDOR_REGISTRATION_PENDING` <br> **Kafka**: `user.events` -> `USER_CREATED` |
| SC-013 | **Vendor Registration - Email Exists** | Email already used | `POST /api/v1/vendor/register` <br> `{email: "existing@example.com"}` | 1. Send Register Request | **HTTP 400 Bad Request** <br> Body: `{"message": "Email already registered"}` |

### 5. Admin User Management Use Case

| ID | Scenario | Preconditions | Input | Steps | Expected Results |
|---|---|---|---|---|---|
| SC-014 | **Create User** | Admin Token | `POST /api/v1/admin/users` <br> `{username: "new", email: "new@ex.com", roles: ["ROLE_USER"]}` | 1. Send Create Request | **HTTP 201 Created** <br> **DB**: User created (ACTIVE) <br> **Kafka**: `audit.events` -> `USER_ACCOUNT_CREATED` <br> **Kafka**: `user.events` -> `USER_CREATED` |
| SC-015 | **Update User** | Admin Token | `PUT /api/v1/admin/users/{id}` <br> `{email: "updated@ex.com"}` | 1. Send Update Request | **HTTP 200 OK** <br> **DB**: User updated <br> **Kafka**: `audit.events` -> `USER_ACCOUNT_UPDATED` <br> **Kafka**: `user.events` -> `USER_UPDATED` |
| SC-016 | **Deactivate User** | Admin Token | `POST /api/v1/admin/users/{id}/deactivate` | 1. Send Deactivate Request | **HTTP 200 OK** <br> **DB**: User status -> INACTIVE <br> **Redis**: All user tokens revoked <br> **Kafka**: `audit.events` -> `USER_ACCOUNT_DEACTIVATED` |

---

## Gap Analysis

| Requirement | Expected Behavior | Actual Implementation | Status | Recommendation |
|---|---|---|---|---|
| **Error Handling** | Standardized JSON `ErrorResponse` with specific error codes. | `GlobalExceptionHandler` returns generic Map with `status`, `error`, `message`. No specific error codes. | **Incomplete** | Update `GlobalExceptionHandler` to use a structured `ErrorResponse` DTO with application-specific error codes. |
| **Login Errors** | Specific errors for "User not found" vs "Bad credentials". | `RuntimeException("User not found")` -> 400 Bad Request. `BadCredentialsException` not explicitly handled (likely 401 or 500). | **Incomplete** | Add specific exception handlers for `BadCredentialsException` (401) and use custom exceptions for Not Found (404). |
| **Admin Self-Update** | Admin should not be able to update their own role/status (E1). | No check in `AdminService.updateUser` or `deactivateUser` to prevent self-modification. | **Missing** | Add logic to check if `id` matches the current authenticated user's ID. |
| **Master Data Sync** | Handle sync failures (E3). | `MasterDataPublisher` retries 3 times then logs error. Transaction commits regardless. | **Ok (Eventual Consistency)** | Acceptable for eventual consistency, but consider adding a "failed_events" table for manual reconciliation if reliability is critical. |
| **Document Upload** | Handle file upload failures. | `VendorService` only stores metadata. No actual file storage logic yet. | **Stubbed** | Acceptable for now as per "Stub/Mock" requirement, but needs implementation integration later. |

## Automated Test Recommendations

### Unit Tests
*   **AuthServiceTest**: Mock `UserRepository`, `TokenService`, `AuthenticationManager`. Verify login flow, token generation, and audit publishing.
*   **VendorServiceTest**: Verify user creation, profile creation, and event publishing.
*   **AdminServiceTest**: Verify CRUD operations and role assignment.
*   **TokenServiceTest**: Use `EmbeddedRedis` to verify TTL, blacklist logic, and rotation.

### Integration Tests
*   **AuthControllerIntegrationTest**: Use `MockMvc` + `Testcontainers` (Postgres, Redis, Kafka).
    *   Test full Login -> Refresh -> Logout flow.
    *   Test Password Reset flow (mock EmailService).
*   **AdminControllerIntegrationTest**: Verify RBAC (ensure non-admins cannot access).

### Mocking Strategy
*   **Kafka**: Use `spring-kafka-test`'s `EmbeddedKafka` or `MockProducer` to verify `audit.events` and `user.events` are published.
*   **Redis**: Use `Testcontainers` generic container for Redis or `EmbeddedRedis` library.
*   **Email**: Mock `NotificationPublisher` bean.
