CREATE SCHEMA IF NOT EXISTS securitypulse;

CREATE TABLE IF NOT EXISTS scan_jobs (
    id UUID PRIMARY KEY,
    pr_url VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    tenant_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS findings (
    id UUID PRIMARY KEY,
    scan_job_id UUID NOT NULL REFERENCES scan_jobs(id),
    pr_url VARCHAR(255) NOT NULL,
    tool VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS policies (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rule VARCHAR(255) NOT NULL,
    action VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS policy_decisions (
    id UUID PRIMARY KEY,
    scan_job_id UUID NOT NULL REFERENCES scan_jobs(id),
    decision VARCHAR(50) NOT NULL,
    reason TEXT,
    tenant_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS triaged_reports (
    id UUID PRIMARY KEY,
    scan_job_id UUID NOT NULL REFERENCES scan_jobs(id),
    summary TEXT,
    priority_level VARCHAR(50),
    tenant_id UUID NOT NULL
);
