CREATE TABLE deployed_apps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    deployed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_deployed_apps_tenant ON deployed_apps(tenant_id);

CREATE TABLE data_flows (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_app_id UUID NOT NULL REFERENCES deployed_apps(id),
    target_app_id UUID NOT NULL REFERENCES deployed_apps(id),
    event_type VARCHAR(255) NOT NULL,
    health_status VARCHAR(50) NOT NULL,
    last_event_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_data_flows_tenant ON data_flows(tenant_id);

CREATE TABLE value_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_value NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(10),
    calculated_at TIMESTAMP NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_value_metrics_tenant ON value_metrics(tenant_id);

CREATE TABLE integration_opportunities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_app VARCHAR(255) NOT NULL,
    target_app VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    potential_value TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_integration_opps_tenant ON integration_opportunities(tenant_id);
