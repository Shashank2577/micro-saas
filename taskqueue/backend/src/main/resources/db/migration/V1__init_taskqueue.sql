CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payload JSONB,
    result JSONB,
    max_retries INT DEFAULT 3,
    retry_count INT DEFAULT 0,
    next_run_at TIMESTAMP,
    timeout_seconds INT DEFAULT 3600,
    depends_on_job_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX idx_jobs_tenant_status_next_run ON jobs(tenant_id, status, next_run_at);
CREATE INDEX idx_jobs_tenant_depends ON jobs(tenant_id, depends_on_job_id);

CREATE TABLE job_history (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_job_history_job_id ON job_history(job_id);

CREATE TABLE scheduled_tasks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(255) NOT NULL,
    job_name VARCHAR(255) NOT NULL,
    payload_template JSONB,
    priority VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_scheduled_tasks_tenant ON scheduled_tasks(tenant_id);
