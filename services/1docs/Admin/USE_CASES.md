# Admin Service Use Cases

This document details the use cases for the Admin Service, covering comprehensive flows for managing employees, vendors, locations, procurement data, master data, system logs, calendars, and notifications.

---

## 4.4.4.3.1 Manage Employees (Mengelola Karyawan)

**Description**: Admin manages internal employee data for the e-Procurement system. This includes adding, updating, and deactivating accounts, as well as assigning roles and work locations. All changes are logged for audit.
**Actor**: Admin
**Pre-conditions**:
1. Admin is logged in.
2. Master role and location data are available.
3. Connection to Master Data Service & Audit Service is active.
**Post-conditions**:
1. Employee data is updated.
2. Activity log is recorded.
3. Notification sent to employee (optional).

### Basic Flow
1. Admin opens the "Employee Management" module.
2. System displays a list of all employees (Name, Email, Role, Status, Location) in a table.
3. Admin selects an action: `Add Employee`, `Edit Employee`, or `Deactivate Employee`.
4. System displays the corresponding form.
5. Admin fills in or updates the required data (Name, Email, Role, Location).
6. System validates the data.
7. System saves changes to **Master Data Service**.
8. System sends modification notification to the employee (optional).
9. **AuditService** records the action with details (action type, actor ID, timestamp).
10. Use case ends.

### Alternative Flows
**A1: Add New Employee**
1. Admin clicks "Add".
2. System displays Add Employee form.
3. Admin inputs new employee data.
4. System validates email format and username/email uniqueness.
5. (If duplicate, see E3).
6. System saves new employee to Master Data Service.
7. System displays success message: "Employee account successfully added."
8. AuditService records `ADD_EMPLOYEE`.
9. Return to Basic Flow step 2.

**A2: Update Employee Data/Role**
1. Admin clicks "Edit" on an employee.
2. System displays current employee data.
3. Admin updates role and/or work location.
4. System validates that Admin is not modifying their own role (see E1).
5. System updates employee data in Master Data Service.
6. System displays success message.
7. AuditService records `UPDATE_EMPLOYEE`.
8. Return to Basic Flow step 2.

**A3: Deactivate Employee**
1. Admin clicks "Deactivate".
2. System requests confirmation.
3. Admin confirms "Yes, Deactivate".
4. System updates employee status to `INACTIVE`.
5. System displays success message.
6. AuditService records `DEACTIVATE_EMPLOYEE`.
7. Return to Basic Flow step 2.

### Error Flows
**E1: Admin modifies own role**
1. System detects the target account matches the logged-in Admin.
2. System rejects the change.
3. Message: "You are not allowed to modify your own role."
4. Process aborts.

**E2: Mandatory fields empty**
1. System detects empty mandatory fields.
2. Message: "Mandatory fields cannot be empty."
3. Admin returns to form.

**E3: Duplicate Email/Username**
1. System detects duplicate email/username.
2. Message: "Email/Username is already in use."
3. Admin inputs a different value.

---

## 4.4.4.3.2 Manage Vendors (Mengelola Vendor)

**Description**: Admin manages registered vendors, verifying new registrations, reviewing legal documents, adding evaluation notes, and setting active/inactive status. Admins cannot edit vendor data directly but can reject verification with notes.
**Actor**: Admin
**Pre-conditions**:
1. Admin is logged in.
2. Vendor has registered.
3. Vendor data available in Vendor Service.
**Post-conditions**:
1. Vendor status updated.
2. Evaluation notes saved.
3. Vendor notified.
4. Audit log recorded.

### Basic Flow
1. Admin opens "Vendor Management".
2. System displays list of vendors with verification status (Pending, Approved, Rejected).
3. Admin selects a vendor.
4. System shows details and legal documents (NPWP, SIUP, etc.).
5. Admin selects action: `Approve`, `Reject`, `Deactivate`, or `Add Evaluation Note`.
6. System validates current vendor status.
7. System saves status change or note to **Vendor Service**.
8. System sends automatic notification to vendor.
9. **AuditService** records action.
10. Use case ends.

