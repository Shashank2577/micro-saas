# featureflagai - SPEC

## Goal
Build featureflagai, an AI-powered feature flag management platform.

## Architecture
- Backend: Spring Boot 3.3.5 (Java 21)
- Frontend: Next.js App Router, TypeScript, Tailwind
- Database: PostgreSQL, Flyway
- AI: LiteLLM via `cc-starter` AiService

## Entities
1. `FeatureFlag` (id, tenant_id, name, rules, enabled, rollout_pct, created_at, updated_at)
2. `FlagEvaluation` (id, tenant_id, flag_id, user_id, result, context, evaluated_at)
3. `RolloutMetrics` (id, tenant_id, flag_id, error_rate, timestamp)
4. `FlagSegment` (id, tenant_id, flag_id, name, conditions)
5. `FlagAuditLog` (id, tenant_id, flag_id, action, timestamp)

## Services
1. `FlagEvaluationService`: flag evaluation logic (<10ms target, hash bucket based on userId + flagId)
2. `RolloutService`: auto-pause rollout on error spike.
3. `ImpactAnalysisService`: measure conversion impact (using `AiService`).
4. `SegmentTargetingService`: evaluate segment targeting rules against user context.
5. `FlagCleanupService`: identify stale flags (>90 days unchanged).

## API Endpoints
- `GET /api/flags` - List all flags with status
- `POST /api/flags` - Create flag with targeting rules
- `GET /api/flags/{id}` - Get single flag
- `POST /api/flags/{id}/evaluate` - Evaluate flag for user context
- `POST /api/flags/{id}/rollout` - Control rollout percentage
- `GET /api/flags/{id}/impact` - Measure conversion impact
- `GET /api/flags/cleanup-suggestions` - Flags unused >90 days
- `GET /api/flags/{id}/segments` - List segments for flag
- `POST /api/flags/{id}/segments` - Create segment for flag
- `POST /api/flags/{id}/metrics` - Record rollout metrics

## Frontend Pages
- `/`: Flag list dashboard
- `/flags/[id]`: Flag detail (with rollout controls, segments)
- `/impact`: Impact analytics
- `/cleanup`: Cleanup dashboard

## Acceptances
- Create flag with segment targeting rules
- Rollout auto-pauses when error rate exceeds threshold
- Cleanup identifies flags unchanged >90 days
- Impact analysis shows conversion delta per flag
