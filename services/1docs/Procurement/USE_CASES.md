# Procurement Service Use Cases

## 1. Operator Use Cases

### 1.1 Submit Goods Procurement (Mengajukan Pengadaan Barang)
**Description**: The process where an Operator requests goods through a multi-step form (Header -> Item -> Delivery -> Document -> Summary) and submits it for Supervisor approval.
**Actor**: Operator
**Pre-conditions**:
1. Operator is logged in.
2. Active Vendor data is available.
3. Vendor Catalog is available.
4. Operator's location is registered.

**Basic Flow**:
1.  **Header**:
    *   Operator selects a Vendor.
    *   System validates Vendor Catalog availability.
    *   Operator fills Description, Priority (Normal/Urgent/Critical), and Deadline.
2.  **Item**:
    *   System loads Vendor Catalog.
    *   Operator selects items and sets quantity.
    *   System calculates subtotal and tax.
3.  **Delivery**:
    *   System defaults to Operator's location.
    *   Operator confirms/edits address and expected delivery date.
4.  **Document**:
    *   System generates auto-documents.
    *   Operator uploads additional documents if needed.
5.  **Summary & Submission**:
    *   system shows summary.
    *   Operator clicks "Submit for Approval".
    *   System sets status to `SUBMITTED`.
    *   System notifies Supervisor.
    *   Activity is audited.

**Alternative Flows**:
*   **A1 Save as Draft**: Operator saves progress to edit later.
*   **A2 No Catalog**: If vendor has no active catalog, system warns and prompts to select another vendor.
*   **A3 Auto-Reminder**: If Supervisor takes no action for 24h, system sends reminder.
*   **A4 Escalation**: If Supervisor takes no action for 48h, system escalates to Admin.

**Error Flows**:
*   **E1 Incomplete Data**: Missing description or deadline.
*   **E2 Invalid Quantity**: 0 or excessive quantity.
*   **E3 Load Failure**: Catalog fails to load.
*   **E4 Upload Failure**: Invalid file type or size.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant VS as Vendor Service
    participant NS as Notification Service
    participant AS as Audit Service

    O->>FE: Select Vendor
    FE->>VS: Get Catalog(vendorId)
    VS-->>FE: Catalog Items
    O->>FE: Select Items & Qty
    O->>FE: Submit Request
    FE->>PS: POST /procurement/goods
    PS->>PS: Validate Data
    PS->>PS: Create PR (Status: SUBMITTED)
    PS->>NS: Notify Supervisor(New PR)
    PS->>AS: Log Activity(CREATE_PR)
    PS-->>FE: PR Created (ID)
    FE-->>O: Success Message
```
*   **E5 Notification Failure**: Notification to Supervisor failed (Auto-retry).
*   **E6 Save Failure**: Failed to save data to database.

---

### 1.2 Submit Service Procurement (Melakukan Pengadaan Jasa)
**Description**: The process for requesting services, involving scope selection, payment terms, scheduling, and documents.
**Actor**: Operator

**Basic Flow**:
1.  **Header**: Select Vendor, fill Description, Priority, Deadline.
2.  **Scope**: Select service scope (e.g., Installation, Maintenance) from Vendor offerings; add technical details.
3.  **Terms**: Select payment terms (e.g., DP 30%, Progress 40%) if applicable, or "No Terms". Input duration.
4.  **Delivery (Schedule)**: Confirm location, set Start and End dates.
5.  **Document**: Auto-generated docs + optional Operator uploads.
6.  **Summary & Submission**: Submit for Supervisor approval.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant VS as Vendor Service
    participant NS as Notification Service
    participant AS as Audit Service

    O->>FE: Initiate Service Procurement
    FE->>VS: Get Service Scopes(vendorId)
    VS-->>FE: Service Scopes
    O->>FE: Select Scope & Terms
    O->>FE: Submit Request
    FE->>PS: POST /procurement/service
    PS->>PS: Create PR (Status: SUBMITTED)
    PS->>NS: Notify Supervisor
    PS->>AS: Log Activity
    PS-->>FE: Returns ID
```

