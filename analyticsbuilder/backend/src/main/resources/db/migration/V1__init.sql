CREATE TABLE dashboards (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    layout_config JSONB,
    white_label_config JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255)
);

CREATE TABLE dashboard_widgets (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID NOT NULL REFERENCES dashboards(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255),
    config JSONB,
    position_x INT,
    position_y INT,
    width INT,
    height INT,
    query_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE dashboard_queries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID NOT NULL REFERENCES dashboards(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    data_source_type VARCHAR(50) NOT NULL,
    query_string TEXT NOT NULL,
    parameters JSONB,
    cache_ttl_seconds INT DEFAULT 300,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE sharing_permissions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID NOT NULL REFERENCES dashboards(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE scheduled_reports (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID NOT NULL REFERENCES dashboards(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    format VARCHAR(20) NOT NULL,
    recipients JSONB NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE report_executions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    scheduled_report_id UUID NOT NULL REFERENCES scheduled_reports(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    error_message TEXT,
    executed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_dashboards_tenant ON dashboards(tenant_id);
CREATE INDEX idx_widgets_dashboard ON dashboard_widgets(dashboard_id);
CREATE INDEX idx_queries_dashboard ON dashboard_queries(dashboard_id);
CREATE INDEX idx_sharing_dashboard ON sharing_permissions(dashboard_id);
CREATE INDEX idx_sharing_user ON sharing_permissions(user_id);
