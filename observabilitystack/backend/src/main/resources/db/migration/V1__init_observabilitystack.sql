CREATE TABLE health_checks (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_checked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alert_rules (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    condition VARCHAR(500) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    rule_id UUID REFERENCES alert_rules(id),
    status VARCHAR(50) NOT NULL,
    triggered_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alert_history (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    alert_id UUID REFERENCES alerts(id),
    status VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE incidents (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP
);

CREATE TABLE incident_timelines (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    incident_id UUID REFERENCES incidents(id),
    message TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE runbooks (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE slos (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_percentage DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE slo_compliance (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    slo_id UUID REFERENCES slos(id),
    compliance_percentage DOUBLE PRECISION NOT NULL,
    measured_at TIMESTAMP NOT NULL
);

CREATE TABLE oncall_schedules (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE dashboards (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE dashboard_queries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID REFERENCES dashboards(id),
    query_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
