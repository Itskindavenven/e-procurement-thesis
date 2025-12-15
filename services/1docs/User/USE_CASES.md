# User Service - Use Cases

## Overview
The User Service manages the lifecycle of internal employees, external vendors (identities), and roles. It is the source of truth for "Who is who".

## Actors
*   **Admin**: Manages employees and roles.
*   **System**: Syncs user data to other services (Auth, Workflow).

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-USER-001 | Create Employee | Admin | Low |
| UC-USER-002 | Update Profile | User | Low |
| UC-USER-003 | Assign Role | Admin | Medium |
| UC-USER-004 | Sync Vendor User | System | Low |

---

## Detailed Use Cases

### UC-USER-001: Create Employee
*   **Trigger**: HR Onboarding.
*   **Flow**:
    1.  Admin inputs details (Name, Email, Dept).
    2.  Service creates record.
    3.  Service publishes `user.created`.
    4.  Auth Service consumes event to create Login Credentials.

### UC-USER-004: Sync Vendor User
*   **Trigger**: Vendor Service emits `vendor.registered`.
*   **Flow**:
    1.  User Service consumes `vendor.registered`.
    2.  Service delegates to `UserFactory` to create a `VendorUser` entity.
    3.  Service assigns default `ROLE_VENDOR`.
    4.  Service publishes `user.created` (for Auth).
