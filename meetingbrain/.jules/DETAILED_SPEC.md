# MeetingBrain - Detailed Implementation Specification

## Overview
MeetingBrain is an AI meeting intelligence platform that transcribes meetings, extracts decisions, action items, and open questions, and provides a searchable institutional memory.

## Architecture

### Backend (Spring Boot 3.3.5)
- **Database:** PostgreSQL 16 + pgvector for semantic search.
- **Dependencies:** cc-starter, LiteLLM, pgmq, MinIO.
- **Entities:**
  - `Meeting` (id, tenant_id, title, start_time, end_time, external_id, platform, status, summary, created_at, updated_at)
  - `TranscriptLine` (id, tenant_id, meeting_id, speaker, text, start_timestamp, end_timestamp)
  - `Decision` (id, tenant_id, meeting_id, topic, description, decision_text, embedding, created_at)
  - `ActionItem` (id, tenant_id, meeting_id, description, owner, due_date, status, created_at)
  - `OpenQuestion` (id, tenant_id, meeting_id, question_text, status, created_at)
  - `Project` (id, tenant_id, name, description, created_at)
  - `ProjectLink` (id, tenant_id, entity_type, entity_id, project_id)

### Frontend (Next.js)
- **Pages:**
  - `/` (Dashboard - metrics, recent meetings, open action items)
  - `/meetings` (List all meetings)
  - `/meetings/[id]` (Meeting details, transcript, extracted insights)
  - `/decisions` (Searchable institutional memory)
  - `/action-items` (Manage action items)
  - `/projects` (Project linking)

### APIs
- `GET /api/meetings`
- `POST /api/meetings`
- `GET /api/meetings/{id}`
- `POST /api/meetings/{id}/analyze`
- `GET /api/meetings/{id}/transcript`
- `GET /api/decisions`
- `GET /api/decisions/search?query=...`
- `GET /api/action-items`
- `PUT /api/action-items/{id}`
- `GET /api/questions`
- `GET /api/projects`

### LiteLLM Integration
- Client for extracting summary, decisions, action items, questions from transcript text.
- Simple integration for generating embeddings using `vector` field.

## Database Schema (Flyway V1__init.sql)
```sql
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
```

## AI Integration Details
- Uses LiteLLM client for text generation and embeddings.
- Prompt for extracting meeting metadata will request JSON format:
```json
{
  "summary": "...",
  "decisions": [{"topic": "...", "description": "...", "decision_text": "..."}],
  "action_items": [{"description": "...", "owner": "...", "due_date": "YYYY-MM-DD"}],
  "open_questions": [{"question_text": "..."}]
}
```
- Emits integration events: `MeetingAnalyzedEvent`, `ActionItemCreatedEvent`.

## Test Plan
- **Backend:** Test controllers and services with `@SpringBootTest` (using H2 with pgvector mocked or disabled if needed, but since we use pgvector we might need Testcontainers or we can mock the repository methods in service tests and use `@WebMvcTest` + `@MockBean` for controller tests).
- **Frontend:** Test pages and components with Vitest.

