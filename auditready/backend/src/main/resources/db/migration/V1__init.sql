CREATE SCHEMA IF NOT EXISTS auditready;

CREATE TABLE auditready.audit_frameworks (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    tenant_id UUID NOT NULL
);

CREATE TABLE auditready.controls (
    id UUID PRIMARY KEY,
    framework_id UUID NOT NULL REFERENCES auditready.audit_frameworks(id),
    control_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    evidence_requirements TEXT,
    tenant_id UUID NOT NULL
);

CREATE TABLE auditready.evidence_items (
    id UUID PRIMARY KEY,
    control_id UUID NOT NULL REFERENCES auditready.controls(id),
    source_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    collected_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE auditready.audit_readiness_scores (
    id UUID PRIMARY KEY,
    framework_id UUID NOT NULL REFERENCES auditready.audit_frameworks(id),
    score INT NOT NULL,
    controls_met INT NOT NULL,
    controls_missing INT NOT NULL,
    calculated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    tenant_id UUID NOT NULL
);

-- Seed Data (SOC2 Framework with 5 sample controls)
INSERT INTO auditready.audit_frameworks (id, name, description, tenant_id)
VALUES ('11111111-1111-1111-1111-111111111111', 'SOC2', 'System and Organization Controls 2', '00000000-0000-0000-0000-000000000000');

INSERT INTO auditready.controls (id, framework_id, control_id, title, description, evidence_requirements, tenant_id)
VALUES 
    ('22222222-2222-2222-2222-222222222221', '11111111-1111-1111-1111-111111111111', 'CC1.1', 'COSO Principle 1', 'The entity demonstrates a commitment to integrity and ethical values.', 'Code of conduct, evidence of annual training', '00000000-0000-0000-0000-000000000000'),
    ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'CC1.2', 'COSO Principle 2', 'The board of directors demonstrates independence from management.', 'Board meeting minutes, charter', '00000000-0000-0000-0000-000000000000'),
    ('22222222-2222-2222-2222-222222222223', '11111111-1111-1111-1111-111111111111', 'CC1.3', 'COSO Principle 3', 'Management establishes structures, reporting lines, and authorities.', 'Organizational chart, job descriptions', '00000000-0000-0000-0000-000000000000'),
    ('22222222-2222-2222-2222-222222222224', '11111111-1111-1111-1111-111111111111', 'CC1.4', 'COSO Principle 4', 'The entity demonstrates a commitment to attract, develop, and retain competent individuals.', 'Performance review process, training programs', '00000000-0000-0000-0000-000000000000'),
    ('22222222-2222-2222-2222-222222222225', '11111111-1111-1111-1111-111111111111', 'CC1.5', 'COSO Principle 5', 'The entity holds individuals accountable for their internal control responsibilities.', 'Disciplinary policies, performance evaluations', '00000000-0000-0000-0000-000000000000');
