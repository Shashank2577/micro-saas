# CallIntelligence - Detailed Specification

## 1. Overview
CallIntelligence is an AI sales call intelligence platform. It transcribes audio recordings (via Whisper), attributes speakers (2-5+), analyzes talk ratios, and detects specific sales patterns like objections, competitor mentions, and commitment language. It also integrates with CRM to extract action items.

## 2. Database Schema (PostgreSQL 16)

```sql
CREATE TABLE calls (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL, -- UPLOADING, TRANSCRIBING, ANALYZING, COMPLETED, FAILED
    audio_url VARCHAR(1024),
    duration_seconds INT,
    summary TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    rep_id VARCHAR(255) -- Optional identifier for the rep
);
CREATE INDEX idx_calls_tenant ON calls(tenant_id);
CREATE INDEX idx_calls_rep ON calls(rep_id);

CREATE TABLE call_speakers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    speaker_label VARCHAR(50) NOT NULL,
    speaker_name VARCHAR(255),
    role VARCHAR(50) NOT NULL, -- REP, PROSPECT
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
    question_type VARCHAR(20), -- OPEN, CLOSED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE call_insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    call_id UUID NOT NULL REFERENCES calls(id) ON DELETE CASCADE,
    insight_type VARCHAR(50) NOT NULL, -- OBJECTION, COMPETITOR, COMMITMENT, ACTION_ITEM
    category VARCHAR(100), -- e.g., 'price', 'timeline', 'AcmeCorp'
    description TEXT NOT NULL,
    transcript_id UUID REFERENCES call_transcripts(id), -- Optional link to specific utterance
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
```

## 3. REST API Endpoints

### Calls API
- `POST /api/calls`: Upload a new audio call (returns `id`, `status: UPLOADING`).
- `GET /api/calls`: List calls (paginated, filter by `rep_id`).
- `GET /api/calls/{id}`: Get call details (status, summary, duration).
- `GET /api/calls/{id}/speakers`: Get speakers and talk ratios.
- `GET /api/calls/{id}/transcript`: Get full transcript with speaker labels.
- `GET /api/calls/{id}/insights`: Get categorized insights (objections, competitors, commitments).

### Rep Scorecard & Coaching API
- `GET /api/scorecards/reps/{repId}`: Get aggregate metrics (avg talk ratio, objection handle rate).
- `GET /api/coaching/{repId}`: Get playbook recommendations.

## 4. Services

- `CallUploadService`: Handles multipart upload, stores in minio/local, creates DB record, publishes to queue.
- `TranscriptionService`: Uses OpenAI Whisper API to transcribe audio.
- `SpeakerDiarizationService`: (Mocked or simple heuristic via LiteLLM if Whisper doesn't natively do it well enough, though Whisper can provide some timestamps. We'll use a simulated process or prompt Claude to attribute speakers based on conversational turn-taking).
- `AnalysisEngineService`: Uses LiteLLM (Claude) to:
  - Detect questions (open/closed).
  - Extract objections, competitor mentions, and commitments.
  - Calculate talk ratio and sentiment per speaker.
  - Generate call summary and coaching insights.

## 5. UI Components (Next.js)

- `Dashboard`: Aggregate stats, recent calls, top coaching priorities.
- `CallUpload`: Dropzone area.
- `CallDetailView`:
  - Player with waveform/timeline.
  - Sidebar: Insights (Objections, Competitors).
  - Main area: Transcript with speaker highlighting.
- `RepScorecard`:
  - Visual charts (Recharts) for talk ratios over time.
  - Peer comparison percentile.

## 6. Integration Manifest

- Emits: `call.analyzed`, `action_item.extracted`
- Consumes: `crm.sync_requested`

