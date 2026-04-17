CREATE TABLE IF NOT EXISTS tenant.ecosystem_events (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id  UUID         NOT NULL,
    source_app VARCHAR(100) NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    payload    JSONB        NOT NULL DEFAULT '{}',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ecosystem_events_tenant_type ON tenant.ecosystem_events (tenant_id, event_type);
CREATE INDEX idx_ecosystem_events_created    ON tenant.ecosystem_events (created_at DESC);