**Alternative Flows**:
*   **A1 Save as Draft**.
*   **A2 Vendor has no services**: Warning displayed.
*   **A3 Terms not available**: Prompt to select valid term or 'No Terms'.
*   **A5 Escalation**: 48h escalation to Admin.

**Error Flows**:
*   **E1 Incomplete Data**: Description or Scope missing.
*   **E2 Scope Load Failure**: Service Scope fails to load from Vendor Service.
*   **E3 Upload Failure**: File too large or invalid format.
*   **E4 Invalid Date**: Start Date > End Date or after Deadline.

---

### 1.3 Review Service Terms (Peninjauan Tawaran Termin Jasa)
**Description**: Operator reviews terms proposed by a Vendor for a service request (phases, duration, cost, deliverables).
**Pre-conditions**: Vendor has submitted terms; Request is in "Submitted by Vendor" state.

**Basic Flow**:
1.  Operator views proposed terms (value, scope, timeline, deliverables).
2.  Operator clicks "Approve Terms" (Review-level).
3.  System updates status to `REVIEWED_BY_OPERATOR` and notifies Supervisor.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant NS as Notification Service
    participant AS as Audit Service

    O->>FE: View Vendor Terms
    FE->>PS: Get Terms(requestId)
    PS-->>FE: Terms Data
    O->>FE: Approve Terms
    FE->>PS: PUT /terms/review (Approve)
    PS->>PS: Update Status -> REVIEWED
    PS->>NS: Notify Supervisor
    PS->>AS: Log Activity
    PS-->>FE: Success
```

**Alternative Flows**:
*   **A1 Clarification**: Operator requests clarification. Status -> `CLARIFICATION_REQUESTED`.
*   **A2 Revision**: Operator requests revision. Status -> `REVISION_REQUESTED`.

**Error Flows**:
*   **E1 Already Processed**: Term has already been processed by Supervisor.
*   **E2 Missing Notes**: Revision/Clarification notes are empty.
*   **E3 Doc Access Failure**: Supporting documents cannot be accessed.
*   **E4 Status Save Failure**: Failed to update status.

---

### 1.4 Confirm Service Results (Melakukan Konfirmasi Hasil Jasa)
**Description**: Operator verifies that the service has been completed according to scope and terms.
**Pre-conditions**: Vendor marked service/term as completed; Vendor sent report.

**Basic Flow**:
1.  Operator views "Waiting for Confirmation" terms.
2.  Operator reviews Vendor's report and evidence.
3.  Operator clicks "Confirm Service Result".
4.  Operator enters confirmation notes.
5.  System updates status to `COMPLETED`/`VERIFIED`.
6.  System notifies Finance for payment.
7.  System updates Inventory (if applicable).

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant FS as Finance Service
    participant IS as Inventory Service
    participant AS as Audit Service

    O->>FE: View Completed Service
    O->>FE: Confirm Result & Notes
    FE->>PS: POST /service/confirm
    PS->>PS: Update Status -> VERIFIED
    PS->>FS: Notify Payment Due
    PS->>IS: Update Stock (if applicable)
    PS->>AS: Log Confirmation
    PS-->>FE: Success
```

**Alternative Flows**:
*   **A2 Request Revision**: Operator rejects result, requests revision. Status -> `REVISION_REQUESTED`.

**Error Flows**:
*   **E1 Empty Notes**: Confirmation notes are missing.
*   **E2 Doc Access Failure**: Vendor report cannot be accessed.
*   **E3 Save Failure**: Failed to save confirmation.
*   **E4 Invalid Status**: Term is not in "Waiting for Confirmation" state.

---

### 1.5 Goods/Service Receiving (Penerimaan Barang/Jasa)
**Description**: Operator verifies received goods/services against the PO.
**Pre-conditions**: PO status is "Shipping" or "Waiting for Acceptance".

**Basic Flow**:
1.  Operator selects PO.
2.  Operator physically checks items/work.
3.  Operator inputs result: Quantity Received, Condition, Notes.
4.  Action: "Accept", "Reject", or "Return".
5.  System updates PO status.
6.  **Goods**: Inventory stock increases. **Services**: Work log recorded.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant IS as Inventory Service
    participant AS as Audit Service

    O->>FE: Scan/Select PO
    FE->>PS: Get PO Details
    O->>FE: Input Received Qty & Condition
    FE->>PS: POST /receiving/submit
    PS->>PS: Update PO Status
    alt Goods
        PS->>IS: Increase Inventory Stock
    else Service
        PS->>PS: Log Work Completion
    end
    PS->>AS: Log Receiving
    PS-->>FE: Success
