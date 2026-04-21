# FeatureFlagAI - Detailed Specification

## Overview
FeatureFlagAI is an AI feature management platform with intelligent targeting and automated rollout. It provides intelligent flag targeting based on user attributes, automated rollout decisions based on metrics, impact analysis showing downstream effects, automatic flag cleanup suggestions, A/B testing integration, and performance tracking per flag state.

## Architecture
- **Backend:** Spring Boot 3.3.5 (Java 21) using `cc-starter` modules (flags, ai).
- **Frontend:** Next.js 15 with TypeScript.
- **Database:** PostgreSQL 16 for flag state.
- **AI Integration:** LiteLLM / Claude for targeting logic.

## Domain Entities
1. **FeatureFlag**
   - `id`: UUID
   - `tenantId`: UUID
   - `name`: String
   - `rules`: JSONB
   - `enabled`: Boolean
   - `rolloutPct`: Integer
   - `createdAt`: Timestamp
   - `updatedAt`: Timestamp

2. **FlagEvaluation**
   - `id`: UUID
   - `tenantId`: UUID
   - `flagId`: UUID
   - `userId`: String
   - `result`: Boolean
   - `context`: JSONB
   - `evaluatedAt`: Timestamp

## Services
1. **FlagEvaluationService**: Evaluate flags with low latency (<10ms).
2. **RolloutService**: Control rollout based on metrics (<1 second decision).

## API Endpoints
- `GET /api/flags` - List flags
- `POST /api/flags` - Create flag
- `GET /api/flags/{id}` - Get flag
- `POST /api/flags/{id}/evaluate` - Evaluate flag for a given context
- `POST /api/flags/{id}/rollout` - Control rollout percentage and state
- `GET /api/flags/{id}/impact` - Measure impact using AI

## Acceptance Criteria
- Create feature flag; target by segment automatically
- Rollout pauses if error rate exceeds threshold
- Identify "stable flags" (unchanged >90 days) for cleanup
- Measure impact: "flag X increased conversion by 3%"

## Integration Manifest
- Emits: `featureflagai.flag.evaluated`
- Consumes: `*.metric.updated`
- Capabilities: `evaluate-flags`, `control-rollouts`, `analyze-impact`
