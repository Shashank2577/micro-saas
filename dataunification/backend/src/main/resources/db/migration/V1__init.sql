CREATE TABLE data_source (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- e.g., DATABASE, API, FILE
    connection_details JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_ds_tenant ON data_source(tenant_id);

CREATE TABLE schema_mapping (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    source_id UUID REFERENCES data_source(id),
    mapping_rules JSONB, -- stores visual mappings
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sm_tenant ON schema_mapping(tenant_id);

CREATE TABLE sync_job (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    mapping_id UUID REFERENCES schema_mapping(id),
    status VARCHAR(50) NOT NULL, -- PENDING, RUNNING, COMPLETED, FAILED
    type VARCHAR(50) NOT NULL, -- BATCH, REALTIME
    records_processed INT DEFAULT 0,
    error_log TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sj_tenant ON sync_job(tenant_id);

CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID REFERENCES sync_job(id),
    record_id VARCHAR(255),
    action VARCHAR(50), -- CREATE, UPDATE, DELETE, CONFLICT_RESOLVED
    details JSONB,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_al_tenant ON audit_log(tenant_id);
