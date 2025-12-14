-- V3__create_delivery_details_table.sql
-- Create delivery_details table

CREATE TABLE IF NOT EXISTS delivery_details (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL,
    delivery_address TEXT NOT NULL,
    location_id UUID,
    planned_delivery_date DATE,
    notes TEXT,
    CONSTRAINT fk_delivery_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id) ON DELETE CASCADE
);

-- Create index
CREATE INDEX idx_delivery_pr_id ON delivery_details(procurement_request_id);
CREATE INDEX idx_delivery_location ON delivery_details(location_id);

-- Comments
COMMENT ON TABLE delivery_details IS 'Delivery information for procurement requests';
COMMENT ON COLUMN delivery_details.location_id IS 'Reference to location in master data service';
