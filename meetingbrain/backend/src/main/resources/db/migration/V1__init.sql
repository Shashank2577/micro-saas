CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE meetings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,
    external_id VARCHAR(255),
    platform VARCHAR(50), -- Zoom, Google Meet, Slack
    status VARCHAR(50) NOT NULL, -- SCHEDULED, RECORDING, COMPLETED, ANALYZED
    summary TEXT,
    audio_file_path VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_meetings_tenant ON meetings(tenant_id);

CREATE TABLE transcript_lines (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    meeting_id UUID NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
    speaker VARCHAR(100),
    text TEXT NOT NULL,
    start_timestamp NUMERIC,
    end_timestamp NUMERIC,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_transcript_tenant_meeting ON transcript_lines(tenant_id, meeting_id);

CREATE TABLE decisions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    meeting_id UUID NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
    topic VARCHAR(255),
    description TEXT,
    decision_text TEXT NOT NULL,
    embedding vector(1536), -- ADA-002 dimension
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_decisions_tenant ON decisions(tenant_id);

CREATE TABLE action_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    meeting_id UUID NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    owner VARCHAR(255),
    due_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL, -- OPEN, IN_PROGRESS, COMPLETED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_action_items_tenant ON action_items(tenant_id);

CREATE TABLE open_questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    meeting_id UUID NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- OPEN, RESOLVED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_open_questions_tenant ON open_questions(tenant_id);

CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE project_links (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    entity_type VARCHAR(50) NOT NULL, -- MEETING, DECISION, ACTION_ITEM
    entity_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(tenant_id, project_id, entity_type, entity_id)
);
