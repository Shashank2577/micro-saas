CREATE TABLE documents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    folder_id UUID,
    current_version_id UUID,
    status VARCHAR(50) NOT NULL, -- ACTIVE, ARCHIVED, DELETED
    retention_hold BOOLEAN DEFAULT FALSE,
    expiration_date TIMESTAMP,
    owner_id UUID NOT NULL,
    size_bytes BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    encryption_key_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_by UUID
);
CREATE INDEX idx_documents_tenant_id ON documents(tenant_id);

CREATE TABLE document_versions (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL REFERENCES documents(id),
    tenant_id UUID NOT NULL,
    version_number INT NOT NULL,
    s3_key VARCHAR(1024) NOT NULL,
    size_bytes BIGINT NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    ocr_text TEXT,
    created_at TIMESTAMP NOT NULL,
    created_by UUID
);
CREATE INDEX idx_document_versions_tenant_id ON document_versions(tenant_id);

CREATE TABLE document_shares (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL REFERENCES documents(id),
    tenant_id UUID NOT NULL,
    shared_with_email VARCHAR(255) NOT NULL,
    access_level VARCHAR(50) NOT NULL, -- VIEW, EDIT
    password_hash VARCHAR(255),
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    created_by UUID
);
CREATE INDEX idx_document_shares_tenant_id ON document_shares(tenant_id);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id),
    user_id UUID NOT NULL,
    action VARCHAR(100) NOT NULL, -- UPLOAD, VIEW, DOWNLOAD, DELETE, SHARE, VERSION_ROLLBACK
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_audit_logs_tenant_id ON audit_logs(tenant_id);

CREATE TABLE storage_quotas (
    tenant_id UUID PRIMARY KEY,
    total_quota_bytes BIGINT NOT NULL,
    used_bytes BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
