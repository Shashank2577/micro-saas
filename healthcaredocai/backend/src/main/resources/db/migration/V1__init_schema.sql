CREATE TABLE clinical_encounters (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    clinician_id VARCHAR(255) NOT NULL,
    transcript TEXT,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clinical_encounters_tenant ON clinical_encounters(tenant_id);

CREATE TABLE generated_notes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    encounter_id UUID NOT NULL REFERENCES clinical_encounters(id) ON DELETE CASCADE,
    note_type VARCHAR(50) NOT NULL,
    content TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_generated_notes_tenant ON generated_notes(tenant_id);
CREATE INDEX idx_generated_notes_encounter ON generated_notes(encounter_id);

CREATE TABLE clinical_templates (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    note_type VARCHAR(50) NOT NULL,
    template_format TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clinical_templates_tenant ON clinical_templates(tenant_id);
CREATE INDEX idx_clinical_templates_specialty ON clinical_templates(tenant_id, specialty);