```

**Alternative Flows**:
*   **A1 Partial Acceptance**: Received quantity < PO quantity. Status -> `PARTIALLY_ACCEPTED`.
*   **A2 Return**: Goods damaged/wrong match. Status -> `RETURNED`.
*   **A3 Service Rejection**: Result mismatch. Status -> `REJECTED`.

**Error Flows**:
*   **E1 No Documents**: Delivery Order/Report missing.
*   **E2 Mismatch**: PO items differ from Physical documents.
*   **E3 Status Update Failure**: Failed to update PO status.
*   **E4 Inventory Sync Failure**: Failed to update stock (Pending Update flagged).
*   **E5 Audit Failure**: Failed to log activity (Local log saved).

---

### 1.6 Procurement Reports (Operator)
**Description**: Generating reports on PRs, POs, Realization vs Budget, Lead Time, Stock, Vendor Performance.
**Details**:
*   **Filters**: Period, Vendor, Location, Status, Category.
*   **Output**: On-screen table/charts, Export to PDF/Excel.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant IS as Inventory Service
    participant VS as Vendor Service
    participant AS as Audit Service

    O->>FE: Select Report Type & Filters
    FE->>PS: Get Report Data
    par Gather Data
        PS->>PS: Query PR/PO
        PS->>IS: Query Inventory
        PS->>VS: Query Vendor Performance
    end
    PS->>PS: Aggregate Data
    PS-->>FE: Report Data
    O->>FE: Request Export(PDF/Excel)
    FE->>PS: Download Report
    PS->>AS: Log Report Generation
    PS-->>FE: File Stream
```

**Error Flows**:
*   **E1 No Data**: No records match the filter.
*   **E2 Partial Data**: One service failed to respond (Incomplete flag).
*   **E3 Export Failure**: PDF/Excel generation failed.
*   **E4 Timeout**: Complex filter exceeded processing time limit.

---

### 1.7 Operator Dashboard
**Description**: Real-time summary of procurement status.
**Content**:
*   Counts: PR Draft/Submitted, PR Waiting Approval, Active POs.
*   Alerts: Stock Minimum, High Priority Notifications.
*   Quick Actions: Create PR, View Inventory.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant IS as Inventory Service
    participant NS as Notification Service

    O->>FE: Open Dashboard
    par Fetch Data
        FE->>PS: Get Request Counts
        FE->>IS: Get Stock Alerts
        FE->>NS: Get High Priority Notifications
    end
    PS-->>FE: PR/PO Stats
    IS-->>FE: Low Stock Items
    NS-->>FE: Urgent Alerts
    FE->>O: Display Dashboard
```

**Error Flows**:
*   **E1 Load Failure**: Dashboard data failed to load (Procurement/Inventory Service down).
*   **E2 Notification Failure**: Failed to load notifications.
*   **E3 Graph Failure**: Metric calculation failed (Fallback table shown).

---

### 1.8 Operator Notifications
**Description**: Manage alerts for approvals, deadlines, stock levels.
**Actions**: Mark as Read, Archive, Filter (by type), Preferences (Email/Push/Dashboard).

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant NS as Notification Service
    participant AS as Audit Service

    O->>FE: View Notifications
    FE->>NS: Get Notifications(userId)
    NS-->>FE: List [Unread, Read]
    O->>FE: Click "Mark as Read"
    FE->>NS: PUT /notifications/read/{id}
    NS->>AS: Log Action
    NS-->>FE: Success
    O->>FE: Update Preferences
    FE->>NS: POST /preferences
    NS-->>FE: Saved
```

**Error Flows**:
*   **E1 Load Failure**: Failed to fetch notifications.
*   **E2 Preference Save Failure**: Service failed to save settings.
*   **E3 Partial Bulk Fail**: Some items failed to update status.
*   **E4 Expired Data**: Notification references old/deleted data (>30 days).

---

