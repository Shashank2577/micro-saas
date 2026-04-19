# VideoNarrator — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `videonarrator`
- **Domain:** Creator Intelligence
- **Outcome:** Repurpose long-form video into clips, summaries, chapters, and social derivatives
- **Primary actors:** Video editor, Content manager, Growth lead

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 35
- **Tier:** Incubating
- **Depth:** Shallow
- **Known structural risks:** no-db-migrations, no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Introduce Flyway V1 baseline and follow-up incremental migrations.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. ingestion: backend service + controller + frontend page + tests
- [ ] 2. transcription: backend service + controller + frontend page + tests
- [ ] 3. chaptering: backend service + controller + frontend page + tests
- [ ] 4. clip-generation: backend service + controller + frontend page + tests
- [ ] 5. social-variants: backend service + controller + frontend page + tests
- [ ] 6. performance: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### VideoAsset
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | VideoAsset display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### Transcript
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Transcript display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ClipSuggestion
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ClipSuggestion display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### Chapter
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Chapter display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SocialVariant
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SocialVariant display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### EngagementMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | EngagementMetric display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/videos/video-assets
- POST /api/v1/videos/video-assets
- GET /api/v1/videos/video-assets/{id}
- PATCH /api/v1/videos/video-assets/{id}
- POST /api/v1/videos/video-assets/{id}/validate
- GET /api/v1/videos/transcripts
- POST /api/v1/videos/transcripts
- GET /api/v1/videos/transcripts/{id}
- PATCH /api/v1/videos/transcripts/{id}
- POST /api/v1/videos/transcripts/{id}/validate
- GET /api/v1/videos/clip-suggestions
- POST /api/v1/videos/clip-suggestions
- GET /api/v1/videos/clip-suggestions/{id}
- PATCH /api/v1/videos/clip-suggestions/{id}
- POST /api/v1/videos/clip-suggestions/{id}/validate
- GET /api/v1/videos/chapters
- POST /api/v1/videos/chapters
- GET /api/v1/videos/chapters/{id}
- PATCH /api/v1/videos/chapters/{id}
- POST /api/v1/videos/chapters/{id}/validate
- GET /api/v1/videos/social-variants
- POST /api/v1/videos/social-variants
- GET /api/v1/videos/social-variants/{id}
- PATCH /api/v1/videos/social-variants/{id}
- POST /api/v1/videos/social-variants/{id}/validate
- POST /api/v1/videos/ai/analyze
- POST /api/v1/videos/ai/recommendations
- POST /api/v1/videos/workflows/execute
- GET /api/v1/videos/health/contracts
- GET /api/v1/videos/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- ingestionService: create/update/list/getById/delete/validate/simulate
- transcriptionService: create/update/list/getById/delete/validate/simulate
- chapteringService: create/update/list/getById/delete/validate/simulate
- clip-generationService: create/update/list/getById/delete/validate/simulate
- social-variantsService: create/update/list/getById/delete/validate/simulate
- performanceService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** videonarrator.transcript.ready, videonarrator.clip.generated, videonarrator.package.ready
- **Consumes:** contentos.calendar.slot.created, socialintelligence.trend.detected, brandvoice.style.updated
- Every emitted event must include: `event_id`, `tenant_id`, `occurred_at`, `source_app`, `payload`.
- Consumed events must be idempotent and deduplicated by `event_id`.
- Register/refresh contracts in `integration-manifest.json` and ensure Nexus Hub compatibility.

## 8) Frontend Requirements (Next.js App Router)
- Build operator-facing pages for all modules in section 3.
- Include:
  - Table/list view with filters and pagination
  - Detail page with timeline/activity
  - Create/edit forms with schema validation
  - Error/empty/loading states
  - Toast notifications and optimistic updates where safe
- Add component and page tests for critical paths.

## 9) AI/LLM Requirements
- Use LiteLLM through a dedicated client wrapper.
- Include:
  - Request schema validation
  - Timeout, retry (exponential backoff), and fallback behavior
  - Prompt templates versioned in code
  - Structured JSON output parsing with guardrails
  - Audit log of AI decisions (traceability)

## 10) Non-Functional & Security Requirements
- Multi-tenant hard isolation by `tenant_id` on every query and mutation.
- RBAC on every endpoint (minimum roles: admin/operator/viewer).
- PII fields encrypted at rest where applicable.
- Metrics:
  - P95 API latency < 300ms for reads, < 600ms for writes
  - Background jobs visible in metrics/health endpoints
- Observability:
  - Structured logs with correlation id
  - Error budget and alert thresholds
- Resilience:
  - Idempotency keys for external side-effects
  - Transaction boundaries for multi-write operations

## 11) Detailed Acceptance Criteria (must all pass)
1. All 6 module slices are implemented and wired from UI to backend.
2. All endpoints return tenant-scoped data and reject cross-tenant access.
3. All write endpoints publish documented domain events with payload schema validation.
4. OpenAPI docs render and include every endpoint in this spec.
5. Backend tests cover happy path, validation failures, and permission failures.
6. Frontend tests cover critical user flows and error states.
7. Async workflows are queued via pgmq and are idempotent on retries.
8. LiteLLM integration is guarded with timeout/retry/circuit breaker policy.
9. All env var contracts are documented and validated on startup.
10. No TODO/FIXME placeholders in production code paths.
11. Integration manifest contracts match implemented emits/consumes events.
12. PR includes .jules/DETAILED_SPEC.md, IMPLEMENTATION_LOG.md, and HANDOFF.md.

## 12) Integration Opportunities (what this app should connect to)
- Add Flyway baseline and incremental migrations for all persisted entities.
- Add backend service + controller tests for critical flows.
- Add frontend component/page tests for key user journeys.
- Add backend Dockerfile to make deploy path repeatable.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl videonarrator/backend clean verify`
- Frontend:
  - `npm --prefix videonarrator/frontend test`
  - `npm --prefix videonarrator/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
