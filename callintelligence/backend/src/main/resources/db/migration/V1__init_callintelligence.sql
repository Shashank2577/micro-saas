CREATE TABLE calls (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    audio_url VARCHAR(1024),
    duration_seconds INT,
    summary TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    rep_id VARCHAR(255)
);
CREATE INDEX idx_calls_tenant ON calls(tenant_id);
CREATE INDEX idx_calls_rep ON calls(rep_id);

CREATE TABLE call_speakers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    speaker_label VARCHAR(50) NOT NULL,
    speaker_name VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    talk_time_seconds INT DEFAULT 0,
    talk_ratio NUMERIC(5,2) DEFAULT 0.0,
    longest_monologue_seconds INT DEFAULT 0,
    sentiment_score NUMERIC(5,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_transcripts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    speaker_id UUID REFERENCES call_speakers(id),
    start_time NUMERIC(10, 2) NOT NULL,
    end_time NUMERIC(10, 2) NOT NULL,
    text TEXT NOT NULL,
    is_question BOOLEAN DEFAULT FALSE,
    question_type VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    insight_type VARCHAR(50) NOT NULL,
    category VARCHAR(100),
    description TEXT NOT NULL,
    transcript_id UUID REFERENCES call_transcripts(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE coaching_recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    rep_id VARCHAR(255),
    category VARCHAR(100),
    recommendation TEXT NOT NULL,
    playbook_suggestion TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
