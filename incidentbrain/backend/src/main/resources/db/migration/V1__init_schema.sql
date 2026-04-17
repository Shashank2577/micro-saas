CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE incidents (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    severity VARCHAR(50),
    status VARCHAR(50),
    root_cause_hypothesis TEXT,
    confidence_score DOUBLE PRECISION,
    timeline_events JSONB,
    similar_incidents JSONB,
    postmortem_draft TEXT,
    tenant_id VARCHAR(36) NOT NULL,
    embedding vector(1536)
);

CREATE INDEX idx_incidents_tenant_id ON incidents(tenant_id);