### 1.9 Audit Trail & History (Operator)
**Description**: View transaction history and status changes for PR/PO.
**Features**:
*   Timeline of changes (Created -> Submitted -> Approved).
*   Who performed actions.
*   Compare Versions (e.g., original vs revised).

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor O as Operator
    participant FE as Frontend
    participant PS as Procurement Service
    participant AS as Audit Service

    O->>FE: Select PR/PO Document
    FE->>PS: Get Document Details
    O->>FE: Click "View History"
    FE->>AS: Get Audit Logs(entityId)
    AS-->>FE: List of Changes [Who, What, When]
    O->>FE: Compare Versions(v1, v2)
    FE->>AS: Get Version Diff
    AS-->>FE: Diffs
    FE->>O: Show Comparison
```

**Error Flows**:
*   **E1 History Not Found**: No history for selected document.
*   **E2 Log Fetch Failure**: Audit Service unresponsive.
*   **E3 Invalid Document**: Document deleted or expired.
*   **E4 Export Failure**: CSV download failed.

---

## 2. Supervisor Use Cases

### 2.1 Approve Procurement Request (Memberikan Persetujuan)
**Description**: Supervisor reviews Submitted PRs and makes a decision.
**Actor**: Supervisor

**Basic Flow**:
1.  View list of "Submitted" PRs for assigned locations.
2.  Review details (Vendor, Items, Cost, Priority).
3.  **Action**:
    *   **Approve**: PR becomes Approved -> PO creation process starts.
    *   **Reject**: PR closed.
    *   **Return**: Sent back to Operator for revision.
    *   **Comment**: Add feedback without status change.
4.  System notifies Operator.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant PS as Procurement Service
    participant NS as Notification Service
    participant AS as Audit Service

    S->>FE: View Submitted PRs
    FE->>PS: Get PRs(status=SUBMITTED)
    PS-->>FE: List of PRs
    S->>FE: Review PR & Approve
    FE->>PS: PUT /approval/submit
    PS->>PS: Update Status -> APPROVED
    PS->>NS: Notify Operator(Approved)
    PS->>AS: Log Approval
    PS-->>FE: Success
```

**Alternative Flows**:
*   **A4 Auto-Escalation**: If no decision in 48h, escalated to Admin.

**Error Flows**:
*   **E1 Status Changed**: Request already processed by another Supervisor/Admin.
*   **E2 Save Failure**: Failed to save decision.
*   **E3 Notification Failure**: Failed to notify Operator (Retry logic).
*   **E4 Audit Failure**: Audit logging failed (Pending sync).

---

### 2.2 Supervisor Reports
**Description**: High-level reports for management.
**Types**:
*   Budget Evaluation.
*   Operator Performance.
*   Inventory per Location.
*   Service History.
*   **Analytics Dashboard (Trends, Lead Time, Vendor KPIs).

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant PS as Procurement Service
    participant IS as Inventory Service
    participant FS as Finance Service

    S->>FE: View Supervisor Reports
    S->>FE: Select Report (e.g. Budget Eval)
    FE->>PS: Get Report Data
    par Fetch
        PS->>FS: Get Budget Realization
        PS->>IS: Get Inventory Value
    end
    PS-->>FE: Consolidated Report
    S->>FE: Export Details
    FE->>PS: Generate PDF
    PS-->>FE: PDF File
```

**Error Flows**:
*   **E1 No Data**: No records found for selected period/filter.
*   **E2 Partial Data**: Service timeout for one of the data sources.
*   **E3 Export Failure**: Failed to render PDF/Excel.
*   **E4 Schedule Failure**: Automatic report scheduling failed to save.

---

### 2.3 Budget Allocation Management (Mengatur Alokasi Anggaran)
**Description**: Monitor Operator budget usage and request Top-Ups.
**Constraint**: Supervisor cannot *reduce* budget, only request *additions*.

**Basic Flow**:
1.  View Operator list with budget usage %.
2.  Select Operator nearing limit (Warning/Reference).
3.  Click "Request Top-Up".
4.  Enter Amount and Reason.
5.  Submit to Finance Service.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant PS as Procurement Service
    participant FS as Finance Service
    participant NS as Notification Service
    participant AS as Audit Service

    S->>FE: View Budget Allocation
    FE->>FS: Get Operator Budgets
    FS-->>FE: Budget Status
    S->>FE: Request Top-Up(amount, reason)
    FE->>PS: POST /budget/topup
    PS->>FS: Submit Top-Up Request
    FS->>FS: Process/Queue Request
    FS->>NS: Notify Finance Admin
    PS->>AS: Log Top-Up Request
    PS-->>FE: Success
```

