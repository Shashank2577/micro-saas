CREATE TABLE event_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    system_type VARCHAR(50) NOT NULL,
    case_id VARCHAR(100) NOT NULL,
    activity_name VARCHAR(255) NOT NULL,
    actor_id VARCHAR(100),
    timestamp TIMESTAMP NOT NULL,
    attributes JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_event_logs_tenant_case ON event_logs(tenant_id, case_id);
CREATE INDEX idx_event_logs_tenant_sys ON event_logs(tenant_id, system_type);

CREATE TABLE process_models (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    system_type VARCHAR(50) NOT NULL,
    bpmn_xml TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE analysis_results (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(20),
    description TEXT,
    details JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    name VARCHAR(255) NOT NULL,
    rule_definition JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE automation_opportunities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    activity_name VARCHAR(255) NOT NULL,
    estimated_roi DECIMAL(10, 2),
    effort_estimate VARCHAR(50),
    rationale TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
