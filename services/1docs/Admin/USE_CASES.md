# Admin Service Use Cases

## 1. Procurement Oversight
The Admin Service allows administrators to intervene in the procurement lifecycle when standard workflows are insufficient or when errors occur.

*   **Administrative Correction**:
    *   **Goal**: Fix data entry errors (e.g., wrong Cost Center, typo in item description) without restarting the entire workflow.
    *   **Actor**: Admin
    *   **Action**: Update specific fields of a `ProcurementRequest`.
    *   **Outcome**: Correction event is published; data is updated in Procurement Service.

*   **Investigation Flagging**:
    *   **Goal**: Halt a transaction suspected of fraud or severe error.
    *   **Actor**: Admin
    *   **Action**: Flag a `ProcurementRequest` with a reason.
    *   **Outcome**: Request is frozen/flagged in Procurement Service until resolved.

*   **Document Status Override**:
    *   **Goal**: Forcefully change the status of a document (e.g., stuck in "PROCESSING" due to system error).
    *   **Actor**: Admin
    *   **Action**: Update status manually.

## 2. Vendor Management
*   **Vendor Verification**:
    *   **Goal**: Onboard new vendors.
    *   **Actor**: Admin
    *   **Action**: Review pending vendor registrations, check documents, and set status to `VERIFIED` or `REJECTED`.
    *   **Outcome**: Vendor can login and participate in RFQs.

## 3. Organization Management
*   **Employee Management**:
    *   **Goal**: Maintain the user base.
    *   **Actor**: Admin
    *   **Action**: Register new employees, update details, assign roles (OPERATOR, SUPERVISOR, FINANCE), and deactivate accounts.

*   **Location (Lokasi) Management**:
    *   **Goal**: Define physical sites for operations.
    *   **Actor**: Admin
    *   **Action**: Create locations, assign a default **Operator** and **Supervisor** to each location.

## 4. System Configuration
*   **Master Data Management**:
    *   **Goal**: Maintain the standard catalog of items and services.
    *   **Actor**: Admin
    *   **Action**: Add/Edit/Delete Items, Categories, and Units of Measurement.

*   **Calendar Management**:
    *   **Goal**: Configure operational timelines.
    *   **Action**: Set monthly cut-off dates for finance; mark non-active days (holidays).

*   **Notification Templates**:
    *   **Goal**: Standardize communication.
    *   **Action**: Edit email/SMS templates used by the system.

## 5. Monitoring
*   **Audit Logging**:
    *   **Goal**: Traceability.
    *   **Action**: View centralized logs of system actions (Who did What, When).
