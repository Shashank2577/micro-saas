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
    embedding vector(1536) -- For similarity search later
);
