CREATE TABLE brand_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    tone VARCHAR(255) NOT NULL,
    vocabulary_allowed JSONB,
    vocabulary_forbidden JSONB,
    core_values JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_brand_profiles_tenant_id ON brand_profiles(tenant_id);

CREATE TABLE content_audits (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brand_profiles(id),
    content_title VARCHAR(255) NOT NULL,
    content_body TEXT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_content_audits_tenant_id ON content_audits(tenant_id);

CREATE TABLE audit_results (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    content_audit_id UUID NOT NULL REFERENCES content_audits(id),
    consistency_score INTEGER NOT NULL,
    sentiment_alignment VARCHAR(255),
    summary_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_audit_results_tenant_id ON audit_results(tenant_id);

CREATE TABLE audit_findings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    audit_result_id UUID NOT NULL REFERENCES audit_results(id),
    finding_type VARCHAR(50) NOT NULL,
    original_text TEXT NOT NULL,
    suggested_rewrite TEXT,
    explanation TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_audit_findings_tenant_id ON audit_findings(tenant_id);
