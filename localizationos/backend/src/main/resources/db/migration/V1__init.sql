CREATE TABLE translation_projects (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name TEXT NOT NULL,
    source_language TEXT NOT NULL,
    target_languages JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE translation_memories (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES translation_projects(id),
    source_language TEXT NOT NULL,
    target_language TEXT NOT NULL,
    source_text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    usage_count INT DEFAULT 0
);

CREATE TABLE translation_jobs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES translation_projects(id),
    source_content TEXT NOT NULL,
    source_language TEXT NOT NULL,
    target_language TEXT NOT NULL,
    translated_content TEXT,
    ai_confidence DECIMAL(5, 2),
    cultural_flags JSONB,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cultural_flags (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID NOT NULL REFERENCES translation_jobs(id),
    phrase TEXT NOT NULL,
    issue_type VARCHAR(50) NOT NULL,
    market TEXT NOT NULL,
    suggestion TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL
);
