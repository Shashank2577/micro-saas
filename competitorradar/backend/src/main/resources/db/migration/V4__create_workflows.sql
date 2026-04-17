CREATE TABLE IF NOT EXISTS tenant.workflows (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID         NOT NULL,
    name              VARCHAR(200) NOT NULL,
    description       TEXT,
    trigger_condition JSONB        NOT NULL DEFAULT '{}',
    steps             JSONB        NOT NULL DEFAULT '[]',
    enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
    last_run_at       TIMESTAMPTZ,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_workflows_tenant   ON tenant.workflows (tenant_id);
CREATE INDEX idx_workflows_enabled  ON tenant.workflows (tenant_id, enabled);