### Alternative Flows
**A1: Approve Vendor**
1. Admin clicks "Approve".
2. Admin confirms "Yes, Approve".
3. System updates status to `APPROVED`.
4. Notification: "Your vendor account has been verified."
5. AuditService records `APPROVE_VENDOR`.

**A2: Reject Vendor**
1. Admin clicks "Reject".
2. System requests rejection reason.
3. Admin inputs reason.
4. System updates status to `REJECTED` with reason.
5. Notification: "Verification rejected. Reason: [Admin Note]."
6. AuditService records `REJECTED_VENDOR`.

**A3: Deactivate Vendor**
1. Admin clicks "Deactivate".
2. Admin confirms.
3. System updates status to `INACTIVE`.
4. AuditService records `DEACTIVATE_VENDOR`.

**A4: Add Evaluation Note**
1. Admin clicks "Add Evaluation Note".
2. Admin inputs note (e.g., document audit result).
3. System saves note to Vendor Service.
4. AuditService records `ADD_VENDOR_NOTE`.

### Error Flows
**E1: Invalid Status Transition**
1. System detects invalid action for current status (e.g., Approving an already Approved vendor).
2. Message: "Action invalid for current vendor status."

**E2: Data Access Failure**
1. Vendor Service timeout/error.
2. Message: "Failed to access vendor data. Please try again."
3. Audit records `ACCESS_VENDOR_FAILED`.

---

## 4.4.4.3.3 Manage Assignment Locations (Mengelola Lokasi Penugasan)

**Description**: Admin manages work locations (Sites) and assigns Operators (1:1 with Location) and Supervisors (1:N with Locations).
**Actor**: Admin
**Pre-conditions**:
1. Master location data available.
2. Employee data available.

### Basic Flow
1. Admin opens "Assignment Locations".
2. System lists all locations (HQ, Branch, Warehouse, Project) and status.
3. Admin selects action: `Add Location`, `Edit Location`, `Set Operator`, `Set Supervisor`, `Deactivate`, `Export`.
4. System displays form/dialog.
5. Admin provides input.
6. System validates data/users.
7. System saves to **Master Data Service**.
8. Success message.
9. AuditService records action.

### Alternative Flows
**A1: Add Location**
1. Admin inputs Name, Address, Type, Status.
2. System validates unique name.
3. Save Location.
4. Audit: `ADD_LOCATION`.

**A2: Edit Location**
1. Admin updates details.
2. Save changes.
3. Audit: `UPDATE_LOCATION`.

**A3: Assign Operator**
1. Admin selects "Set Operator".
2. System shows operators without location.
3. Admin selects Operator.
4. System validates Operator is not assigned elsewhere.
5. Save assignment.
6. Audit: `ASSIGN_OPERATOR`.

**A4: Assign Supervisor**
1. Admin selects "Set Supervisor".
2. Admin selects Supervisor and one or more Locations.
3. Save relation.
4. Audit: `ASSIGN_SUPERVISOR`.

**A5: Deactivate Location**
1. Set status `INACTIVE`.
2. Audit: `DEACTIVATE_LOCATION`.

**A6: Export Locations**
1. Admin selects PDF/Excel.
2. System generates and downloads file.

### Error Flows
**E1: Duplicate Location Name**
1. Message: "Location name already exists."

**E2: Operator Already Assigned**
1. Message: "Operator already assigned to another location."

**E3: Connection Failure**
1. Message: "Failed to save changes."
2. Audit: `LOCATION_UPDATE_FAILED`.

---

## 4.4.4.3.4 Manage Procurement Data (Mengelola Data Pengadaan)

**Description**: Admin manages cross-unit procurement data (PR/PO). Admin can correct administrative metadata, force status changes, or flag for investigation. **Core transaction data (Items, Qty, Value) cannot be changed.**
**Actor**: Admin
**Pre-conditions**:
1. Procurement data available.
2. Integrated with Master Data & Audit.

