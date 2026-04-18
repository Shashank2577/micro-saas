CREATE TABLE integrations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    provider VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    auth_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_integrations_tenant ON integrations(tenant_id);

CREATE TABLE api_credentials (
    id UUID PRIMARY KEY,
    integration_id UUID NOT NULL REFERENCES integrations(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    encrypted_token VARCHAR(2048),
    refresh_token VARCHAR(2048),
    token_expiry TIMESTAMP,
    username VARCHAR(255),
    encrypted_password VARCHAR(2048),
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_api_credentials_tenant ON api_credentials(tenant_id);

CREATE TABLE sync_jobs (
    id UUID PRIMARY KEY,
    integration_id UUID NOT NULL REFERENCES integrations(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    schedule_cron VARCHAR(255) NOT NULL,
    source_entity VARCHAR(255) NOT NULL,
    target_entity VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    last_run_at TIMESTAMP,
    next_run_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sync_jobs_tenant ON sync_jobs(tenant_id);

CREATE TABLE field_mappings (
    id UUID PRIMARY KEY,
    sync_job_id UUID NOT NULL REFERENCES sync_jobs(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    source_field VARCHAR(255) NOT NULL,
    target_field VARCHAR(255) NOT NULL,
    transformation_rule VARCHAR(1024)
);
CREATE INDEX idx_field_mappings_tenant ON field_mappings(tenant_id);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID NOT NULL REFERENCES integrations(id) ON DELETE CASCADE,
    action VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    records_processed INTEGER NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_audit_logs_tenant ON audit_logs(tenant_id);

CREATE TABLE synced_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID NOT NULL REFERENCES integrations(id) ON DELETE CASCADE,
    external_id VARCHAR(255) NOT NULL,
    data JSONB NOT NULL,
    synced_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_synced_records_tenant ON synced_records(tenant_id);
