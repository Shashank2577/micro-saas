CREATE TABLE employees (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_number VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    role VARCHAR(100),
    manager_id UUID,
    status VARCHAR(50) NOT NULL,
    hire_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_employees_tenant ON employees(tenant_id);

CREATE TABLE performance_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    metric_type VARCHAR(50) NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(10,2) NOT NULL,
    metric_date DATE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_perf_metrics_employee ON performance_metrics(employee_id);

CREATE TABLE engagement_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    score DECIMAL(5,2) NOT NULL,
    calculated_at TIMESTAMP NOT NULL,
    factors JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_eng_scores_employee ON engagement_scores(employee_id);

CREATE TABLE pulse_surveys (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    questions JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    distributed_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_pulse_surveys_tenant ON pulse_surveys(tenant_id);

CREATE TABLE survey_responses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    survey_id UUID NOT NULL REFERENCES pulse_surveys(id),
    employee_id UUID NOT NULL REFERENCES employees(id),
    responses JSONB NOT NULL,
    submitted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_survey_responses_survey ON survey_responses(survey_id);

CREATE TABLE team_health_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    department VARCHAR(100) NOT NULL,
    health_score DECIMAL(5,2) NOT NULL,
    collaboration_score DECIMAL(5,2) NOT NULL,
    productivity_score DECIMAL(5,2) NOT NULL,
    measured_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_team_health_dept ON team_health_metrics(tenant_id, department);

CREATE TABLE retention_predictions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    risk_score DECIMAL(5,2) NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    factors JSONB NOT NULL,
    predicted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE INDEX idx_retention_employee ON retention_predictions(employee_id);

CREATE TABLE analytics_cache (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    cache_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
CREATE UNIQUE INDEX idx_analytics_cache_key ON analytics_cache(tenant_id, cache_key);
