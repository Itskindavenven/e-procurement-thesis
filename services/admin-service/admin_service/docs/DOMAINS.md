# Domain Logic Guide

This document details the business logic and workflows for each domain in the Admin Service.

## 1. Employee Management
**Purpose**: Manage internal users (employees) and their access.
-   **Entities**: `Employee`, `Role`
-   **Key Operations**:
    -   **Create Employee**: Registers a new employee. Checks for email uniqueness.
    -   **Assign Role**: Assigns a role (e.g., ADMIN, OPERATOR) to an employee.
    -   **Assign Location**: Links an employee to a specific operational location.
-   **Events**: `employee.created`, `employee.updated`, `employee.deactivated`

## 2. Vendor Management (Verification)
**Purpose**: Handle the verification process for new vendor registrations.
-   **Entities**: `VendorRegistration`
-   **Workflow**:
    1.  Vendor registers (handled by Auth Service/Vendor Service).
    2.  Admin reviews documents.
    3.  Admin takes action: **APPROVE**, **REJECT**, or **REQUEST REVISION**.
    4.  Admin can add internal notes.
-   **Events**: `vendor.approved`, `vendor.rejected`, `vendor.revision_requested`

## 3. Location Management
**Purpose**: Manage physical or logical locations for operations.
-   **Entities**: `Lokasi`, `OperatorLocation`, `SupervisorLocation`
-   **Key Operations**:
    -   **Create Location**: Define a new warehouse, office, or site.
    -   **Assign Operator**: 1-to-1 mapping. An operator can only manage one location.
    -   **Assign Supervisor**: M-to-N mapping. A supervisor can oversee multiple locations.
-   **Events**: `location.created`, `operator.assigned`, `supervisor.assigned`

## 4. Master Data Management
**Purpose**: Central repository for standardized data (items, units, categories).
-   **Entities**: `MasterDataItem`, `MasterDataRelation`
-   **Key Operations**:
    -   **CRUD**: Create, read, update, delete master items.
    -   **Relations**: Define parent-child relationships (e.g., Category -> Sub-category).
    -   **Sync**: Trigger synchronization to other services (Procurement, Inventory).
-   **Events**: `masterdata.created`, `sync.requested`

## 5. Procurement Data Management
**Purpose**: Administrative oversight of procurement processes.
-   **Entities**: `ProcurementCorrection`, `AuditInvestigationFlag`
-   **Key Operations**:
    -   **Correction**: Admin fixes metadata errors in procurement records.
    -   **Investigation**: Flag suspicious procurement activities for audit.
-   **Events**: `procurement.metadata.updated`, `procurement.investigation.flagged`

## 6. Calendar Operations
**Purpose**: Define the operational calendar for the organization.
-   **Entities**: `Calendar`, `NonActiveDay`
-   **Key Operations**:
    -   **Set Working Days**: Define which days are operational.
    -   **Set Cutoff**: Define cutoff dates for monthly processing.
    -   **Manage Holidays**: Add non-active days (holidays, maintenance).
-   **Events**: `calendar.updated`, `calendar.cutoff.updated`, `nonactive.day.added`

## 7. Notification & Alert
**Purpose**: Manage communication templates and track history.
-   **Entities**: `NotificationTemplate`, `NotificationLog`
-   **Key Operations**:
    -   **Templates**: Create and edit email/SMS templates with placeholders.
    -   **Logs**: View history of sent notifications and their status.

## 8. Audit & Monitoring
**Purpose**: Centralized logging for security and compliance.
-   **Entities**: `LogSystem`
-   **Key Operations**:
    -   **Log Recording**: Capture actions, user IDs, IPs, and details.
    -   **Search**: Query logs by service, user, or date.
