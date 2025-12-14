-- V6__create_termin_details_table.sql
-- Create termin_details table for service procurement milestones

CREATE TABLE IF NOT EXISTS termin_details (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL,
    termin_number INTEGER NOT NULL,
    phase_name VARCHAR(200) NOT NULL,
    value DECIMAL(15, 2) NOT NULL CHECK (value >= 0),
    deliverables TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED_BY_VENDOR' 
        CHECK (status IN ('SUBMITTED_BY_VENDOR', 'CLARIFICATION_REQUESTED', 'REVISION_REQUESTED', 
                          'REVIEWED_BY_OPERATOR', 'APPROVED_BY_SUPERVISOR', 'REJECTED')),
    vendor_submitted_at TIMESTAMP,
    operator_reviewed_at TIMESTAMP,
    supervisor_decided_at TIMESTAMP,
    clarification_notes TEXT,
    revision_notes TEXT,
    operator_review_notes TEXT,
    supervisor_notes TEXT,
    reviewed_by UUID,
    approved_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_termin_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_termin_pr_id ON termin_details(procurement_request_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_termin_status ON termin_details(status) WHERE is_deleted = FALSE;
CREATE INDEX idx_termin_vendor_submitted ON termin_details(vendor_submitted_at DESC) WHERE is_deleted = FALSE;
CREATE INDEX idx_termin_number ON termin_details(procurement_request_id, termin_number) WHERE is_deleted = FALSE;

-- Comments
COMMENT ON TABLE termin_details IS 'Service procurement payment milestones/termins';
COMMENT ON COLUMN termin_details.termin_number IS 'Sequential number: 1, 2, 3, etc.';
COMMENT ON COLUMN termin_details.phase_name IS 'Name of the service phase, e.g., Initial Design, Implementation';
COMMENT ON COLUMN termin_details.value IS 'Payment amount for this termin';
COMMENT ON COLUMN termin_details.deliverables IS 'Expected deliverables for this phase';
COMMENT ON COLUMN termin_details.status IS 'Current review/approval status';
