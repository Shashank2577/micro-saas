CREATE TABLE headcount_plan (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    fiscal_year VARCHAR(10) NOT NULL,
    scenario_type VARCHAR(50) NOT NULL,
    total_headcount INT NOT NULL,
    total_cost DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE headcount_line (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    plan_id UUID NOT NULL REFERENCES headcount_plan(id),
    department VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    current_count INT NOT NULL,
    planned_count INT NOT NULL,
    avg_cost DECIMAL(19, 2) NOT NULL,
    start_quarter VARCHAR(10) NOT NULL
);

CREATE TABLE org_health_metric (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    period VARCHAR(50) NOT NULL,
    span_of_control_avg DECIMAL(10, 2) NOT NULL,
    management_ratio DECIMAL(10, 2) NOT NULL,
    turnover_rate DECIMAL(10, 2) NOT NULL,
    time_to_fill_avg DECIMAL(10, 2) NOT NULL,
    computed_at TIMESTAMP NOT NULL
);

CREATE TABLE skills_gap (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    department VARCHAR(255) NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    current_coverage DECIMAL(10, 2) NOT NULL,
    required_coverage DECIMAL(10, 2) NOT NULL,
    gap_severity VARCHAR(50) NOT NULL,
    identified_at TIMESTAMP NOT NULL
);

CREATE TABLE workforce_scenario (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    baseline_headcount INT NOT NULL,
    projected_headcount INT NOT NULL,
    burn_rate_monthly DECIMAL(19, 2) NOT NULL,
    runway_months DECIMAL(10, 2) NOT NULL,
    assumptions JSONB NOT NULL
);