**Error Flows**:
*   **E1 Load Failure**: Budget data failed to retrieve from Finance.
*   **E2 Invalid Amount**: Top-up amount 0 or exceeds max limit.
*   **E3 Save Failure**: Request failed to submit.
*   **E4 Notification Failure**: Finance Service unreachable for notification.

---

### 2.4 Set Procurement Priority
**Description**: Adjust urgency of PRs.
**Options**: Normal, Important, Urgent.
**Effect**: Updates workflow priority and notifies Operator.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant PS as Procurement Service
    participant NS as Notification Service
    participant AS as Audit Service

    S->>FE: View PR List
    S->>FE: Change Priority (Normal -> Urgent)
    FE->>PS: PUT /priority/update
    PS->>PS: Update Priority
    PS->>NS: Notify Operator
    PS->>AS: Log Priority Change
    PS-->>FE: Success
```

**Error Flows**:
*   **E1 Access Denied**: Supervisor not assigned to this location.
*   **E2 Save Failure**: Priority update failed.
*   **E3 Data Conflict**: Concurrent update detected.
*   **E4 Notification Failure**: Notification to Operator failed.

---

### 2.5 Supervisor Dashboard & Analytics
**Description**: Visual metrics for decision making.
**Metrics**:
*   Monthly Trend (PR/PO volume).
*   Budget Realization.
*   Vendor Performance (On-time vs Late).
*   **Average Lead Time.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant PS as Procurement Service
    participant IS as Inventory Service
    participant VS as Vendor Service

    S->>FE: Open Analytics Dashboard
    par Fetch Metrics
        FE->>PS: Get PR Trend & Lead Time
        FE->>IS: Get Stock Value
        FE->>VS: Get Vendor Performance
    end
    PS->>PS: Calculate Aggregates
    PS-->>FE: Dashboard Data
    FE->>S: Display Charts & KPIs
```

**Error Flows**:
*   **E1 Service Failure**: Data sources (Procurement/Inventory/Finance) unreachable.
*   **E2 Incomplete Data**: Historical data missing for a location.
*   **E3 Graph Failure**: Rendering error (Fallback to table).
*   **E4 Export Failure**: Dashboard export failed.

---

### 2.6 Supervisor Notifications
**Description**: Managing approvals, deadline alerts, and budget warnings.
**Actions**: Read, Archive, Configure Preferences.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant NS as Notification Service

    S->>FE: View Notifications
    FE->>NS: Get Inbox(supervisorId)
    NS-->>FE: List of Alerts
    S->>FE: Click "View Detail"
    FE->>NS: Mark Read
    FE->>S: Redirect to PR/Budget Detail
```

**Error Flows**:
*   **E1 Load Failure**: Notifications failed to load.
*   **E2 Save Failure**: Preference settings failed to save.
*   **E3 Sync Failure**: Status update (read/unread) failed to sync.
*   **E4 Duplication**: Duplicate notifications detected and merged.

---

### 2.7 Supervisor Audit & History
**Description**: Review team's transaction history, logs, and document versions.

**Sequence Diagram**:
```mermaid
sequenceDiagram
    actor S as Supervisor
    participant FE as Frontend
    participant AS as Audit Service

    S->>FE: View Team Audit
    S->>FE: Filter by Operator/Date
    FE->>AS: Get Logs(filters)
    AS-->>FE: Activity Logs
    S->>FE: Export Audit Log
    FE->>AS: Request CSV Export
    AS-->>FE: CSV File
```

**Error Flows**:
*   **E1 No Data**: No audit logs found for filter.
*   **E2 Service Failure**: Audit Service down.
*   **E3 Version Missing**: Document version comparison unavailable.
*   **E4 Export Failure**: Audit report export failed.