### Basic Flow
1. Admin opens "Procurement Data".
2. System lists cross-location PRs/POs with filters.
3. Admin selects a document.
4. System shows details, timeline, notes.
5. Admin selects action: `Administrative Correction`, `Change Status`, `Mark for Investigation`, `Export`.
6. System validates rights and constraints.
7. System saves changes to **Procurement Service**.
8. AuditService logs changes (Old Value -> New Value).

### Alternative Flows
**A1: Administrative Correction**
1. Admin updates metadata (Category, Urgency, Internal Notes).
2. **Constraint**: Value/Items/Qty read-only.
3. Save.
4. Audit: `UPDATE_ADMIN_METADATA`.

**A2: Change Document Status**
1. Admin selects new status (e.g., Pending -> Revision).
2. System validates legal transition.
3. Save.
4. Audit: `UPDATE_DOC_STATUS`.

**A3: Mark for Investigation**
1. Admin provides investigation reason.
2. System flags document.
3. Audit: `FLAG_INVESTIGATION`.

### Error Flows
**E1: Invalid Status Action**
1. Message: "Action cannot be performed on current status."

**E2: Illegal Modification**
1. Admin attempts to change core values (Qty/Price).
2. System rejects.
3. Message: "Core transaction modification not allowed."

---

## 4.4.4.3.5 Manage System Master Data (Mengelola Master Data Sistem)

**Description**: Admin manages shared master data (Items, Categories, Units, Document Statuses). Includes synchronization between services.
**Actor**: Admin

### Basic Flow
1. Admin opens "System Master Data".
2. Selects entity (Item, Category, Unit, etc.).
3. Selects action: `Add`, `Edit`, `Set Relation`, `Sync`, `Export`.
4. Admin inputs data.
5. System validates.
6. System saves to **Master Data Service**.
7. AuditService records action.

### Alternative Flows
**A1: Add Master Data**
1. Admin fills form.
2. System checks duplicates (Name/Code).
3. Save.
4. Audit: `ADD_MASTER_DATA`.

**A2: Edit Master Data**
1. Update fields.
2. Save.
3. Audit: `UPDATE_MASTER_DATA`.

**A3: Set Relations**
1. Link entities (e.g., Item -> Category).
2. System validates integrity.
3. Save.
4. Audit: `SET_MASTER_RELATION`.

**A4: Sync Data**
1. Admin selects target service (Procurement, Vendor, etc.).
2. System triggers sync event.
3. Report results.
4. Audit: `SYNC_MASTER_DATA`.

### Error Flows
**E3: Duplicate Data**
1. Message: "Master data already exists."

**E4: Invalid Relation**
1. Message: "Invalid data relation."

**E5: Sync Failed**
1. Message: "Synchronization failed."
2. Audit: `SYNC_FAILED`.

---

## 4.4.4.3.6 Manage System Activity Logs (Manajemen Log Aktivitas Sistem)

**Description**: Admin monitors audit logs for security and compliance. Supports filtering, exporting, and backing up. Logs cannot be deleted.
**Actor**: Admin
**Pre-conditions**:
1. Logs available in Audit/Logging Service.

### Basic Flow
1. Admin opens "System Activity Logs".
2. Admin sets filters (Date, User, Module, Severity).
3. System displays filtered logs.
4. Admin performs: `Export` or `Backup`.
5. AuditService records "Admin accessed logs".

### Alternative Flows
**A2: Export Log**
1. Select format (PDF/CSV).
2. Download.
3. Audit: `EXPORT_LOG`.

**A3: Backup Log**
1. Confirm backup to separate server.
2. System transfers logs.
3. Audit: `BACKUP_LOG`.

### Error Flows
**E1: Service Unavailable**
1. Message: "Failed to retrieve logs."

---

## 4.4.4.3.7 Manage Operational Calendar (Mengelola Kalender Operasional)

**Description**: Admin defines workdays, cut-off dates for procurement, and holidays.
**Actor**: Admin

### Basic Flow
1. Admin opens "Operational Calendar".
2. View month-view and important dates.
3. Actions: `Add Workday`, `Set Cut-Off`, `Deactivate Date`, `Export`.
4. Validate dates.
5. Save to **Calendar Service**.
6. Sync with **Notification Service** (for deadlines).
7. AuditService records action.

