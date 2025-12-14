-- V8__create_receiving_records_table.sql
-- Create receiving_records table for goods/service receiving

CREATE TABLE IF NOT EXISTS receiving_records (
    id UUID PRIMARY KEY,
    po_id UUID NOT NULL,
    received_quantity INTEGER,
    condition VARCHAR(50),
    status VARCHAR(30) NOT NULL 
        CHECK (status IN ('ACCEPTED', 'REJECTED', 'RETURNED', 'PARTIALLY_ACCEPTED')),
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    received_by UUID NOT NULL,
    notes TEXT,
    rejection_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_receiving_po FOREIGN KEY (po_id) 
        REFERENCES po_headers(id)
);

-- Create indexes
CREATE INDEX idx_receiving_po_id ON receiving_records(po_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_receiving_status ON receiving_records(status) WHERE is_deleted = FALSE;
CREATE INDEX idx_receiving_date ON receiving_records(received_at DESC) WHERE is_deleted = FALSE;
CREATE INDEX idx_receiving_by ON receiving_records(received_by) WHERE is_deleted = FALSE;

-- Comments
COMMENT ON TABLE receiving_records IS 'Records of goods/service receiving activities';
COMMENT ON COLUMN receiving_records.received_quantity IS 'Quantity actually received';
COMMENT ON COLUMN receiving_records.condition IS 'Condition of goods: GOOD, DAMAGED, INCOMPLETE';
COMMENT ON COLUMN receiving_records.status IS 'Receiving decision status';
COMMENT ON COLUMN receiving_records.received_by IS 'Operator who performed receiving';
COMMENT ON COLUMN receiving_records.rejection_reason IS 'Reason if status is REJECTED or RETURNED';
