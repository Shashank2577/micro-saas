CREATE TABLE IF NOT EXISTS framework (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_framework_tenant ON framework(tenant_id);

CREATE TABLE IF NOT EXISTS control (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    framework_id UUID NOT NULL REFERENCES framework(id) ON DELETE CASCADE,
    control_code VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    evidence_requirements TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_control_framework ON control(framework_id);
CREATE INDEX idx_control_tenant ON control(tenant_id);

CREATE TABLE IF NOT EXISTS evidence (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_app VARCHAR(255) NOT NULL,
    evidence_type VARCHAR(100) NOT NULL,
    content TEXT,
    url VARCHAR(1024),
    status VARCHAR(50) NOT NULL,
    collected_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_evidence_tenant ON evidence(tenant_id);

CREATE TABLE IF NOT EXISTS evidence_mapping (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    evidence_id UUID NOT NULL REFERENCES evidence(id) ON DELETE CASCADE,
    control_id UUID NOT NULL REFERENCES control(id) ON DELETE CASCADE,
    ai_confidence_score DOUBLE PRECISION,
    ai_rationale TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_evidence_mapping_evidence ON evidence_mapping(evidence_id);
CREATE INDEX idx_evidence_mapping_control ON evidence_mapping(control_id);
CREATE INDEX idx_evidence_mapping_tenant ON evidence_mapping(tenant_id);

CREATE TABLE IF NOT EXISTS audit_package (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    framework_id UUID NOT NULL REFERENCES framework(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    download_url VARCHAR(1024),
    generated_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_package_tenant ON audit_package(tenant_id);
CREATE INDEX idx_audit_package_framework ON audit_package(framework_id);


CREATE TABLE IF NOT EXISTS framework_requirement (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    framework_id UUID NOT NULL REFERENCES framework(id) ON DELETE CASCADE,
    requirement_code VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_framework_requirement_tenant ON framework_requirement(tenant_id);

CREATE TABLE IF NOT EXISTS evidence_request (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES control(id) ON DELETE CASCADE,
    requested_by VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_evidence_request_tenant ON evidence_request(tenant_id);

CREATE TABLE IF NOT EXISTS compliance_finding (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    control_id UUID NOT NULL REFERENCES control(id) ON DELETE CASCADE,
    severity VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_compliance_finding_tenant ON compliance_finding(tenant_id);
