-- V7__create_po_tables.sql
-- Create purchase order related tables

-- PO Header table
CREATE TABLE IF NOT EXISTS po_headers (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL UNIQUE,
    po_number VARCHAR(50) NOT NULL UNIQUE,
    vendor_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED' 
        CHECK (status IN ('CREATED', 'IN_DELIVERY', 'DELIVERED', 'COMPLETED', 'CANCELLED')),
    total_amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_po_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id)
);

-- Create indexes for PO Header
CREATE INDEX idx_po_pr_id ON po_headers(procurement_request_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_po_number ON po_headers(po_number) WHERE is_deleted = FALSE;
CREATE INDEX idx_po_vendor ON po_headers(vendor_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_po_status ON po_headers(status) WHERE is_deleted = FALSE;
CREATE INDEX idx_po_created ON po_headers(created_at DESC) WHERE is_deleted = FALSE;

-- PO Items table
CREATE TABLE IF NOT EXISTS po_items (
    id UUID PRIMARY KEY,
    po_id UUID NOT NULL,
    item_id UUID,
    item_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(15, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal DECIMAL(15, 2) NOT NULL,
    CONSTRAINT fk_po_item_po_header FOREIGN KEY (po_id) 
        REFERENCES po_headers(id) ON DELETE CASCADE
);

-- Create indexes for PO Items
CREATE INDEX idx_po_item_po_id ON po_items(po_id);
CREATE INDEX idx_po_item_item_id ON po_items(item_id);

-- Comments
COMMENT ON TABLE po_headers IS 'Purchase order headers created from approved PRs';
COMMENT ON COLUMN po_headers.po_number IS 'Unique PO number for tracking';
COMMENT ON COLUMN po_headers.status IS 'PO fulfillment status';

COMMENT ON TABLE po_items IS 'Line items in purchase orders';
COMMENT ON COLUMN po_items.item_id IS 'Reference to original procurement item';
