CREATE SCHEMA IF NOT EXISTS retentionsignal;

CREATE TABLE retentionsignal.employee_profiles (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    tenure_months INT NOT NULL,
    comp_level VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE retentionsignal.retention_signals (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    signal_type VARCHAR(50) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE retentionsignal.flight_risk_scores (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    score INT NOT NULL,
    top_risk_factors TEXT,
    calculated_at TIMESTAMP NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE retentionsignal.retention_interventions (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    intervention_type VARCHAR(50) NOT NULL,
    recommended_action TEXT,
    outcome VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE INDEX idx_emp_profiles_tenant ON retentionsignal.employee_profiles(tenant_id);
CREATE INDEX idx_ret_signals_tenant ON retentionsignal.retention_signals(tenant_id);
CREATE INDEX idx_ret_signals_emp ON retentionsignal.retention_signals(employee_id);
CREATE INDEX idx_fr_scores_tenant ON retentionsignal.flight_risk_scores(tenant_id);
CREATE INDEX idx_fr_scores_emp ON retentionsignal.flight_risk_scores(employee_id);
CREATE INDEX idx_ret_interventions_tenant ON retentionsignal.retention_interventions(tenant_id);
CREATE INDEX idx_ret_interventions_emp ON retentionsignal.retention_interventions(employee_id);
