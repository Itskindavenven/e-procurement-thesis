# Database Schema Documentation

The Admin Service uses a relational database (PostgreSQL) with the following schema.

## Tables

### Employee Management
-   **`employees`**: Stores employee details (name, email, status).
-   **`roles`**: Stores available roles (e.g., ADMIN, OPERATOR).

### Location Management
-   **`locations`**: Stores operational locations.
-   **`operator_locations`**: 1:1 mapping between an operator (employee) and a location.
-   **`supervisor_locations`**: M:N mapping between supervisors and locations.

### Vendor Management
-   **`vendor_registrations`**: Stores vendor registration requests and their verification status.

### Master Data
-   **`master_data_items`**: Stores standardized items, categories, and units.
-   **`master_data_relations`**: Stores hierarchical relationships (Parent-Child) between items.

### Procurement
-   **`procurement_corrections`**: Logs administrative corrections made to procurement records.
-   **`audit_investigation_flags`**: Stores flags for procurement items under investigation.

### Calendar
-   **`calendars`**: Stores daily operational status (working day, cutoff).
-   **`calendar_nonactive_days`**: Stores ranges of non-active days (holidays).
    > **Note**: `calendar_nonactive_days` stores date ranges (start_date, end_date). The system must auto-expand these ranges into individual `calendars` entries with `is_working_day = false` when a range is added or updated.

### Notifications
-   **`notification_templates`**: Stores email/SMS templates.
-   **`notification_template_placeholders`**: Stores valid placeholders for templates.
-   **`notification_logs`**: Logs sent notifications.
-   **`notification_recipients`**: Stores recipients for each log entry.

### Audit
-   **`log_systems`**: Centralized table for system audit logs.

## Common Fields
All entities (except join tables) inherit from `BaseEntity` and include:
-   `created_at` (Timestamp)
-   `updated_at` (Timestamp)
-   `created_by` (String)
-   `updated_by` (String)
-   `is_deleted` (Boolean, for soft delete)
