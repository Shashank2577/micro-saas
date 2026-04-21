CREATE TABLE feature_flags (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    rules JSONB,
    enabled BOOLEAN NOT NULL,
    rollout_pct INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE flag_evaluations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    flag_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    result BOOLEAN NOT NULL,
    context JSONB,
    evaluated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE rollout_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    flag_id UUID NOT NULL,
    error_rate DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE flag_segments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    flag_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    conditions JSONB NOT NULL
);

CREATE TABLE flag_audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    flag_id UUID NOT NULL,
    action VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);
