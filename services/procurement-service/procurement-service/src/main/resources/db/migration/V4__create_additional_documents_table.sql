-- V4__create_additional_documents_table.sql
-- Create additional_documents table for supporting documents

CREATE TABLE IF NOT EXISTS additional_documents (
    id UUID PRIMARY KEY,
    procurement_request_id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    file_url TEXT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_document_procurement_request FOREIGN KEY (procurement_request_id) 
        REFERENCES procurement_requests(id) ON DELETE CASCADE
);

-- Create index
CREATE INDEX idx_document_pr_id ON additional_documents(procurement_request_id);
CREATE INDEX idx_document_uploaded ON additional_documents(uploaded_at DESC);

-- Comments
COMMENT ON TABLE additional_documents IS 'Supporting documents attached to procurement requests';
COMMENT ON COLUMN additional_documents.file_url IS 'URL or path to stored document file';
COMMENT ON COLUMN additional_documents.file_size IS 'File size in bytes';
