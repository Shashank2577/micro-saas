CREATE TABLE IF NOT EXISTS tenant.workflow_runs (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id  UUID         NOT NULL REFERENCES tenant.workflows (id),
    triggered_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ,
    status       VARCHAR(50)  NOT NULL DEFAULT 'RUNNING',
    step_results JSONB,
    error_message TEXT
);

CREATE INDEX idx_workflow_runs_workflow ON tenant.workflow_runs (workflow_id);
CREATE INDEX idx_workflow_runs_status  ON tenant.workflow_runs (status);
