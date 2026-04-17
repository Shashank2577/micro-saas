CREATE TABLE data_sources (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    encrypted_connection TEXT,
    last_synced_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_data_sources_tenant ON data_sources(tenant_id);

CREATE TABLE datasets (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    data_source_id UUID NOT NULL REFERENCES data_sources(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    sql_query TEXT NOT NULL,
    schema_json JSONB,
    refresh_cadence VARCHAR(255),
    owner_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_datasets_tenant ON datasets(tenant_id);

CREATE TABLE narrative_templates (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    prompt_template TEXT NOT NULL,
    output_schema JSONB,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_narrative_templates_tenant ON narrative_templates(tenant_id);

CREATE TABLE narrative_reports (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dataset_id UUID NOT NULL REFERENCES datasets(id) ON DELETE CASCADE,
    template_id UUID NOT NULL REFERENCES narrative_templates(id),
    title VARCHAR(255) NOT NULL,
    content_markdown TEXT,
    time_range_start TIMESTAMP WITH TIME ZONE,
    time_range_end TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    generated_at TIMESTAMP WITH TIME ZONE,
    token_usage INT,
    model VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_narrative_reports_tenant ON narrative_reports(tenant_id);

CREATE TABLE insights (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    report_id UUID NOT NULL REFERENCES narrative_reports(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    headline VARCHAR(255) NOT NULL,
    description TEXT,
    evidence_json JSONB,
    severity VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_insights_tenant ON insights(tenant_id);

CREATE TABLE scheduled_deliveries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    report_template_id UUID NOT NULL REFERENCES narrative_templates(id) ON DELETE CASCADE,
    cron_expr VARCHAR(255) NOT NULL,
    recipients_json JSONB NOT NULL,
    channel VARCHAR(50) NOT NULL,
    last_delivered_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_scheduled_deliveries_tenant ON scheduled_deliveries(tenant_id);

CREATE TABLE feedbacks (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    report_id UUID NOT NULL REFERENCES narrative_reports(id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    user_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_feedbacks_tenant ON feedbacks(tenant_id);

CREATE TABLE ai_usages (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    feature VARCHAR(255) NOT NULL,
    prompt_tokens INT NOT NULL,
    completion_tokens INT NOT NULL,
    cost_usd NUMERIC(10, 6) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_ai_usages_tenant ON ai_usages(tenant_id);
