-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ROLES
CREATE TABLE roles (
    role_id VARCHAR(50) PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- LOCATIONS
CREATE TABLE locations (
    location_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    location_name VARCHAR(100) NOT NULL,
    address TEXT,
    location_type VARCHAR(50) NOT NULL, -- HEAD_OFFICE, BRANCH, WAREHOUSE, PROJECT_SITE
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- EMPLOYEES
CREATE TABLE employees (
    employee_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id VARCHAR(50) REFERENCES roles(role_id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- OPERATOR LOCATION (1:1)
CREATE TABLE operator_locations (
    operator_id UUID PRIMARY KEY REFERENCES employees(employee_id),
    location_id UUID NOT NULL REFERENCES locations(location_id),
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(50)
);

-- SUPERVISOR LOCATIONS (M:N)
CREATE TABLE supervisor_locations (
    supervisor_id UUID REFERENCES employees(employee_id),
    location_id UUID REFERENCES locations(location_id),
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(50),
    PRIMARY KEY (supervisor_id, location_id)
);

-- VENDOR REGISTRATIONS
CREATE TABLE vendor_registrations (
    registration_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vendor_id UUID NOT NULL, -- Reference to Vendor Service ID
    document_url TEXT,
    verification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED, REVISION_REQUESTED
    admin_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- MASTER DATA ITEMS
CREATE TABLE master_data_items (
    item_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE,
    category VARCHAR(50),
    unit VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- MASTER DATA RELATIONS
CREATE TABLE master_data_relations (
    relation_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    parent_id UUID NOT NULL REFERENCES master_data_items(item_id),
    child_id UUID NOT NULL REFERENCES master_data_items(item_id),
    relation_type VARCHAR(50) NOT NULL, -- e.g., CATEGORY_ITEM, BUNDLE_ITEM
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- PROCUREMENT CORRECTIONS (Administrative)
CREATE TABLE procurement_corrections (
    correction_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    procurement_id UUID NOT NULL, -- Reference to Procurement Service
    correction_type VARCHAR(50) NOT NULL, -- METADATA, STATUS, ETC
    old_value TEXT,
    new_value TEXT,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- AUDIT INVESTIGATION FLAGS
CREATE TABLE audit_investigation_flags (
    flag_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    procurement_id UUID NOT NULL,
    reason TEXT NOT NULL,
    flagged_by VARCHAR(50) NOT NULL,
    flagged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' -- OPEN, RESOLVED
);

-- CALENDARS
CREATE TABLE calendars (
    calendar_date DATE PRIMARY KEY,
    is_working_day BOOLEAN NOT NULL DEFAULT TRUE,
    is_cutoff BOOLEAN NOT NULL DEFAULT FALSE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- CALENDAR NON-ACTIVE DAYS
CREATE TABLE calendar_nonactive_days (
    nonactive_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- NOTIFICATION TEMPLATES
CREATE TABLE notification_templates (
    template_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(100) NOT NULL UNIQUE, -- e.g., VENDOR_APPROVED
    subject_template VARCHAR(255) NOT NULL,
    body_template TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- NOTIFICATION TEMPLATE PLACEHOLDERS
CREATE TABLE notification_template_placeholders (
    placeholder_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    template_id UUID REFERENCES notification_templates(template_id),
    placeholder_key VARCHAR(50) NOT NULL, -- e.g., {{vendor_name}}
    description VARCHAR(255)
);

-- NOTIFICATION RECIPIENTS
CREATE TABLE notification_recipients (
    recipient_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(100) NOT NULL,
    recipient_role VARCHAR(50), -- Send to all users with this role
    recipient_email VARCHAR(100), -- Or specific email
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- NOTIFICATION LOGS
CREATE TABLE notification_logs (
    log_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(100) NOT NULL,
    recipient VARCHAR(100) NOT NULL,
    channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL',
    status VARCHAR(20) NOT NULL, -- SENT, FAILED
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    error_message TEXT
);

-- LOG SYSTEM (Audit Trail)
CREATE TABLE log_systems (
    log_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    module VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    user_id VARCHAR(50),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    severity VARCHAR(20) NOT NULL DEFAULT 'INFO', -- INFO, WARNING, ERROR, CRITICAL
    ip_address VARCHAR(50),
    user_agent VARCHAR(255)
);
