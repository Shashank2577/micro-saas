CREATE SCHEMA IF NOT EXISTS complianceradar;

CREATE TABLE complianceradar.regulatory_changes (
    id UUID PRIMARY KEY,
    jurisdiction VARCHAR(255) NOT NULL,
    regulation_name VARCHAR(255) NOT NULL,
    summary TEXT,
    effective_date DATE,
    impact_level VARCHAR(50),
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE complianceradar.compliance_policies (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    owner VARCHAR(255),
    last_reviewed_at TIMESTAMP WITH TIME ZONE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE complianceradar.compliance_gaps (
    id UUID PRIMARY KEY,
    regulatory_change_id UUID NOT NULL REFERENCES complianceradar.regulatory_changes(id),
    policy_id UUID REFERENCES complianceradar.compliance_policies(id),
    gap_description TEXT,
    severity VARCHAR(50),
    status VARCHAR(50),
    owner VARCHAR(255),
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE complianceradar.compliance_tasks (
    id UUID PRIMARY KEY,
    gap_id UUID NOT NULL REFERENCES complianceradar.compliance_gaps(id),
    description TEXT,
    due_date DATE,
    assigned_to VARCHAR(255),
    completed_at TIMESTAMP WITH TIME ZONE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
