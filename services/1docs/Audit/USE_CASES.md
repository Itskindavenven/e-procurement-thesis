# Audit Service - Use Cases

## Overview
The Audit Service acts as the central repository for historical records of all significant actions taken within the e-Procurement system. It ensures non-repudiation and provides accountability.

## Actors
*   **System (Internal)**: Services that publish events to be audited.
*   **Administrator**: System user responsible for monitoring system usage.
*   **Auditor**: External or internal role responsible for compliance verification.
*   **Supervisor/Manager**: May view audit logs relevant to their department.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-AUDIT-001 | Log System Action | System | Low |
| UC-AUDIT-002 | View Audit Logs | Admin/Auditor | Medium |
| UC-AUDIT-003 | View Entity History | User/System | Medium |
| UC-AUDIT-004 | Export Audit Data | Admin/Auditor | Medium |

---

## Detailed Use Cases

### UC-AUDIT-001: Log System Action
*   **Description**: The system captures details of an action performed by a user or system process.
*   **Trigger**: A significant state change event (e.g., `procurement.created`, `vendor.approved`) occurs in another service.
*   **Flow**:
    1.  Source service publishes an event to the Event Bus (e.g., Kafka).
    2.  Audit Service consumes the event.
    3.  Audit Service extracts relevant metadata (User ID, Timestamp, Action, Old Value, New Value, IP Address).
    4.  Audit Service persists the record to the database.

### UC-AUDIT-002: View Audit Logs
*   **Description**: An administrator or auditor views a chronological list of actions.
*   **Trigger**: User navigates to the Audit Log dashboard.
*   **Flow**:
    1.  User requests audit logs with optional filters (Date Range, Actor, Action Type, Module).
    2.  System validates User permissions (ROLE_ADMIN, ROLE_AUDITOR).
    3.  System retrieves matching records, paginated.
    4.  System displays the logs.

### UC-AUDIT-003: View Entity History
*   **Description**: View the complete history of changes for a specific entity (e.g., a specific Purchase Order).
*   **Trigger**: User requests history for a specific Entity ID.
*   **Flow**:
    1.  User requests logs for `entityId` and `entityType`.
    2.  System retrieves all records linked to that entity ID.
    3.  System displays the timeline of changes.

### UC-AUDIT-004: Export Audit Data
*   **Description**: Export logs for external analysis or legal compliance.
*   **Trigger**: User initiates export.
*   **Flow**:
    1.  User defines export criteria (Date Range).
    2.  System generates a secure report (CSV/PDF).
    3.  System logs the "Export" action itself (Audit the Auditor).
    4.  System provides the download link.
