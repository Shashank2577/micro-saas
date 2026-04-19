CREATE SCHEMA IF NOT EXISTS complianceradar;

CREATE TABLE complianceradar.regulation_updates (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_regulation_updates_tenant_id ON complianceradar.regulation_updates(tenant_id);

CREATE TABLE complianceradar.jurisdiction_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_jurisdiction_rules_tenant_id ON complianceradar.jurisdiction_rules(tenant_id);

CREATE TABLE complianceradar.impact_assessments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_impact_assessments_tenant_id ON complianceradar.impact_assessments(tenant_id);

CREATE TABLE complianceradar.control_gaps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_control_gaps_tenant_id ON complianceradar.control_gaps(tenant_id);

CREATE TABLE complianceradar.task_assignments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_task_assignments_tenant_id ON complianceradar.task_assignments(tenant_id);

CREATE TABLE complianceradar.deadline_alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180),
    status VARCHAR(40),
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
CREATE INDEX idx_deadline_alerts_tenant_id ON complianceradar.deadline_alerts(tenant_id);
