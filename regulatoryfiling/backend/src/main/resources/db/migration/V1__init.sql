CREATE SCHEMA IF NOT EXISTS regulatoryfiling;

CREATE TABLE regulatoryfiling.filing_obligations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    jurisdiction VARCHAR(255) NOT NULL,
    filing_type VARCHAR(255) NOT NULL,
    due_date DATE NOT NULL,
    recurrence_pattern VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE regulatoryfiling.filing_drafts (
    id UUID PRIMARY KEY,
    obligation_id UUID NOT NULL,
    content TEXT,
    generated_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE regulatoryfiling.filing_alerts (
    id UUID PRIMARY KEY,
    obligation_id UUID NOT NULL,
    alert_date DATE NOT NULL,
    days_until_due INT NOT NULL,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);
