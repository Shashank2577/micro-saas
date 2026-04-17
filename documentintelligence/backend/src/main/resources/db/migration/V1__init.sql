CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE documents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    storage_path VARCHAR(1024) NOT NULL,
    status VARCHAR(50) NOT NULL,
    document_type VARCHAR(100),
    size_bytes BIGINT NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);
CREATE INDEX idx_documents_tenant ON documents(tenant_id);

CREATE TABLE document_extractions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    extraction_type VARCHAR(100) NOT NULL,
    key_name VARCHAR(255) NOT NULL,
    value_text TEXT,
    confidence_score DECIMAL(5,4),
    page_number INTEGER,
    bounding_box JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_extractions_tenant_doc ON document_extractions(tenant_id, document_id);

CREATE TABLE document_chunks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    page_number INTEGER,
    embedding vector(1536),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_chunks_tenant_doc ON document_chunks(tenant_id, document_id);

CREATE TABLE document_audit_trails (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    action VARCHAR(100) NOT NULL,
    details JSONB,
    performed_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_audit_tenant_doc ON document_audit_trails(tenant_id, document_id);
