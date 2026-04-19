# VideoNarrator Detailed Specification

## Overview
VideoNarrator is an AI video narration and transcription service for creators. It allows users to upload videos, generate transcripts, sync subtitles to a timeline, and add AI narration via providers like Google TTS, OpenAI TTS, and ElevenLabs.

## 1. Database Schema
PostgreSQL 16 with pgvector extension (via Testcontainers for local dev, actual provisioned instance for production).

```sql
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
```

## 2. API Endpoints

All endpoints are secured via the `X-Tenant-ID` header provided by `cc-starter`.

### Video Projects
*   `POST /api/v1/projects`
    *   **Request:** `multipart/form-data` with `file` (video) and `title` (string).
    *   **Response:** `VideoProject` object.
    *   **Action:** Uploads file to MinIO (bucket: `videonarrator-{tenant_id}`), creates `VideoProject` record.
*   `GET /api/v1/projects`
    *   **Response:** List of `VideoProject` objects.
*   `GET /api/v1/projects/{id}`
    *   **Response:** `VideoProject` details.
*   `DELETE /api/v1/projects/{id}`
    *   **Action:** Deletes project and associated MinIO object.

### Transcriptions & Subtitles
*   `POST /api/v1/projects/{projectId}/transcribe`
    *   **Request:** `{ "languageCode": "en" }`
    *   **Response:** `Transcription` object (status PENDING).
    *   **Action:** Triggers async transcription job (pgmq).
*   `GET /api/v1/projects/{projectId}/transcriptions`
    *   **Response:** List of transcriptions with status.
*   `GET /api/v1/transcriptions/{transcriptionId}/subtitles`
    *   **Response:** List of `SubtitleTrack` objects ordered by sequence.
*   `PUT /api/v1/subtitles/{subtitleId}`
    *   **Request:** `{ "startTimeMs": 1000, "endTimeMs": 5000, "content": "Updated text" }`
    *   **Response:** Updated `SubtitleTrack`.

### Narrations
*   `GET /api/v1/voices`
    *   **Response:** List of available voices `[{ "id": "en-US-Standard-A", "provider": "GOOGLE", "name": "Google US English" }]`.
*   `POST /api/v1/projects/{projectId}/narrate`
    *   **Request:** `{ "voiceProvider": "OPENAI", "voiceId": "alloy", "transcriptionId": "uuid" }`
    *   **Response:** `NarrationTrack` object.
    *   **Action:** Triggers async narration generation (pgmq).

## 3. Services

*   `VideoProcessingService`: Handles MinIO uploads, retrieves URLs.
*   `TranscriptionService`: Interacts with OpenAI Whisper (via LiteLLM if possible, or direct API if audio files not supported well). Parses SRT/VTT into `SubtitleTrack` entities.
*   `NarrationService`: Calls TTS providers (Google TTS, OpenAI TTS, ElevenLabs). Supports voice cloning based on premium checks.
*   `SubtitleSyncService`: Manages timeline logic, ensures non-overlapping subtitles.

## 4. Frontend (Next.js)

*   **Pages:**
    *   `/dashboard`: List of video projects.
    *   `/projects/new`: Upload form (drag & drop).
    *   `/projects/[id]`: The main editor.
*   **Components:**
    *   `VideoPlayer`: Uses Video.js.
    *   `TimelineEditor`: Custom or react-timeline-based component for showing video track, audio track, and subtitle blocks.
    *   `VoiceSelector`: Dropdown mapping providers and voices.
    *   `SubtitleList`: List of editable subtitle inputs next to the timeline.

## 5. AI Integrations

*   **Transcription:** OpenAI Whisper model. We'll use the LiteLLM proxy locally (`http://localhost:4000`) for orchestration.
*   **TTS:** We'll model requests to LiteLLM's audio generation endpoints (or direct if LiteLLM doesn't support the specific TTS modality required, e.g., ElevenLabs direct). *Assumption: LiteLLM proxy is configured with these models.*

## 6. Events & Async

*   Uses `pgmq` for background jobs to avoid blocking REST threads for long API calls.
*   `JobType.TRANSCRIBE`, `JobType.NARRATE`.

## 7. Acceptance Criteria Check

1.  User can upload video (multipart API, MinIO storage) -> Yes.
2.  AI narration with selectable voice options -> Yes.
3.  Subtitles auto-synced to video timeline -> Yes.
4.  User can edit subtitle timing and content -> Yes (via PUT API).
5.  Export video with embedded narration -> Basic implementation will provide the audio URL and subtitle JSON; full ffmpeg burn-in might be complex for this scope, but we will mock the ffmpeg call if needed or provide a merged response.
6.  Multi-language -> Supported via `languageCode`.
7.  Integration manifest -> Will be defined.
8.  Pricing -> Track minutes (basic counter in service layer).
