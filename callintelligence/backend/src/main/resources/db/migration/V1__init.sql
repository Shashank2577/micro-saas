CREATE TABLE calls (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    audio_url VARCHAR(1024),
    duration_seconds INTEGER,
    summary TEXT,
    rep_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_speakers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    speaker_label VARCHAR(255) NOT NULL,
    speaker_name VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    talk_time_seconds INTEGER,
    talk_ratio NUMERIC(5,2),
    longest_monologue_seconds INTEGER,
    sentiment_score NUMERIC(5,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_transcripts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    speaker_id UUID REFERENCES call_speakers(id),
    start_time NUMERIC(10,2) NOT NULL,
    end_time NUMERIC(10,2) NOT NULL,
    text TEXT NOT NULL,
    sentiment VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    transcript_id UUID REFERENCES call_transcripts(id),
    insight_type VARCHAR(50) NOT NULL,
    category VARCHAR(255),
    description TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE coaching_recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    rep_id VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    recommendation TEXT NOT NULL,
    playbook_suggestion TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- New tables for expansion constraints --
CREATE TABLE call_participants (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    participant_role VARCHAR(50),
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sentiment_analysis (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    speaker_id UUID REFERENCES call_speakers(id),
    overall_score NUMERIC(5,2) NOT NULL,
    positivity_ratio NUMERIC(5,2),
    negativity_ratio NUMERIC(5,2),
    analyzed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE action_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id),
    description TEXT NOT NULL,
    assignee_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
