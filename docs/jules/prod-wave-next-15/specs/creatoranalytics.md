# CreatorAnalytics — Next-15 Production Progression Spec

## Product Intent
- App: `creatoranalytics`
- Domain: Creator Intelligence
- Outcome: Content performance attribution and ROI insights for creator teams
- Primary actors: Content lead, Growth marketer, Creator manager

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Medium
- Risks: no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add backend service/controller tests for critical paths.
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. assets: backend service + API + frontend page + tests
- [ ] 2. metrics: backend service + API + frontend page + tests
- [ ] 3. attribution: backend service + API + frontend page + tests
- [ ] 4. roi: backend service + API + frontend page + tests
- [ ] 5. segments: backend service + API + frontend page + tests
- [ ] 6. insights: backend service + API + frontend page + tests

## Domain Model
### ContentAsset
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ContentAsset display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ChannelMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ChannelMetric display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### AttributionModel
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | AttributionModel display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ROISnapshot
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ROISnapshot display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### AudienceSegment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | AudienceSegment display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PerformanceInsight
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PerformanceInsight display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/creator-analytics/content-assets
- POST /api/v1/creator-analytics/content-assets
- GET /api/v1/creator-analytics/content-assets/{id}
- PATCH /api/v1/creator-analytics/content-assets/{id}
- POST /api/v1/creator-analytics/content-assets/{id}/validate
- GET /api/v1/creator-analytics/channel-metrics
- POST /api/v1/creator-analytics/channel-metrics
- GET /api/v1/creator-analytics/channel-metrics/{id}
- PATCH /api/v1/creator-analytics/channel-metrics/{id}
- POST /api/v1/creator-analytics/channel-metrics/{id}/validate
- GET /api/v1/creator-analytics/attribution-models
- POST /api/v1/creator-analytics/attribution-models
- GET /api/v1/creator-analytics/attribution-models/{id}
- PATCH /api/v1/creator-analytics/attribution-models/{id}
- POST /api/v1/creator-analytics/attribution-models/{id}/validate
- GET /api/v1/creator-analytics/roisnapshots
- POST /api/v1/creator-analytics/roisnapshots
- GET /api/v1/creator-analytics/roisnapshots/{id}
- PATCH /api/v1/creator-analytics/roisnapshots/{id}
- POST /api/v1/creator-analytics/roisnapshots/{id}/validate
- GET /api/v1/creator-analytics/audience-segments
- POST /api/v1/creator-analytics/audience-segments
- GET /api/v1/creator-analytics/audience-segments/{id}
- PATCH /api/v1/creator-analytics/audience-segments/{id}
- POST /api/v1/creator-analytics/audience-segments/{id}/validate
- POST /api/v1/creator-analytics/ai/analyze
- POST /api/v1/creator-analytics/workflows/execute
- GET /api/v1/creator-analytics/metrics/summary

## Event Contract
- Emits: creatoranalytics.roi.updated, creatoranalytics.insight.generated, creatoranalytics.segment.identified
- Consumes: contentos.asset.published, socialintelligence.signal.detected, seointelligence.rank.alerted
- All events include: `event_id`, `tenant_id`, `occurred_at`, `source_app`, `payload`
- Consumers are idempotent by `event_id`

## Non-Functional Requirements
- P95 latency target: <300ms reads, <600ms writes
- Multi-tenant isolation on all reads/writes
- Structured logs with correlation IDs
- OpenAPI completeness and contract tests

## Acceptance Criteria
1. All module slices implemented with API + UI + tests.
2. Strict tenant isolation and RBAC checks on all endpoints.
3. Event emit/consume contracts implemented and validated.
4. OpenAPI includes all endpoints from this spec.
5. Backend and frontend test suites pass in CI.
6. LiteLLM calls are guarded with timeout/retry/circuit-breaker.
7. No TODO or stubbed production logic remains.

## Build & Verify
- `mvn -pl creatoranalytics/backend clean verify`
- `npm --prefix creatoranalytics/frontend test`
- `npm --prefix creatoranalytics/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `creatoranalytics/.jules/HANDOFF.md`
- Submit PR when done
