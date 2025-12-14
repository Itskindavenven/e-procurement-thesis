-- V5__create_approval_histories_table.sql
-- Create approval_histories table for audit trail

CREATE TABLE IF NOT EXISTS approval_histories (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL,
    approver_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('SUPERVISOR', 'FINANCE', 'MANAGER')),
    decision VARCHAR(20) NOT NULL CHECK (decision IN ('APPROVE', 'REJECT', 'RETURN', 'FEEDBACK_ONLY')),
    note TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_approval_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_approval_pr_id ON approval_histories(procurement_request_id);
CREATE INDEX idx_approval_approver ON approval_histories(approver_id);
CREATE INDEX idx_approval_timestamp ON approval_histories(timestamp DESC);
CREATE INDEX idx_approval_decision ON approval_histories(decision);

-- Comments
COMMENT ON TABLE approval_histories IS 'Audit trail of all approval actions';
COMMENT ON COLUMN approval_histories.role IS 'Role of the approver: SUPERVISOR, FINANCE, or MANAGER';
COMMENT ON COLUMN approval_histories.decision IS 'Approval decision made';
COMMENT ON COLUMN approval_histories.timestamp IS 'When the decision was made';
