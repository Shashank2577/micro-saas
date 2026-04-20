-- V1__init_videonarrator_schema.sql
CREATE TABLE video_projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    video_url TEXT NOT NULL,
    original_filename VARCHAR(255),
    status VARCHAR(50) NOT NULL, -- UPLOADED, TRANSCRIBING, NARRATING, SYNCING, READY, FAILED
    storage_path TEXT NOT NULL,
    duration_seconds INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_video_projects_tenant ON video_projects(tenant_id);

CREATE TABLE transcriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL REFERENCES video_projects(id) ON DELETE CASCADE,
    tenant_id VARCHAR(255) NOT NULL,
    language_code VARCHAR(10) NOT NULL,
    full_text TEXT,
    status VARCHAR(50) NOT NULL, -- PENDING, PROCESSING, COMPLETED, FAILED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subtitle_tracks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transcription_id UUID NOT NULL REFERENCES transcriptions(id) ON DELETE CASCADE,
    tenant_id VARCHAR(255) NOT NULL,
    start_time_ms BIGINT NOT NULL,
    end_time_ms BIGINT NOT NULL,
    content TEXT NOT NULL,
    sequence_order INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE narration_tracks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL REFERENCES video_projects(id) ON DELETE CASCADE,
    tenant_id VARCHAR(255) NOT NULL,
    voice_provider VARCHAR(50) NOT NULL, -- GOOGLE, OPENAI, ELEVENLABS
    voice_id VARCHAR(100) NOT NULL,
    audio_url TEXT,
    status VARCHAR(50) NOT NULL, -- PENDING, GENERATING, COMPLETED, FAILED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
