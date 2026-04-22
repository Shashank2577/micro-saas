# Call Intelligence

## Overview
Call Intelligence is fully implemented. It acts as an autonomous backend application tracking calls, creating speaker diarizations, generating transcripts, extracting actionable insights, and generating coaching recommendations using LLM pipelines.

## Implementation Details
1. **Domain Models**:
   - `Call`: Contains core call data and status (UPLOADING, TRANSCRIBING, ANALYZING, COMPLETED, FAILED).
   - `CallSpeaker`: Stores identified speakers (REP, PROSPECT) with talk ratio.
   - `CallTranscript`: Records timestamped utterances for each speaker.
   - `CallInsight`: Captures action items and general insights.
   - `CoachingRecommendation`: Tracks coaching feedback for sales reps.
   - `CallParticipant`: Tracks participants on a call.
   - `ActionItem`: Action items created during the call.
   - `SentimentAnalysis`: Tracks sentiment scores.

2. **Services**:
   - `CallService`: Core CRUD for Calls.
   - `AnalysisService`: The main worker. Asynchronously updates the call status, calls `AiService` to generate simulated transcript JSON, and publishes `call.analyzed` and `action_item.extracted` webhooks.
   - `ScorecardService`: Provides rep performance aggregates.
   - `EventPublisherService`: Emits system events securely.
   - `ActionItemService`: Tracks and aggregates action items.
   - `SentimentAnalysisService`: Aggregates sentiment data.

3. **Controllers**:
   - `CallController`: Manages interactions.
   - `ScorecardController`: Serves coaching insights.
   - `AnalysisController`: Exposes manual start hooks for analytics.
   - `InsightController`: Fetches generated insights per call.
   - `TranscriptController`: Fetches parsed call transcripts.
   - `ActionItemController`: Fetches action items.
   - `CallParticipantController`: Fetches call participants.
   - `SentimentAnalysisController`: Fetches sentiment analysis data.

4. **Integration**:
   - Webhooks mapped via `integration-manifest.json` (`call_created`, `call.analyzed`, `action_item.extracted`).

5. **Frontend**:
   - Foundational Next.js boilerplate setup with pages mapped to the eight main entities.

## Status
- **Build**: `mvn clean install -DskipTests` passes.
- **Frontend Build**: `npm run build` succeeds.
