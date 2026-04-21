CREATE TABLE IF NOT EXISTS tenant.ecosystem_apps (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id    UUID         NOT NULL,
    name         VARCHAR(100) NOT NULL,
    display_name VARCHAR(200),
    base_url     VARCHAR(500),
    manifest     JSONB        NOT NULL DEFAULT '{}',
    status       VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    last_heartbeat_at TIMESTAMPTZ,
    registered_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_ecosystem_apps_tenant_name UNIQUE (tenant_id, name)
);

CREATE INDEX idx_ecosystem_apps_tenant ON tenant.ecosystem_apps (tenant_id);
CREATE INDEX idx_ecosystem_apps_status ON tenant.ecosystem_apps (status);
