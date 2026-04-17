CREATE TABLE data_retention_policy (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    data_type VARCHAR(255) NOT NULL,
    retention_days INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE data_subject_request (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    subject_email VARCHAR(255) NOT NULL,
    request_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
);

CREATE TABLE pii_record (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_table VARCHAR(255) NOT NULL,
    source_column VARCHAR(255) NOT NULL,
    pii_type VARCHAR(100) NOT NULL,
    is_masked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE consent_record (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    processing_purpose VARCHAR(255) NOT NULL,
    is_granted BOOLEAN NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE data_lineage_node (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    origin_service VARCHAR(255) NOT NULL,
    current_service VARCHAR(255) NOT NULL,
    transformation_logic TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    resource VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    details TEXT
);
