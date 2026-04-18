CREATE TABLE events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    properties JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    dimensions JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE ai_insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    recommendation TEXT NOT NULL,
    data_references JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE cohorts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    criteria JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_events_tenant_time ON events(tenant_id, created_at);
CREATE INDEX idx_metrics_tenant_time ON metrics(tenant_id, created_at);
