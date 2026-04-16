CREATE SCHEMA IF NOT EXISTS ndaflow;

CREATE TABLE ndaflow.ndas (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    counterparty VARCHAR(255) NOT NULL,
    nda_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    expires_at DATE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ndaflow.nda_clauses (
    id UUID PRIMARY KEY,
    nda_id UUID NOT NULL REFERENCES ndaflow.ndas(id) ON DELETE CASCADE,
    clause_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    is_negotiable BOOLEAN NOT NULL DEFAULT TRUE,
    tenant_id UUID NOT NULL
);

CREATE TABLE ndaflow.nda_redlines (
    id UUID PRIMARY KEY,
    nda_id UUID NOT NULL REFERENCES ndaflow.ndas(id) ON DELETE CASCADE,
    clause_id UUID NOT NULL REFERENCES ndaflow.nda_clauses(id) ON DELETE CASCADE,
    original_text TEXT NOT NULL,
    proposed_text TEXT NOT NULL,
    rationale TEXT,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE INDEX idx_ndas_tenant_id ON ndaflow.ndas(tenant_id);
CREATE INDEX idx_nda_clauses_tenant_id ON ndaflow.nda_clauses(tenant_id);
CREATE INDEX idx_nda_redlines_tenant_id ON ndaflow.nda_redlines(tenant_id);
CREATE INDEX idx_nda_clauses_nda_id ON ndaflow.nda_clauses(nda_id);
CREATE INDEX idx_nda_redlines_nda_id ON ndaflow.nda_redlines(nda_id);
CREATE INDEX idx_nda_redlines_clause_id ON ndaflow.nda_redlines(clause_id);
