# Test Scenarios

This document outlines the test scenarios for verifying the Admin Service functionality. These scenarios cover happy paths, negative cases, and integration flows.

## 1. Security

### Scenario 1.1: Invalid JWT
-   **Action**: Request any protected endpoint with an invalid JWT signature.
-   **Expected Result**: HTTP 401 Unauthorized.

### Scenario 1.2: Expired JWT
-   **Action**: Request any protected endpoint with an expired JWT.
-   **Expected Result**: HTTP 401 Unauthorized.

### Scenario 1.3: Insufficient Role
-   **Action**: Request an ADMIN-only endpoint (e.g., `POST /api/admin/employees`) with a user having only `OPERATOR` role.
-   **Expected Result**: HTTP 403 Forbidden.

## 2. Employee Management

### Scenario 2.1: Create New Employee (Success)
-   **Precondition**: Admin is logged in. Email `new.user@example.com` does not exist.
-   **Action**: `POST /api/admin/employees` with valid payload (Name, Email, Role).
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Response contains created Employee ID.
    -   Database: New record in `employees` table.
    -   Kafka: `employee.created` event published.

### Scenario 2.2: Create Duplicate Employee (Fail)
-   **Precondition**: Employee with email `existing@example.com` exists.
-   **Action**: `POST /api/admin/employees` with email `existing@example.com`.
-   **Expected Result**:
    -   HTTP 400 Bad Request.
    -   Error message: "Email already exists".

### Scenario 2.3: Assign Role
-   **Action**: `PUT /api/admin/employees/{id}/role` with new Role ID.
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Employee record updated with new Role.
    -   Kafka: `employee.updated` event published.

## 3. Vendor Management (Verification)

### Scenario 3.1: Approve Vendor
-   **Precondition**: Vendor registration exists with status `PENDING`.
-   **Action**: `POST /api/admin/vendors/{id}/verify` with status `APPROVED`.
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Vendor status updates to `APPROVED`.
    -   Kafka: `vendor.approved` event published.

### Scenario 3.2: Reject Vendor with Notes
-   **Precondition**: Vendor registration exists with status `PENDING`.
-   **Action**: `POST /api/admin/vendors/{id}/verify` with status `REJECTED` and notes "Invalid docs".
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Vendor status updates to `REJECTED`.
    -   Admin notes saved.
    -   Kafka: `vendor.rejected` event published.

## 4. Location Management

### Scenario 4.1: Create Location
-   **Action**: `POST /api/admin/locations` with valid details.
-   **Expected Result**: HTTP 200 OK, Location created.

### Scenario 4.2: Assign Operator (Success)
-   **Precondition**: Operator is not assigned to any location.
-   **Action**: `POST /api/admin/locations/assign-operator` with Location ID and Operator ID.
-   **Expected Result**: HTTP 200 OK, Assignment created.

### Scenario 4.3: Assign Operator (Conflict)
-   **Precondition**: Operator is already assigned to Location A.
-   **Action**: `POST /api/admin/locations/assign-operator` to Location B.
-   **Expected Result**: HTTP 400 Bad Request ("Operator already assigned").

## 5. Master Data Management

### Scenario 5.1: Create Master Item
-   **Action**: `POST /api/admin/master-data` with Item Code "ITM001".
-   **Expected Result**: HTTP 200 OK, Item created.

### Scenario 5.2: Create Parent-Child Relation
-   **Action**: `POST /api/admin/master-data/relations` with Parent ID and Child ID.
-   **Expected Result**: HTTP 200 OK, Relation established.

### Scenario 5.3: Sync Data
-   **Action**: `POST /api/admin/master-data/sync`.
-   **Expected Result**: HTTP 200 OK, Kafka `sync.requested` event published.

## 6. Procurement Data Management

### Scenario 6.1: Administrative Correction
-   **Action**: `POST /api/admin/procurement/correction` with Procurement ID and correction details.
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Correction logged in `procurement_corrections`.
    -   Kafka: `procurement.metadata.updated` event published.

### Scenario 6.2: Flag for Investigation
-   **Action**: `POST /api/admin/procurement/flag` with reason.
-   **Expected Result**:
    -   HTTP 200 OK.
    -   Flag record created in `audit_investigation_flags`.
    -   Kafka: `procurement.investigation.flagged` event published.

## 7. Calendar Operations

### Scenario 7.1: Mark Non-Working Day
-   **Action**: `POST /api/admin/calendar/update` for a specific date with `isWorkingDay=false`.
-   **Expected Result**: HTTP 200 OK, Calendar entry updated.

### Scenario 7.2: Add Holiday Range
-   **Action**: `POST /api/admin/calendar/non-active` with Start Date and End Date.
-   **Expected Result**:
    -   HTTP 200 OK.
    -   `NonActiveDay` record created.
    -   Individual `Calendar` entries updated to non-working.

## 8. Notification & Alert

### Scenario 8.1: Create Template
-   **Action**: `POST /api/admin/notifications/templates` with Code, Subject, Body, Placeholders.
-   **Expected Result**: HTTP 201 Created.

### Scenario 8.2: Duplicate Template Code
-   **Precondition**: Template with code `WELCOME_EMAIL` exists.
-   **Action**: Create template with code `WELCOME_EMAIL`.
-   **Expected Result**: HTTP 400 Bad Request.

## 9. Audit & Monitoring

### Scenario 9.1: View Logs
-   **Action**: `GET /api/admin/audit`.
-   **Expected Result**: HTTP 200 OK, List of log entries returned.

### Scenario 9.2: Filter Logs by Service
-   **Action**: `GET /api/admin/audit/service/auth-service`.
-   **Expected Result**: HTTP 200 OK, Returns only logs where service name is `auth-service`.

## 10. Kafka Negative Flow

### Scenario 10.1: Kafka Broker Down
-   **Precondition**: Kafka broker is unreachable.
-   **Action**: Perform an action that triggers an event (e.g., Create Employee).
-   **Expected Result**:
    -   HTTP 200 OK (Application should not fail the request due to async messaging failure).
    -   Error logged in application logs regarding Kafka connection.