### Alternative Flows
**A1: Add Workday**
1. Select date.
2. Save as workday.
3. Audit: `ADD_WORKDAY`.

**A2: Set Cut-Off**
1. Select date for monthly cut-off.
2. Validate (not a holiday).
3. Save.
4. Audit: `SET_CUTOFF_DATE`.

**A3: Deactivate Date**
1. Select date.
2. Validate no active critical processes.
3. Mark non-active.
4. Audit: `SET_NONACTIVE_DAY`.

### Error Flows
**E2: Cut-off on Non-active Day**
1. Message: "Cut-off cannot be on a non-active day."

**E3: Active Process Conflict**
1. Message: "Cannot deactivate date with active procurement processes."

---

## 4.4.4.3.8 Verify Vendor (Verifikasi Vendor)

**Description**: Detailed flow for reviewing vendor registrations.
**Actor**: Admin
**Pre-conditions**: Status is `pending_verification`.

### Basic Flow
1. Admin opens "Vendor Verification".
2. Lists pending vendors.
3. Admin reviews Company Profile and Documents.
4. Action: `Approve`, `Reject`, `Request Revision`.
5. System saves result.
6. Notify Vendor.
7. Audit: `VERIFY_VENDOR`.

### Alternative Flows
**A1: Approve**
1. Status -> `APPROVED`.
2. Notify: "Approved".
3. Audit: `APPROVE_VENDOR_REGISTRATION`.

**A2: Reject**
1. Input reason.
2. Status -> `REJECTED`.
3. Notify: "Rejected: [Reason]".
4. Audit: `REJECT_VENDOR_REGISTRATION`.

**A3: Request Revision**
1. Input correction notes (e.g., "Blurry Document").
2. Status -> `PENDING_REVISION`.
3. Notify: "Please revise."
4. Audit: `REQUEST_VENDOR_REVISION`.

### Error Flows
**E1: Corrupt Document**
1. Message: "Document inaccessible."

**E2: Invalid Status**
1. Message: "Vendor not in verification status."

---

## 4.4.4.3.9 Audit & System Monitoring (Audit & Monitoring Sistem)

**Description**: Proactive monitoring, anomaly detection, and user suspension.
**Actor**: Admin

### Basic Flow
1. Open "Audit & Monitoring".
2. Dashboard shows stats/alerts.
3. Actions: `View Audit Trail`, `Monitor Procurement`, `Detect Anomaly`, `Generate Report`.
4. Analyze data.
5. Corrective Action (e.g., Suspend User).
6. Save context.
7. Audit log.

### Alternative Flows
**A2: Monitor Procurement**
1. View delayed/suspicious PRs.
2. Flag for investigation.
3. Audit: `FLAG_PROCUREMENT`.

**A3: Detect Anomaly**
1. Run detection (login failures, weird access).
2. View results.
3. Audit: `VIEW_ANOMALY`.

**A5: Suspend User**
1. Select User.
2. Action: Suspend.
3. Status -> `SUSPENDED`.
4. Notify Internal Admin.
5. Audit: `SUSPEND_USER`.

---

## 4.4.4.3.10 Manage Notifications & Alerts (Manajemen Notifikasi & Alert)

**Description**: Admin configures notification templates and recipients.
**Actor**: Admin

### Basic Flow
1. Open "Notifications & Alerts".
2. List configurations/templates.
3. Actions: `Edit Template`, `Set Receivers`, `View Log`.
4. Update config.
5. Save to **Notification Service**.
6. Audit log.

### Alternative Flows
**A1: Edit Template**
1. Select template (e.g., "PR Approved").
2. Edit text/placeholders.
3. Validate syntax.
4. Save.
5. Audit: `UPDATE_NOTIFICATION_TEMPLATE`.

**A3: View Send Logs**
1. Filter logs (time, status).
2. View/Export.
3. Audit: `VIEW_NOTIFICATION_LOG`.

### Error Flows
**E1: Invalid Placeholder**
1. Message: "Invalid placeholder format."

**E2: No Receiver**
1. Message: "Event must have at least 1 receiver."
