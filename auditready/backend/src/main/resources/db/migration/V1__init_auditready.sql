CREATE TABLE control_framework (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE control (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    framework_id UUID NOT NULL REFERENCES control_framework(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE evidence (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES control(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    file_url VARCHAR(255),
    collected_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE compliance_gap (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES control(id),
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE audit_report (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    framework_id UUID NOT NULL REFERENCES control_framework(id),
    report_name VARCHAR(255) NOT NULL,
    readiness_score DOUBLE PRECISION NOT NULL,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    summary TEXT
);

CREATE TABLE remediation_workflow (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    gap_id UUID NOT NULL REFERENCES compliance_gap(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assigned_to VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE audit_trail (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    entity_type VARCHAR(255) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(255) NOT NULL,
    performed_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);
