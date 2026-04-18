CREATE TABLE observability_signal (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    signal_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    trace_id VARCHAR(255),
    span_id VARCHAR(255),
    payload JSONB NOT NULL,
    severity VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_obs_signal_tenant ON observability_signal(tenant_id);
CREATE INDEX idx_obs_signal_timestamp ON observability_signal(timestamp);
CREATE INDEX idx_obs_signal_trace ON observability_signal(trace_id);

CREATE TABLE observability_alert (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    ai_analysis JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_obs_alert_tenant ON observability_alert(tenant_id);
