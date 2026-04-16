CREATE TABLE voice_models (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    owner_id UUID NOT NULL,
    corpus_size INT DEFAULT 0,
    style_attributes JSONB,
    trained_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_voice_models_tenant ON voice_models(tenant_id);

CREATE TABLE corpus_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    voice_model_id UUID NOT NULL REFERENCES voice_models(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    word_count INT DEFAULT 0,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_corpus_items_tenant_model ON corpus_items(tenant_id, voice_model_id);

CREATE TABLE writing_sessions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    voice_model_id UUID REFERENCES voice_models(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    topic TEXT NOT NULL,
    target_word_count INT DEFAULT 0,
    current_draft TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_writing_sessions_tenant ON writing_sessions(tenant_id);

CREATE TABLE research_notes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    session_id UUID NOT NULL REFERENCES writing_sessions(id) ON DELETE CASCADE,
    source_url TEXT,
    excerpt TEXT NOT NULL,
    citation TEXT,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_research_notes_tenant_session ON research_notes(tenant_id, session_id);

CREATE TABLE draft_sections (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    session_id UUID NOT NULL REFERENCES writing_sessions(id) ON DELETE CASCADE,
    heading TEXT NOT NULL,
    content TEXT,
    word_count INT DEFAULT 0,
    section_order INT NOT NULL
);

CREATE INDEX idx_draft_sections_tenant_session ON draft_sections(tenant_id, session_id);
