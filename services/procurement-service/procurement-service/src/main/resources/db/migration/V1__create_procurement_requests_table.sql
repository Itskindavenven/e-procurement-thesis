-- V1__create_procurement_requests_table.sql
-- Create procurement_requests table with all relationships

CREATE TABLE IF NOT EXISTS procurement_requests (
    id UUID PRIMARY KEY,
    operator_id UUID NOT NULL,
    vendor_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('GOODS', 'SERVICE')),
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL' CHECK (priority IN ('NORMAL', 'IMPORTANT', 'URGENT')),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED', 'RETURNED', 'ESCALATED', 'CANCELLED')),
    description TEXT,
    deadline TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_pr_operator_id ON procurement_requests(operator_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_pr_status ON procurement_requests(status) WHERE is_deleted = FALSE;
CREATE INDEX idx_pr_vendor_id ON procurement_requests(vendor_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_pr_created_at ON procurement_requests(created_at DESC) WHERE is_deleted = FALSE;
CREATE INDEX idx_pr_operator_status ON procurement_requests(operator_id, status) WHERE is_deleted = FALSE;

-- Comments for documentation
COMMENT ON TABLE procurement_requests IS 'Core procurement request entity for both goods and services';
COMMENT ON COLUMN procurement_requests.type IS 'Type of procurement: GOODS or SERVICE';
COMMENT ON COLUMN procurement_requests.priority IS 'Priority level: NORMAL, IMPORTANT, or URGENT';
COMMENT ON COLUMN procurement_requests.status IS 'Current workflow status';
COMMENT ON COLUMN procurement_requests.is_deleted IS 'Soft delete flag';
