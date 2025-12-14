-- V2__create_procurement_items_table.sql
-- Create procurement_items table for line items

CREATE TABLE IF NOT EXISTS procurement_items (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL,
    catalog_item_id UUID,
    item_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(15, 2) NOT NULL CHECK (unit_price >= 0),
    tax_ppn DECIMAL(15, 2),
    subtotal DECIMAL(15, 2),
    CONSTRAINT fk_item_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_item_pr_id ON procurement_items(procurement_request_id);
CREATE INDEX idx_item_catalog_id ON procurement_items(catalog_item_id);

-- Comments
COMMENT ON TABLE procurement_items IS 'Line items for procurement requests';
COMMENT ON COLUMN procurement_items.catalog_item_id IS 'Reference to catalog item in catalog service';
COMMENT ON COLUMN procurement_items.tax_ppn IS 'PPN (VAT) tax amount';
COMMENT ON COLUMN procurement_items.subtotal IS 'Calculated: quantity * unit_price';
