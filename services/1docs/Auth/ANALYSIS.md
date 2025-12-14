# Auth Service Analysis

This document outlines the operational capabilities (Use Cases) found within the `auth-service`, distinguishing between public Authentication flows and administrative User Management functions.

## 1. Authentication Use Cases
**Controller:** `AuthController` (`/api/v1/auth`)

These are public or semi-public endpoints used by all users (Operators, Supervisors, Admins, Vendors) to gain access to the system.

*   **Login**: Authenticate with username/password to receive Access and Refresh tokens.
*   **Refresh Token**: Exchange a valid Refresh Token for a new Access Token.
*   **Logout**: Invalidate the current session (Access and Refresh tokens).
*   **Password Reset Request**: Initiate the reset flow via email (generates token).
*   **Password Reset Confirm**: Complete the reset flow using the token and new password.

**Service Logic**: `AuthService` handles credential validation, JWT generation (`JwtUtil`), and token lifecycle (`TokenService`).

## 2. Admin Use Cases (User Management)
**Controller:** `AdminController` (`/api/v1/admin/users`)

These endpoints are protected and restricted to users with the `ADMIN` role. They are used to manage the internal user base of the e-Procurement system.

*   **View All Users**: Retrieve a paginated list of users.
*   **Create User**: Register a new internal user (e.g., Operator, Supervisor) and assign roles.
*   **Update User**: Modify details (Email, Roles) of an existing user.
*   **Deactivate User**: Soft-delete/Ban a user to prevent login.
*   **Reactivate User**: Restore a deactivated user's access.

**Service Logic**: `AdminService` manages `UserRepository` and `RoleRepository`, publishes audit logs (`AuditPublisher`), and syncs user changes to other services via Kafka (`MasterDataPublisher`).

## 3. Vendor Use Cases
**Controller:** `VendorController` (`/api/v1/vendor`)

Specific flow for external vendor onboarding.

*   **Register Vendor**: Submit company details and documents for approval.

## 4. Architecture Note
*   The **Auth Service** acts as the centralized identity provider.
*   **Admins** in this context (via `AdminController`) are managing **System Access**.
*   **Admins** in `admin-service` are managing **Business Rules** (Master Data, Approval Hierarchies, Corrections).
