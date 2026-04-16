-- V12__create_export_jobs.sql
-- ExportJob entity requires this table but it was never created in V0-V10.

CREATE TABLE IF NOT EXISTS cc.export_jobs (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id   UUID NOT NULL REFERENCES cc.tenants(id) ON DELETE CASCADE,
    resource_type VARCHAR(100) NOT NULL,
    format      VARCHAR(20) NOT NULL DEFAULT 'CSV',
    status      VARCHAR(20) NOT NULL DEFAULT 'pending',
    query       TEXT,
    columns     TEXT,
    result_url  TEXT,
    error_message TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ
);

CREATE INDEX idx_export_jobs_tenant ON cc.export_jobs (tenant_id, created_at DESC);
