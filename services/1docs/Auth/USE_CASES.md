# Auth Service Use Cases

## 1. Public Authentication
Allow users to securely access the system.

*   **User Login**:
    *   **Goal**: Authenticate and obtain tokens.
    *   **Actor**: Any User (Operator, Admin, Vendor, etc.)
    *   **Input**: Username, Password
    *   **Output**: Access Token (JWT), Refresh Token

*   **Token Refresh**:
    *   **Goal**: Maintain a valid session without re-entering credentials.
    *   **Mechanism**: Exchange valid Refresh Token for new Access Token.

*   **Secure Logout**:
    *   **Goal**: Terminate session immediately.
    *   **Mechanism**: Blacklist current Access Token and revoke Refresh Token.

*   **Password Reset**:
    *   **Goal**: Recover access when password is forgotten.
    *   **Flow**:
        1.  User requests reset via email.
        2.  System emails a token.
        3.  User submits token + new password to confirm.

## 2. Internal User Management (Admin)
Manage the lifecycle of internal staff accounts.

*   **User Onboarding**:
    *   **Goal**: Create accounts for new employees.
    *   **Actor**: Admin
    *   **Details**: Assign username, email, and initial roles (e.g., `ROLE_OPERATOR`).

*   **User Modification**:
    *   **Goal**: Update roles or contact info.
    *   **Example**: Promoting an Operator to Supervisor.

*   **Access Revocation**:
    *   **Goal**: Immediate removal of access for terminated employees.
    *   **Action**: Deactivate user (Soft Delete) and revoke all active tokens.

## 3. Vendor Onboarding
*   **Vendor Registration**:
    *   **Goal**: Allow external suppliers to apply for access.
    *   **Action**: Vendor submits company details (Name, NPWP, Address) and documents.
    *   **Outcome**: Account is created in potential/pending state (to be verified by `admin-service`).
