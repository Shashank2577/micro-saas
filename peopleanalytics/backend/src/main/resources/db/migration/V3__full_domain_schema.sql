-- Initial schema for PeopleAnalytics
CREATE SCHEMA IF NOT EXISTS peopleanalytics;

CREATE TABLE IF NOT EXISTS employees (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    external_id VARCHAR(255),
    first_name BYTEA, -- Encrypted
    last_name BYTEA,  -- Encrypted
    email BYTEA,      -- Encrypted
    department VARCHAR(255),
    role VARCHAR(255),
    manager_id UUID,
    hire_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_employees_tenant ON employees(tenant_id);

CREATE TABLE IF NOT EXISTS performance_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    metric_type VARCHAR(50) NOT NULL,
    value DOUBLE PRECISION,
    target DOUBLE PRECISION,
    period VARCHAR(50),
    metric_date DATE,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_performance_metrics_tenant ON performance_metrics(tenant_id);
CREATE INDEX idx_performance_metrics_employee ON performance_metrics(employee_id);

CREATE TABLE IF NOT EXISTS engagement_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    score DOUBLE PRECISION NOT NULL,
    source VARCHAR(50),
    calculated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_engagement_scores_tenant ON engagement_scores(tenant_id);

CREATE TABLE IF NOT EXISTS pulse_surveys (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_pulse_surveys_tenant ON pulse_surveys(tenant_id);

CREATE TABLE IF NOT EXISTS survey_responses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    survey_id UUID NOT NULL REFERENCES pulse_surveys(id),
    employee_id UUID NOT NULL REFERENCES employees(id),
    score INTEGER,
    feedback BYTEA, -- Encrypted
    submitted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_survey_responses_tenant ON survey_responses(tenant_id);

CREATE TABLE IF NOT EXISTS team_health_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    department VARCHAR(255),
    team_id VARCHAR(255),
    collaboration_score DOUBLE PRECISION,
    productivity_signal DOUBLE PRECISION,
    measured_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_team_health_metrics_tenant ON team_health_metrics(tenant_id);

CREATE TABLE IF NOT EXISTS retention_predictions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    risk_score DOUBLE PRECISION,
    risk_level VARCHAR(50),
    factors JSONB,
    predicted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_retention_predictions_tenant ON retention_predictions(tenant_id);

CREATE TABLE IF NOT EXISTS analytics_cache (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    cache_key VARCHAR(255) NOT NULL,
    cache_value JSONB,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_analytics_cache_tenant ON analytics_cache(tenant_id);
CREATE UNIQUE INDEX idx_analytics_cache_key ON analytics_cache(tenant_id, cache_key);
