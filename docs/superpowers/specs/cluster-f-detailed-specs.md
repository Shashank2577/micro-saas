# Cluster F: Data & Analytics Intelligence — Detailed Specs

## Overview
11 AI-native SaaS apps for data teams, analysts, and business stakeholders. Each app leverages AI to bridge the gap between raw data and actionable business value. Every app is built to **fully utilize a 2-hour Jules session** — rich domain models, deep REST surface, full frontend scaffolding, comprehensive tests.

**Global conventions (apply to all 11 apps):**
- Backend: Spring Boot 3.2 + `cc-starter 0.0.1-SNAPSHOT` via `tools/scaffold-app.sh <app> "<Display>" <port>`
- Java package: `com.microsaas.<app>`
- Layers: `api/` (REST controllers), `service/`, `domain/model/` (JPA entities), `domain/repository/`, `ai/` (LiteLLM clients + prompt templates), `event/` (webhook emitters/consumers), `config/`
- DB: PostgreSQL, schema-per-app, Flyway migrations under `db/migration/V1__*.sql`
- Auth: Keycloak via cc-starter OIDC; tenant isolation on every query (`@TenantScoped` or `tenantId` filter)
- AI: LiteLLM at `http://localhost:4000` → Claude Sonnet 4.6 for generation; `text-embedding-3-small` for vectors via pgvector
- Nexus Hub: Register at startup (`POST /api/v1/apps/register` → 8090). Emit events via webhook (`/api/v1/events/emit`). Consume via subscription.
- Frontend: Next.js 14 App Router + TypeScript + Tailwind + shadcn/ui. Typed API client from OpenAPI via `openapi-typescript`.
- Tests: JUnit 5 (backend unit + `@SpringBootTest` integration), Testcontainers for Postgres, Vitest + Testing Library (frontend).
- OpenAPI spec generated at `/v3/api-docs` (springdoc). Must serve at `/swagger-ui.html`.
- Health endpoint `/actuator/health` registered with Nexus.
- Integration manifest at repo root `<app>/integration-manifest.json`.
- Docker: multi-stage `Dockerfile` per app, `docker-compose.yml` snippet in `infra/compose.f.yml`.

**Universal Acceptance Criteria (every app must pass):**
1. `mvn -pl <app>/backend clean verify` passes with ≥80% line coverage on service layer.
2. `npm --prefix <app>/frontend run build && npm --prefix <app>/frontend run test` passes.
3. App boots with `docker compose -f infra/docker-compose.yml -f infra/compose.f.yml up <app>-backend`.
4. Swagger UI reachable at `http://localhost:<port>/swagger-ui.html` with every endpoint documented.
5. App auto-registers with Nexus Hub on startup; presence visible at `GET http://localhost:8090/api/v1/apps`.
6. At least one `emits` event is triggered by an integration test and visible on the Nexus event bus.
7. Every repository method filters by `tenantId`; a cross-tenant read test must fail with 404/403.
8. Every AI call has retry with exponential backoff, circuit-breaker wrapping, and records token usage to `ai_usage` table.
9. README.md in `<app>/` explains: what it is, ports, env vars, how to run, how to test.
10. `integration-manifest.json` exists and matches the spec below.

---

## 1. DataStoryTeller  — port 8150

**Tagline:** AI-powered data narrative and insight generation

**Problem:** Dashboards are full of charts nobody interprets correctly. Every Monday analysts waste 3–4 hours translating metrics into prose. This bottleneck slows decisions and creates a data literacy gap.

**Solution:** Connect to BI/warehouse → automatically produce structured markdown narratives with:
- What changed (delta)
- What drove it (attribution)
- Why it matters (impact)
- What to do next (prescriptive)

**Core Features:**
1. **Data connection**: Adapters for Snowflake, BigQuery, Postgres, CSV/JSON upload. Credentials encrypted via cc-starter `storage` module.
2. **Dataset registration**: Define a dataset = SQL query + schedule + metadata.
3. **Narrative generation**: Claude Sonnet 4.6 with a 3-stage prompt chain (summarize → attribute → recommend).
4. **Attribution analysis**: Dimensional decomposition — split delta across segments (region, product, channel).
5. **Scheduled briefs**: Cron-style delivery to Slack/Email/Nexus Hub webhook.
6. **Interactive Q&A**: Natural language → SQL (via Claude with `dataset.schema` context) → tabular result → prose answer.
7. **Chart-to-text**: Upload PNG/SVG of a chart; Claude describes the trend.
8. **Templates**: MoM executive summary, KPI deep-dive, cohort analysis, funnel performance.
9. **Feedback loop**: Users rate narratives; ratings fine-tune few-shot examples.
10. **Version history**: Every published narrative is immutable and linkable.

**Domain Model:**
- `DataSource`: id, tenantId, type (SNOWFLAKE|BQ|POSTGRES|UPLOAD), name, encryptedConnection, lastSyncedAt, status
- `Dataset`: id, dataSourceId, name, sqlQuery, schemaJson, refreshCadence, ownerId
- `NarrativeTemplate`: id, name, promptTemplate, outputSchema, isDefault
- `NarrativeReport`: id, datasetId, templateId, title, contentMarkdown, timeRangeStart, timeRangeEnd, status (DRAFT|PUBLISHED|ARCHIVED), generatedAt, tokenUsage, model
- `Insight`: id, reportId, type (TREND|ANOMALY|CORRELATION|SEGMENT_DRIVER), headline, description, evidenceJson, severity (INFO|WARN|ALERT)
- `ScheduledDelivery`: id, reportTemplateId, cronExpr, recipientsJson, channel (SLACK|EMAIL|WEBHOOK), lastDeliveredAt
- `Feedback`: id, reportId, rating (1–5), comment, userId, createdAt
- `AiUsage`: id, feature, promptTokens, completionTokens, costUsd, createdAt

**REST API (20 endpoints):**
```
POST   /data-sources                           create connection
GET    /data-sources                           list
GET    /data-sources/{id}                      detail (secrets redacted)
DELETE /data-sources/{id}
POST   /data-sources/{id}/test                 connectivity probe
POST   /datasets                               register
GET    /datasets                               list
POST   /datasets/{id}/preview                  run SQL, return first 100 rows
POST   /narratives/generate                    body: {datasetId, templateId, timeRange}
GET    /narratives                             list (paginated, filter by dataset/status)
GET    /narratives/{id}                        detail
PUT    /narratives/{id}/publish                change status DRAFT→PUBLISHED
POST   /narratives/{id}/feedback               body: {rating, comment}
POST   /narratives/ask                         Q&A: {datasetId, question}
POST   /narratives/chart-describe              multipart/form-data image upload
GET    /templates                              list templates
POST   /templates                              create custom template
POST   /schedules                              register cron
GET    /schedules
DELETE /schedules/{id}
```

**AI pipelines:**
- **Generate**: system prompt with business context + dataset schema + time-range aggregates → Claude streams markdown.
- **Attribute**: GROUP BY all dimensions, run significance test (z-score), feed top drivers into prompt.
- **Q&A**: two-stage — NL→SQL with schema + few-shots, then execute+format, then prose wrap.
- **Chart-describe**: use vision model (Claude Sonnet 4.6 multimodal) with image + context.

**Integration manifest:**
```json
{
  "app":"datastoryteller","displayName":"DataStoryTeller","version":"0.0.1",
  "baseUrl":"http://localhost:8150","healthEndpoint":"/actuator/health",
  "emits":["narrative.generated","narrative.published","insight.detected","delivery.dispatched"],
  "consumes":["metric.updated","dashboard.refreshed","anomaly.detected"],
  "capabilities":[
    {"name":"generate-narrative","input":"datasetId+timeRange","output":"markdownReport"},
    {"name":"explain-metric","input":"metricName+values","output":"prose"},
    {"name":"summarize-dashboard","input":"dashboardId","output":"executiveSummary"}
  ]
}
```

**Frontend pages:**
- `/` — home with recent narratives grid
- `/data-sources`, `/data-sources/new`
- `/datasets`, `/datasets/[id]` — preview + schedule
- `/narratives`, `/narratives/[id]` — rendered markdown + feedback widget
- `/narratives/new` — wizard (pick dataset → template → generate → review → publish)
- `/schedules`, `/templates`, `/settings`

**App-specific Acceptance Criteria (adds to 10 universal):**
11. Generate a narrative from a 50-row sample dataset and return markdown that references at least 3 distinct metrics.
12. Attribution endpoint breaks down a 20% revenue delta into ≥3 segment drivers with contribution %.
13. Q&A endpoint executes an NL question end-to-end and returns both SQL and prose answer in <8 s for warm cache.
14. Scheduled delivery fires a Slack webhook in a test that uses a mock server to assert the payload.
15. Feedback rating updates a rolling average visible on the report detail page.
16. Chart-describe endpoint accepts a PNG and returns ≥50-word description grounded in what the chart shows.
17. Publishing a narrative emits `narrative.published` event to Nexus hub (asserted in integration test).

---

## 2. PipelineGuardian — port 8151

**Tagline:** AI-powered data pipeline monitoring and root-cause analysis

**Problem:** Data pipeline failures are silent and expensive. Tables go stale, schemas drift, downstream dashboards show wrong numbers. Engineers find out when stakeholders complain.

**Solution:** Monitor all pipelines for freshness, completeness, volume anomalies; when something breaks, identify root cause and estimate downstream impact across dependent assets.

**Core Features:**
1. **Pipeline registration** — name, schedule, owner, SLA.
2. **Run ingestion** — webhook from Airflow/dbt/GitHub Actions posts run events.
3. **Freshness tracking** — per-asset staleness detection.
4. **Volume anomaly detection** — EWMA + z-score on row counts.
5. **Schema drift alerts** — compare current schema hash to history.
6. **Root-cause AI** — given error log + recent changes + lineage, Claude ranks probable causes.
7. **Lineage graph** — upstream/downstream DAG with transformation metadata.
8. **Blast-radius** — traverse lineage to count affected dashboards + downstream assets.
9. **SLA policies** — freshness/quality thresholds, notification routing.
10. **Incident history** — every anomaly becomes a timeline event.

**Domain Model:**
- `Pipeline`: id, tenantId, name, schedule (cron), ownerId, slaMinutes, status (ACTIVE|PAUSED|ARCHIVED)
- `PipelineRun`: id, pipelineId, startedAt, completedAt, rowsProcessed, status (RUNNING|SUCCESS|FAILED), errorMessage, logsRef
- `DataAsset`: id, pipelineId, name, type (TABLE|VIEW|MATERIALIZED), lastUpdatedAt, rowCount, schemaHash, slaFreshnessMinutes
- `SchemaSnapshot`: id, assetId, columnsJson, hash, capturedAt
- `Anomaly`: id, assetId, runId, type (VOLUME|FRESHNESS|SCHEMA|NULL_RATE), severity, detectedAt, estimatedImpactJson, rootCauseHypothesis, rcaConfidence
- `Lineage`: id, upstreamAssetId, downstreamAssetId, transformationType (SQL|DBT_MODEL|SPARK_JOB), dependencyStrength
- `AlertPolicy`: id, scope (asset|pipeline), condition (json), channel, recipientsJson

**REST API (22 endpoints):**
```
POST   /pipelines                        register
GET    /pipelines
GET    /pipelines/{id}
PUT    /pipelines/{id}
POST   /pipelines/{id}/pause
POST   /pipelines/{id}/runs              ingest run (webhook target)
GET    /pipelines/{id}/runs
GET    /runs/{id}                        with error + logs summary
POST   /assets                           register asset
GET    /assets
GET    /assets/{id}
GET    /assets/{id}/freshness
GET    /assets/{id}/schema-history
GET    /assets/{id}/lineage?direction=upstream|downstream|both&depth=3
POST   /anomalies                        manual trigger
GET    /anomalies                        filter by severity, type
GET    /anomalies/{id}
POST   /anomalies/{id}/analyze           run AI RCA
POST   /anomalies/{id}/resolve
GET    /anomalies/{id}/blast-radius      list affected assets + dashboards
POST   /alert-policies
GET    /alert-policies
```

**AI pipelines:**
- **RCA**: input = error log (truncated to 4k tokens) + recent schema diffs + last 5 runs' metadata + known code deploys within 24 h. Output = ranked hypotheses with evidence citations.
- **Anomaly scoring**: not AI — classic EWMA + z-score with threshold. AI is *only* for RCA + narrative.
- **Impact narrative**: given affected assets, Claude writes human-readable impact summary for notifications.

**Integration manifest:**
```json
{
  "app":"pipelineguardian","displayName":"PipelineGuardian","version":"0.0.1",
  "baseUrl":"http://localhost:8151","healthEndpoint":"/actuator/health",
  "emits":["pipeline.failed","anomaly.detected","freshness.violated","schema.changed","impact.analyzed"],
  "consumes":["job.completed","schema.changed","deploy.completed"],
  "capabilities":[
    {"name":"analyze-impact","input":"assetId","output":"downstreamAssets[]"},
    {"name":"get-root-cause","input":"runId","output":"analysisReport"},
    {"name":"check-freshness","input":"assetId","output":"status"},
    {"name":"upstream-dependencies","input":"assetId","output":"upstreamGraph"}
  ]
}
```

**Frontend pages:** `/` dashboard, `/pipelines`, `/pipelines/[id]` (run timeline), `/assets`, `/assets/[id]/lineage` (force-directed graph), `/anomalies`, `/anomalies/[id]` (RCA view), `/alert-policies`.

**App-specific AC:**
11. Ingest 100 synthetic runs; 10 below volume threshold must produce exactly 10 anomalies.
12. Schema diff: change one column type; next registration must emit `schema.changed`.
13. Lineage query at depth 3 returns correct DAG on a fixture with 15 nodes.
14. RCA endpoint produces ≥3 ranked hypotheses and at least one includes a recent schema change citation when one exists.
15. Blast-radius for an asset with 4 downstream dashboards returns all 4 in the response.
16. Freshness SLA test: simulate 65 min staleness on 60-min SLA — `freshness.violated` event emitted within 1 poll cycle.
17. Load test: 1000 runs/min ingested without loss (test uses a mock queue).

---

## 3. MetricsHub — port 8152

**Tagline:** Semantic metric layer that prevents "which revenue?" fights

**Problem:** Revenue means different things to sales/finance/product. Five SQL snippets, five answers, zero trust.

**Solution:** Central registry of metric definitions (SQL + dimensions + semantics). AI-powered consistency checker. Agent-callable API used by other apps to look up canonical definitions.

**Core Features:**
1. **Metric CRUD** — YAML or UI to register metrics with name, description, formula, dimensions, grain, units.
2. **SQL validation** — parse formula, ensure references exist, check dimension conformance.
3. **Consistency checker** — given ad-hoc SQL, detect metrics-in-disguise and flag vs canonical definitions.
4. **Versioning** — definitions versioned; change requires PR-style approval.
5. **Usage tracking** — dashboards/reports register their metric consumption; lineage visible per metric.
6. **Conflict detection** — fuzzy match on names + semantic similarity (embedding) to surface duplicates.
7. **Documentation generation** — Claude writes human-readable descriptions from SQL + schema.
8. **Metric API** — `GET /metrics/{name}/value?time=...&groupBy=...` executes canonical formula.
9. **Approval workflow** — DRAFT → IN_REVIEW → APPROVED with RBAC.
10. **Change impact** — when a metric changes, compute downstream impact across dashboards that use it.

**Domain Model:**
- `MetricDefinition`: id, tenantId, name (unique per tenant), description, formulaSql, grain (DAILY|WEEKLY|MONTHLY|EVENT), unit, dimensionsJson, ownerId, version, status (DRAFT|IN_REVIEW|APPROVED|DEPRECATED), deprecatedAt
- `MetricVersion`: id, metricId, version, formulaSql, changelogMarkdown, approvedBy, approvedAt
- `MetricMapping`: id, metricId, dataSourceId, physicalTable, physicalColumn
- `MetricUsage`: id, metricId, consumerAppId, consumerEntityType, consumerEntityId, lastAccessedAt
- `MetricConflict`: id, metricIdA, metricIdB, reason (NAME_SIMILAR|FORMULA_OVERLAP|SEMANTIC_DUP), similarityScore, detectedAt, status (OPEN|RESOLVED|IGNORED)
- `ApprovalRequest`: id, metricId, proposedFormula, requesterId, reviewerId, status, comments

**REST API (24 endpoints):**
```
POST   /metrics                            register
GET    /metrics                            list (filter by status, owner, search)
GET    /metrics/{name}
GET    /metrics/{id}
PUT    /metrics/{id}                       create new DRAFT version
POST   /metrics/{id}/submit-for-review
POST   /metrics/{id}/approve
POST   /metrics/{id}/deprecate
GET    /metrics/{id}/versions
GET    /metrics/{id}/usage
GET    /metrics/{id}/change-impact         list affected dashboards
GET    /metrics/{name}/value               execute canonical
POST   /metrics/validate-sql               check ad-hoc SQL
POST   /metrics/find-conflicts             run conflict detection
GET    /conflicts
POST   /conflicts/{id}/ignore
POST   /conflicts/{id}/resolve
POST   /ai/generate-description            body: {sql, schema}
POST   /ai/explain-metric                  body: {metricId}
POST   /usage/report                       consumer registers usage
POST   /imports/yaml                       bulk upload
GET    /exports/yaml                       bulk dump
GET    /exports/openapi                    metric service discovery spec
POST   /mappings                           add data-source mapping
```

**AI pipelines:**
- **Description gen**: schema + SQL → markdown description + tags.
- **Semantic dedup**: embed `name + description + formula` of every metric; nightly nearest-neighbor pass surfaces candidate dupes.
- **Formula explain**: translate SQL to plain English.

**Integration manifest:**
```json
{
  "app":"metricshub","displayName":"MetricsHub","version":"0.0.1",
  "baseUrl":"http://localhost:8152","healthEndpoint":"/actuator/health",
  "emits":["metric.defined","metric.updated","metric.approved","metric.deprecated","conflict.detected"],
  "consumes":["query.executed","dashboard.created","dashboard.updated"],
  "capabilities":[
    {"name":"lookup-metric","input":"metricName","output":"definition"},
    {"name":"validate-sql","input":"sqlQuery","output":"consistencyReport"},
    {"name":"list-dimensions","input":"metricName","output":"dimensions[]"},
    {"name":"get-value","input":"metricName+dateRange+groupBy","output":"tabular"}
  ]
}
```

**Frontend pages:** `/metrics`, `/metrics/new`, `/metrics/[id]` (tabs: definition, usage, versions, conflicts), `/conflicts`, `/approvals`, `/import`.

**App-specific AC:**
11. Register two metrics with similar semantics; conflict detector surfaces the pair with score >0.75.
12. Approval workflow blocks non-approvers from hitting `/approve` (403).
13. `GET /metrics/{name}/value` returns correct result vs a hand-written control query.
14. Submitting new version auto-creates a `MetricVersion` row + changelog.
15. Change-impact for a metric used by 2 dashboards lists both.
16. Bulk YAML import of 20 metrics lands idempotently (rerun produces 0 changes).
17. `ai/generate-description` produces a 3-sentence description grounded in the SQL references.

---

## 4. DataCatalogAI — port 8153

**Tagline:** Self-maintaining data catalog — no human tax

**Problem:** Data catalogs (Alation, Collibra) require massive manual curation. Most rot within months of launch.

**Solution:** AI catalog that documents itself from schema introspection + query logs, ranks trust, and surfaces business context automatically.

**Core Features:**
1. **Schema crawlers** — Snowflake/BQ/Postgres introspectors on schedule.
2. **Auto-documentation** — Claude drafts table + column descriptions from name, types, sample values.
3. **Query-log ingestion** — parse warehouse query history; infer relationships + usage.
4. **Lineage inference** — parse SQL to extract source→target relationships.
5. **Trust score** — freshness + test coverage + ownership + usage yields a 0–100 score.
6. **Semantic search** — natural language → pgvector → matching assets.
7. **Ownership** — owner assignment + SLA.
8. **Tagging** — PII, PHI, financial, internal-only — auto-detected + human-curated.
9. **Popularity** — who uses what, how often.
10. **Business glossary** — link business terms to physical assets.

**Domain Model:**
- `Catalog`: id, tenantId, name
- `DataSource`: id, catalogId, type, connection (encrypted)
- `Asset`: id, sourceId, fqn (db.schema.table), type (TABLE|VIEW|MATERIALIZED|MODEL), rowCountEstimate, ownerId, trustScore, tagsJson, descriptionAi, descriptionHuman
- `Column`: id, assetId, name, dataType, isNullable, isPrimaryKey, piiCategory, description
- `QueryLog`: id, assetId, executedAt, executorUserId, durationMs, rowsReturned
- `SemanticEmbedding`: id, assetId, vector (pgvector), textSource
- `Relationship`: id, fromAssetId, toAssetId, relType (FK|LINEAGE|JOIN_INFERRED), confidence
- `GlossaryTerm`: id, term, definition, linkedAssetIds

**REST API (22 endpoints):**
```
POST   /sources                        register
POST   /sources/{id}/crawl             trigger full crawl
GET    /sources
GET    /assets                         paginated, facet filters
GET    /assets/{id}
GET    /assets/search?q=               semantic search
PUT    /assets/{id}/description
PUT    /assets/{id}/owner
PUT    /assets/{id}/tags
GET    /assets/{id}/columns
PUT    /columns/{id}/description
GET    /assets/{id}/lineage
GET    /assets/{id}/popularity
GET    /assets/{id}/queries
POST   /ai/document-asset              {assetId}
POST   /ai/detect-pii                  {assetId}
POST   /query-logs/ingest              batch endpoint
POST   /glossary                       create term
GET    /glossary
PUT    /glossary/{id}
POST   /relationships                  manual
GET    /trust/{assetId}                trust breakdown
```

**AI pipelines:**
- **Auto-doc**: column names + first 10 sample values + foreign-key hints → Claude markdown.
- **PII detection**: column content pattern-match + Claude classification (EMAIL, SSN, PHONE, etc.).
- **Semantic search**: embed query with `text-embedding-3-small`, cosine against asset embeddings.
- **Relationship inference**: parse SQL queries with a parser; AI second-opinion on JOIN hints.

**Integration manifest:**
```json
{
  "app":"datacatalogai","displayName":"DataCatalogAI","version":"0.0.1",
  "baseUrl":"http://localhost:8153","healthEndpoint":"/actuator/health",
  "emits":["asset.discovered","asset.documented","pii.detected","trust.degraded"],
  "consumes":["schema.changed","query.executed","deploy.completed"],
  "capabilities":[
    {"name":"search-assets","input":"query","output":"assets[]"},
    {"name":"describe-asset","input":"assetId","output":"documentation"},
    {"name":"list-pii","input":"sourceId","output":"piiColumns[]"},
    {"name":"get-lineage","input":"assetId","output":"graph"}
  ]
}
```

**Frontend pages:** `/` search-first homepage, `/assets/[id]`, `/glossary`, `/sources`, `/pii-report`.

**App-specific AC:**
11. Crawl a fixture Postgres schema with 10 tables; exactly 10 assets + correct column counts persisted.
12. Auto-doc produces ≥2-sentence description referencing at least one column name.
13. PII detection flags a column of fake emails with confidence >0.85.
14. Semantic search query "monthly revenue" returns the `orders.amount_usd` table in top 3.
15. Lineage inference from a query log `INSERT INTO a SELECT ... FROM b,c` creates two relationships.
16. Trust score monotonically changes when freshness drops.
17. Glossary term linked to 3 assets returns all 3 on term detail.

---

## 5. ExperimentEngine — port 8154

**Tagline:** A/B testing with real statistical rigor — without paying Optimizely $5K/month

**Problem:** SMBs run A/B tests in analytics tools with no stats, or pay $60K/yr for platforms they don't fully use.

**Solution:** Experimentation platform with hypothesis gen, proper stats (frequentist + Bayesian), feature-flag integration, and peek-protection.

**Core Features:**
1. **Experiment definition** — hypothesis, primary metric, guardrails, audience, allocation.
2. **Variant management** — control + 1..n variants, traffic split.
3. **Assignment API** — deterministic hashing for idempotent bucketing.
4. **Event tracking** — exposure + conversion events.
5. **Stats engine** — frequentist (t/χ²) + Bayesian (posterior distributions) + sequential testing.
6. **Significance alerts** — notify when test hits power or reaches novel threshold.
7. **Peek protection** — hide results until minimum sample; warn on repeated checks.
8. **AI hypothesis gen** — given product metric + past experiments, propose 5 candidate tests.
9. **Feature-flag integration** — cc-starter `flags` module for safe rollout post-win.
10. **Multi-metric view** — primary + secondary + guardrail with per-metric stats.

**Domain Model:**
- `Experiment`: id, tenantId, name, hypothesis, status (DRAFT|RUNNING|PAUSED|CONCLUDED), startedAt, endedAt, minSampleSize, significanceThreshold, peekProtection
- `Variant`: id, experimentId, name, trafficPercent, featureFlagKey
- `Metric`: id, experimentId, name, type (COUNT|SUM|MEAN|PROPORTION), role (PRIMARY|SECONDARY|GUARDRAIL), eventName
- `Assignment`: id, experimentId, unitId (user/session), variantId, assignedAt
- `Event`: id, experimentId, unitId, eventName, value, properties, ts
- `AnalysisResult`: id, experimentId, metricId, variantId, nExposures, nConversions, mean, stdErr, pValue, bayesianProbBetter, updatedAt

**REST API (20 endpoints):**
```
POST   /experiments
GET    /experiments
GET    /experiments/{id}
PUT    /experiments/{id}
POST   /experiments/{id}/start
POST   /experiments/{id}/pause
POST   /experiments/{id}/conclude          {winnerVariantId}
POST   /experiments/{id}/variants
POST   /experiments/{id}/metrics
POST   /assign                             {experimentId, unitId}   → variant
POST   /events/track                       batch events
GET    /experiments/{id}/results
GET    /experiments/{id}/results/live      server-sent-events stream
POST   /ai/suggest-hypotheses              {productArea, metricId}
POST   /ai/interpret-results               {experimentId}
GET    /experiments/{id}/power-curve
POST   /experiments/{id}/rollout           activate flag for winner
GET    /flags                              list managed flags
POST   /flags/{key}/toggle
GET    /experiments/{id}/audit
```

**AI pipelines:**
- **Hypothesis gen**: recent behavioral data summary + past experiments → Claude proposes 5 hypotheses with expected lift range + instrument effort.
- **Results interpreter**: Claude writes plain-English interpretation of stats results with correct hedging ("inconclusive" if underpowered).

**Integration manifest:**
```json
{
  "app":"experimentengine","displayName":"ExperimentEngine","version":"0.0.1",
  "baseUrl":"http://localhost:8154","healthEndpoint":"/actuator/health",
  "emits":["experiment.started","experiment.significant","experiment.concluded","rollout.activated"],
  "consumes":["metric.defined","feature-flag.updated"],
  "capabilities":[
    {"name":"assign","input":"unitId+experimentId","output":"variant"},
    {"name":"track-event","input":"event","output":"ack"},
    {"name":"get-results","input":"experimentId","output":"analysis"}
  ]
}
```

**Frontend pages:** `/experiments`, `/experiments/new` (wizard), `/experiments/[id]` (tabs: overview, variants, results, timeline), `/insights`, `/flags`.

**App-specific AC:**
11. Given 10k synthetic assignments, stats engine produces p-value within ±0.001 of scipy reference.
12. Peek protection: 5 results checks before min-sample produces 1 warning notification.
13. Deterministic assignment: same unitId + experimentId always returns same variant.
14. Concluding an experiment with winner toggles feature flag for that variant.
15. Hypothesis generator proposes 5 ideas grounded in provided behavioral summary.
16. Guardrail metric regressing >5% triggers `experiment.guardrail-violated` event.
17. SSE stream on results returns ≥1 update within 10 s of a new batch of events.

---

## 6. CustomerSignal — port 8155

**Tagline:** AI customer intelligence — health, churn, expansion in one place

**Problem:** Product analytics show usage but not customer health. Churn is discovered the month it happens.

**Solution:** Aggregate behavioral + support + sales data → health scores, churn prediction 60+ days out, CLV, expansion signals.

**Core Features:**
1. **Signal ingestion** — adapters for product events, support (tickets, NPS), sales (deal stage).
2. **Customer aggregate** — unified customer profile with rollups.
3. **Health scoring** — weighted formula (engagement + breadth + adoption + sentiment).
4. **Churn model** — gradient-boosted trees with feature store.
5. **CLV model** — probabilistic (BG/NBD or Pareto/NBD).
6. **Segmentation** — behavioral clustering (k-means on embeddings).
7. **Account plays** — templated CSM workflows triggered by signals.
8. **Risk alerts** — Slack/Email when score drops >X in Y days.
9. **Expansion signals** — feature adoption thresholds hint at upsell.
10. **What-changed** — per-customer narrative explaining score delta.

**Domain Model:**
- `Customer`: id, tenantId, externalId, name, plan, mrrCents, startedAt, status
- `Signal`: id, customerId, source (PRODUCT|SUPPORT|SALES), type, value, ts
- `HealthScore`: id, customerId, scoreCurrent, scorePrev, bucket (HEALTHY|AT_RISK|CRITICAL), updatedAt
- `ChurnPrediction`: id, customerId, horizon (30|60|90), probability, topFeaturesJson, modelVersion, predictedAt
- `ClvForecast`: id, customerId, lifetimeMonths, expectedRevenueCents, confidenceInterval, updatedAt
- `Segment`: id, name, rulesJson, customerCount
- `Play`: id, name, triggerCondition, actionsJson, ownerRole
- `PlayExecution`: id, playId, customerId, triggeredAt, status

**REST API (22 endpoints):**
```
POST   /customers/upsert                    batch
GET    /customers
GET    /customers/{id}                      full profile
GET    /customers/{id}/health
GET    /customers/{id}/churn
GET    /customers/{id}/clv
GET    /customers/{id}/signals              paginated
POST   /signals/ingest                      batch
POST   /scoring/recompute                   {customerId}
POST   /models/churn/train                  async
POST   /models/churn/predict                {customerId}
GET    /segments
POST   /segments                            create rule-based
POST   /segments/ai                         nl → rules
POST   /plays
POST   /plays/{id}/execute                  {customerId}
GET    /plays/{id}/executions
GET    /insights/what-changed/{customerId}
GET    /insights/top-risks                  dashboard
GET    /insights/top-expansion
POST   /ai/explain-score                    {customerId}
POST   /ai/recommend-action                 {customerId}
```

**AI pipelines:**
- **Explain score**: SHAP-like feature contributions + Claude prose.
- **Recommend action**: customer profile + recent signals → suggested CSM play.
- **Segment from NL**: "customers with >10 users but <20% adoption" → parsed rule JSON via Claude.

**Integration manifest:**
```json
{
  "app":"customersignal","displayName":"CustomerSignal","version":"0.0.1",
  "baseUrl":"http://localhost:8155","healthEndpoint":"/actuator/health",
  "emits":["health.degraded","churn.predicted","expansion.detected","play.triggered"],
  "consumes":["product.event","support.ticket","deal.updated","nps.submitted"],
  "capabilities":[
    {"name":"get-health","input":"customerId","output":"score+drivers"},
    {"name":"predict-churn","input":"customerId+horizon","output":"probability"},
    {"name":"explain-score","input":"customerId","output":"narrative"}
  ]
}
```

**Frontend pages:** `/` portfolio dashboard, `/customers/[id]` (tabs: overview, health, signals, plays), `/segments`, `/plays`, `/models`.

**App-specific AC:**
11. 1k synthetic customers with seeded signals: health distribution ranges correctly across buckets.
12. Churn prediction endpoint returns probability in [0,1] for all seeded customers.
13. Segment-from-NL produces valid JSON rules for 3 seed prompts.
14. Play triggered on threshold breach produces `play.triggered` event.
15. What-changed for a customer whose score dropped 20 pts names at least 2 contributing signals.
16. Model retrain endpoint persists a new model version and previous predictions remain queryable.
17. Top-risks dashboard returns accounts sorted by risk × MRR.

---

## 7. ForecastAI — port 8156

**Tagline:** Revenue/demand forecasts that account for reality

**Problem:** Forecasts are built on pipeline coverage with no stats, no seasonality, no confidence interval. When sales misses, nobody knows why.

**Solution:** ML-based forecasting with calibrated intervals + "what-needs-to-be-true" reverse-engineering.

**Core Features:**
1. **Historical load** — actuals + pipeline by stage + external signals.
2. **Seasonality decomposition** — STL/Prophet-style.
3. **Ensemble models** — SARIMA + gradient boost + neural — stacked.
4. **Confidence intervals** — conformal prediction for calibrated ranges.
5. **Scenario planning** — change inputs, see forecast shift.
6. **WNTBT** — given a target, derive required conversion/ACV/cycle assumptions.
7. **Backtesting** — rolling origin evaluation with MAPE per horizon.
8. **What-changed** — since last forecast, narrative explains swings.
9. **External signals** — macro (interest rate), seasonality, holidays.
10. **Alerting** — forecast miss probability exceeds threshold → ping.

**Domain Model:**
- `ForecastSeries`: id, tenantId, name, grain, unit, target (REVENUE|BOOKINGS|DEMAND|OTHER)
- `HistoricalPoint`: id, seriesId, periodStart, actualValue
- `PipelineSnapshot`: id, seriesId, capturedAt, stagesJson (stage→{count, amount, prob})
- `Model`: id, seriesId, algorithm, hyperparamsJson, trainedAt, mapeBacktest
- `Forecast`: id, seriesId, modelId, generatedAt, horizonPeriods, pointsJson ({period, point, lo, hi})
- `Scenario`: id, baseForecastId, adjustmentsJson, generatedForecastId
- `Wntbt`: id, seriesId, targetValue, assumptionsJson, feasibilityScore

**REST API (21 endpoints):**
```
POST   /series
GET    /series
GET    /series/{id}
POST   /series/{id}/history                 bulk upload
POST   /series/{id}/pipeline                snapshot
POST   /series/{id}/train
POST   /series/{id}/forecast                {horizon}
GET    /series/{id}/forecasts/latest
GET    /forecasts/{id}
POST   /scenarios                           base + adjustments
GET    /scenarios/{id}
POST   /wntbt                               {seriesId, target}
GET    /series/{id}/backtest
GET    /series/{id}/seasonality             decomposition
POST   /models/{id}/retrain
POST   /signals/macro                       add external
POST   /alerts
GET    /alerts
POST   /ai/explain-forecast                 narrative
POST   /ai/recommend-adjustment             scenario
GET    /exports/csv
```

**AI pipelines:**
- **Explain forecast**: model + recent signals → narrative covering trend, seasonality, pipeline cover ratio, macro.
- **WNTBT narrative**: required assumptions translated to plain English + feasibility verdict.
- **Anomaly-in-forecast**: flag points with residual > 2σ vs last forecast.

**Integration manifest:**
```json
{
  "app":"forecastai","displayName":"ForecastAI","version":"0.0.1",
  "baseUrl":"http://localhost:8156","healthEndpoint":"/actuator/health",
  "emits":["forecast.generated","forecast.miss-probable","scenario.created"],
  "consumes":["deal.updated","closing.finalized","actual.logged"],
  "capabilities":[
    {"name":"get-forecast","input":"seriesId+horizon","output":"points+CI"},
    {"name":"scenario","input":"baseId+adjustments","output":"forecast"},
    {"name":"wntbt","input":"seriesId+target","output":"assumptions"}
  ]
}
```

**Frontend pages:** `/series`, `/series/[id]` (chart + CI band), `/scenarios`, `/wntbt`, `/backtest`.

**App-specific AC:**
11. Trained on 36 months of synthetic data, MAPE for 3-month horizon < 10%.
12. Conformal interval coverage ≥90% on holdout.
13. Scenario adjustment of +10% conversion produces expected forecast shift.
14. WNTBT given target 20% above trend returns assumptions marked "aggressive".
15. Backtest endpoint returns MAPE per fold for ≥3 folds.
16. Explain-forecast narrative cites at least one seasonal and one pipeline factor.
17. Alert fires when forecast miss probability > configured threshold.

---

## 8. AlertIntelligence — port 8157

**Tagline:** Kill alert fatigue; alerts become incidents

**Problem:** Monitoring emits hundreds of alerts; real incidents drown in noise; on-call burns out.

**Solution:** Correlate alerts into incidents, learn actionable-vs-noise per environment, route with context.

**Core Features:**
1. **Alert ingest** — webhook/source adapters.
2. **Dedup + suppression** — fingerprint + window.
3. **Correlation** — group into incidents by time/service/symptom.
4. **Classification** — noise / informational / actionable ML model.
5. **Routing** — rules engine with on-call calendar.
6. **Context enrichment** — attach recent deploys, related metrics.
7. **Feedback loop** — resolve with label; retrains classifier.
8. **Runbook link-up** — attach RAG match from runbook store.
9. **Noise analytics** — per-rule noise ratios, worst offenders.
10. **Mute windows** — maintenance scheduling.

**Domain Model:**
- `AlertSource`: id, tenantId, name, type, config, lastSeenAt
- `Alert`: id, sourceId, fingerprint, severity, title, description, labels, firedAt, resolvedAt, status
- `Incident`: id, status, severity, startedAt, resolvedAt, alertIds[], ownerId
- `Correlation`: id, incidentId, alertId, correlationType (TEMPORAL|ENTITY|SYMPTOM), score
- `NoiseClassification`: id, alertId, label (NOISE|INFO|ACTIONABLE), modelVersion, confidence, humanOverride
- `RoutingRule`: id, condition, routeTo, priority
- `OnCallShift`: id, userId, startsAt, endsAt
- `MuteWindow`: id, matcher, startsAt, endsAt, reason
- `Runbook`: id, title, contentMarkdown, embedding (pgvector)

**REST API (23 endpoints):**
```
POST   /sources
POST   /alerts/ingest                       webhook
GET    /alerts
GET    /alerts/{id}
POST   /alerts/{id}/resolve                 {label}
POST   /incidents
GET    /incidents
GET    /incidents/{id}
POST   /incidents/{id}/add-alert
POST   /incidents/{id}/resolve
POST   /classify                            {alertId}
POST   /route                                {alertId}
POST   /routing-rules
GET    /routing-rules
POST   /oncall/shifts
GET    /oncall/current
POST   /mute-windows
GET    /mute-windows
POST   /runbooks
GET    /runbooks/search?q=
POST   /feedback                             {alertId, label}
POST   /models/retrain
GET    /analytics/noise-ratio
GET    /analytics/top-noisy
```

**AI pipelines:**
- **Correlation**: embeddings of labels + temporal bucket + same service key; score pairs.
- **Classification**: features = label bag, recent resolution history; logistic / lightgbm + LLM fallback for low-confidence.
- **Runbook match**: semantic search over runbook embeddings.

**Integration manifest:**
```json
{
  "app":"alertintelligence","displayName":"AlertIntelligence","version":"0.0.1",
  "baseUrl":"http://localhost:8157","healthEndpoint":"/actuator/health",
  "emits":["alert.fired","alert.classified","incident.opened","incident.resolved"],
  "consumes":["deploy.completed","metric.anomaly","service.degraded"],
  "capabilities":[
    {"name":"ingest-alert","input":"payload","output":"ack"},
    {"name":"classify","input":"alertId","output":"label+confidence"},
    {"name":"find-runbook","input":"alertId","output":"runbooks[]"}
  ]
}
```

**Frontend pages:** `/` live feed, `/incidents`, `/incidents/[id]`, `/runbooks`, `/rules`, `/analytics`, `/oncall`.

**App-specific AC:**
11. 100 alerts in 5 minutes across same service: dedup + correlation yields 1 incident.
12. Classifier accuracy ≥80% on labeled test set.
13. Mute-window suppresses matching alerts for the duration.
14. Feedback labels persist and are included in next retrain.
15. Runbook search returns top-3 matches for a seeded alert title.
16. Routing rule for sev=critical triggers Slack webhook (mock asserted).
17. Noise-ratio analytics computes per-rule ratio correctly on fixture.

---

## 9. DataQualityAI — port 8158

**Tagline:** Catch bad data before it reaches a dashboard

**Solution:** Continuous DQ monitoring across datasets: schema drift, null-rate anomalies, referential integrity, distribution shift. AI traces root cause.

**Core Features:**
1. **Test registry** — built-in + custom DQ tests.
2. **Schedule** — per-asset cadence.
3. **Anomaly detection** — baseline + z-score on numeric aggregates.
4. **Schema drift** — snapshot hash per run.
5. **Referential integrity** — FK violation scans.
6. **Distribution shift** — PSI / KS test against baseline.
7. **Scorecards** — per-table quality score 0–100.
8. **Auto-ticket** — create ticket on owner when score drops.
9. **Auto-remediation hints** — Claude suggests fix from failure + history.
10. **SLA dashboards** — track per-domain DQ over time.

**Domain Model:**
- `Dataset`: id, tenantId, fqn, ownerId, slaScore
- `DqTest`: id, datasetId, type (NOT_NULL|UNIQUE|ACCEPTED_VALUES|RANGE|FK|CUSTOM_SQL|DISTRIBUTION_SHIFT|SCHEMA_DRIFT), config, enabled
- `TestRun`: id, testId, executedAt, status (PASS|FAIL|ERROR), observedJson, expectedJson
- `QualityScore`: id, datasetId, score, computedAt, breakdownJson
- `Issue`: id, testRunId, severity, description, rootCauseHypothesis, ownerNotifiedAt
- `BaselineStat`: id, datasetId, columnName, mean, std, p50, p95, minVal, maxVal, capturedAt
- `RemediationHint`: id, issueId, hint, sourceEvidenceJson

**REST API (20 endpoints):**
```
POST   /datasets
GET    /datasets
GET    /datasets/{id}
POST   /datasets/{id}/tests
GET    /datasets/{id}/tests
POST   /tests/{id}/run                       manual
POST   /tests/{id}/enable
POST   /tests/{id}/disable
GET    /tests/{id}/runs
GET    /runs/{id}
POST   /datasets/{id}/baseline/refresh
GET    /datasets/{id}/baseline
POST   /datasets/{id}/score/recompute
GET    /datasets/{id}/score
GET    /issues                               filter by severity/owner
GET    /issues/{id}
POST   /issues/{id}/assign
POST   /issues/{id}/resolve
POST   /ai/suggest-fix                       {issueId}
GET    /analytics/score-history/{datasetId}
```

**AI pipelines:**
- **Suggest-fix**: issue + recent failed runs + column baseline → Claude suggests SQL fix or upstream check.
- **RCA**: correlate failure with recent schema changes / upstream failures.

**Integration manifest:**
```json
{
  "app":"dataqualityai","displayName":"DataQualityAI","version":"0.0.1",
  "baseUrl":"http://localhost:8158","healthEndpoint":"/actuator/health",
  "emits":["dq.test.failed","dq.score.degraded","dq.schema.drift","dq.issue.opened"],
  "consumes":["asset.loaded","schema.changed","pipeline.completed"],
  "capabilities":[
    {"name":"run-tests","input":"datasetId","output":"testRuns[]"},
    {"name":"get-score","input":"datasetId","output":"score"},
    {"name":"suggest-fix","input":"issueId","output":"hint"}
  ]
}
```

**Frontend pages:** `/datasets`, `/datasets/[id]` (score + tests + history), `/issues`, `/tests/new`.

**App-specific AC:**
11. Seed 5 tests on dataset; run all with `run-tests`; correct pass/fail states.
12. Inject a null in a NOT_NULL column; next run produces FAIL issue.
13. Distribution shift test detects PSI > 0.2 on a deliberately shifted fixture.
14. Score recompute drops by >10 pts when two critical tests fail.
15. Auto-ticket notifies owner (mock webhook asserted).
16. Suggest-fix returns actionable SQL referencing the failing column.
17. Score history returns time series for 30 days.

---

## 10. ReportAutomator — port 8159

**Tagline:** Board packs, ops reviews, daily emails — automated end-to-end

**Solution:** Connect data sources, build report templates once, AI populates them on schedule and delivers to recipients with narrative commentary.

**Core Features:**
1. **Template authoring** — Markdown+placeholders + data-binding (dataset query or metric name).
2. **Renderer** — substitute placeholders with live data + Claude commentary.
3. **Chart generation** — server-side vega-lite.
4. **Scheduling** — cron, grace windows, retry.
5. **Delivery channels** — email (via cc-starter `notifications`), Slack, webhook, PDF export.
6. **Approvals** — optional human review step before send.
7. **Version history** — every rendered report archived and linkable.
8. **Personalization** — per-recipient filters (region, segment).
9. **Cost tracking** — AI + render cost per report.
10. **Template marketplace** — built-ins: weekly revenue, monthly ops, quarterly board.

**Domain Model:**
- `Template`: id, tenantId, name, contentMarkdown, bindingsJson, chartsJson, defaultRecipientsJson, approvalRequired
- `Schedule`: id, templateId, cronExpr, timezone, status
- `Run`: id, templateId, scheduleId, startedAt, completedAt, status, renderedContentRef, costCents
- `Delivery`: id, runId, channel, recipient, status, deliveredAt
- `Approval`: id, runId, approverId, status, comment
- `ChartArtifact`: id, runId, chartKey, imageRef, vegaSpecJson

**REST API (19 endpoints):**
```
POST   /templates
GET    /templates
GET    /templates/{id}
PUT    /templates/{id}
POST   /templates/{id}/preview               with sample data
POST   /templates/{id}/render                adhoc render
POST   /schedules
GET    /schedules
POST   /schedules/{id}/pause
POST   /schedules/{id}/resume
POST   /runs/{id}/approve
POST   /runs/{id}/reject
POST   /runs/{id}/deliver                    manual send
GET    /runs
GET    /runs/{id}                            with delivery statuses
GET    /runs/{id}/pdf
GET    /runs/{id}/markdown
POST   /ai/draft-template                    nl prompt → template skeleton
GET    /costs
```

**AI pipelines:**
- **Commentary generation**: current period stats + prior period → 2–3 paragraph summary.
- **Template drafting**: "weekly ops review" → skeleton with sections + bindings.

**Integration manifest:**
```json
{
  "app":"reportautomator","displayName":"ReportAutomator","version":"0.0.1",
  "baseUrl":"http://localhost:8159","healthEndpoint":"/actuator/health",
  "emits":["report.rendered","report.delivered","approval.requested"],
  "consumes":["metric.updated","dataset.refreshed"],
  "capabilities":[
    {"name":"render","input":"templateId","output":"reportRef"},
    {"name":"deliver","input":"runId+channel","output":"status"},
    {"name":"draft-template","input":"description","output":"markdown"}
  ]
}
```

**Frontend pages:** `/templates`, `/templates/[id]/edit` (split markdown + preview), `/schedules`, `/runs`, `/approvals`, `/costs`.

**App-specific AC:**
11. Preview renders within <3 s for a 10-placeholder template.
12. Schedule fires at set cron and creates a run (tested with shortened cron + wall-clock wait).
13. Approval-required template blocks auto-delivery until `approve`.
14. Chart artifact produced as PNG for a vega-lite spec bound to a dataset query.
15. PDF export download byte count >0; file opens as valid PDF.
16. AI-drafted template produces valid template with ≥3 bindings.
17. Cost tracker records tokens + approximate USD for a rendered run.

---

## 11. EthicsMonitor — port 8160

**Tagline:** ML ethics, bias, drift — for EU AI Act compliance

**Problem:** Companies ship ML models affecting hiring, pricing, credit without ongoing bias/drift monitoring. EU AI Act (2026) mandates this for high-risk systems.

**Solution:** Register models, monitor performance + fairness + drift over time, generate model cards + compliance docs.

**Core Features:**
1. **Model registry** — name, purpose, risk tier (EU AI Act), owners, dataset refs.
2. **Performance metrics** — per-slice accuracy/precision/recall/F1/AUC.
3. **Fairness metrics** — demographic parity, equal opportunity, equalized odds across protected attrs.
4. **Drift detection** — input drift (PSI), output drift, concept drift (label feedback).
5. **Model card gen** — markdown + structured JSON templates matching NIST AI RMF / EU AI Act Annex IV.
6. **Incident log** — every fairness/drift violation persisted.
7. **Gating** — prevent new deployment if fairness regresses.
8. **Decision log** — sample + log every prediction with input features (configurable sampling).
9. **Auditor exports** — sign-off ready PDF bundle.
10. **Notifications** — per-violation routing.

**Domain Model:**
- `Model`: id, tenantId, name, purpose, riskTier (MINIMAL|LIMITED|HIGH|UNACCEPTABLE), ownersJson, intendedUses, prohibitedUses, version, status
- `Dataset`: id, name, description, piiFlag, role (TRAIN|VAL|TEST|PROD)
- `PerformanceMetric`: id, modelId, name, value, slice, evaluatedAt
- `FairnessMetric`: id, modelId, metric (DEM_PARITY|EQ_OPP|EQ_ODDS), protectedAttr, groupA, groupB, value, threshold, passed, evaluatedAt
- `DriftSignal`: id, modelId, kind (INPUT|OUTPUT|CONCEPT), feature, baselineStats, currentStats, score, passed, evaluatedAt
- `PredictionLog`: id, modelId, inputFeaturesJson, prediction, groundTruth, occurredAt, sampledProb
- `ModelCard`: id, modelId, version, contentMarkdown, contentJson, generatedAt
- `Violation`: id, modelId, type, severity, detail, createdAt, resolvedAt
- `AuditBundle`: id, modelId, windowStart, windowEnd, artifactsRef, generatedAt

**REST API (24 endpoints):**
```
POST   /models
GET    /models
GET    /models/{id}
PUT    /models/{id}
POST   /models/{id}/datasets
POST   /models/{id}/performance              batch metrics
POST   /models/{id}/fairness                 compute
GET    /models/{id}/fairness
POST   /models/{id}/drift/input              {currentStats}
POST   /models/{id}/drift/output
POST   /models/{id}/drift/concept            {groundTruthBatch}
POST   /predictions/log                      async ingest
GET    /predictions/{modelId}                paginated
POST   /models/{id}/card/generate
GET    /models/{id}/card
POST   /models/{id}/gate/deploy              enforce guardrails
GET    /violations
GET    /violations/{id}
POST   /violations/{id}/acknowledge
POST   /audit/bundles                        build bundle
GET    /audit/bundles/{id}
GET    /audit/bundles/{id}/download          zip
POST   /ai/explain-violation                 claude summary
POST   /ai/recommend-remediation
GET    /compliance/eu-ai-act-checklist       current status
```

**AI pipelines:**
- **Model card**: model metadata + datasets + metrics → markdown matching EU AI Act Annex IV outline.
- **Explain violation**: Claude writes human-readable summary of which group/metric/threshold tripped.
- **Remediation**: suggest sampling/reweighting/algorithmic fairness options for the violation type.

**Integration manifest:**
```json
{
  "app":"ethicsmonitor","displayName":"EthicsMonitor","version":"0.0.1",
  "baseUrl":"http://localhost:8160","healthEndpoint":"/actuator/health",
  "emits":["fairness.violation","drift.detected","model.card.generated","deploy.blocked"],
  "consumes":["prediction.logged","dataset.updated","deploy.requested"],
  "capabilities":[
    {"name":"check-fairness","input":"modelId","output":"report"},
    {"name":"check-drift","input":"modelId+kind","output":"score"},
    {"name":"generate-card","input":"modelId","output":"markdown+json"},
    {"name":"gate-deploy","input":"modelId","output":"allowed|reason"}
  ]
}
```

**Frontend pages:** `/models`, `/models/[id]` (tabs: overview, performance, fairness, drift, card, violations), `/violations`, `/audit`, `/compliance`.

**App-specific AC:**
11. Register a model; fairness endpoint computes demographic parity for 2 groups from a provided batch.
12. Drift input kind: PSI > 0.25 on provided fixture yields `drift.detected` event.
13. Gate-deploy blocks a model with failing fairness threshold.
14. Model card generator outputs markdown with ≥8 EU AI Act Annex IV sections present.
15. Audit bundle download returns a zip containing card, violations, metrics CSV.
16. Sampling config at 5% logs ~5% of submitted predictions.
17. Remediation endpoint recommendation mentions at least one technique (reweighting, threshold adjust, etc.).

---

## Cluster F: Implementation Strategy

**Parallel Build with Jules — single wave of 11 sessions.** Each session runs ~2 h with the rich prompts from `scripts/dispatch-cluster-f.sh`.

**Ports:**
| App | Port | | App | Port |
|-----|------|---|-----|------|
| DataStoryTeller   | 8150 | | CustomerSignal    | 8155 |
| PipelineGuardian  | 8151 | | ForecastAI        | 8156 |
| MetricsHub        | 8152 | | AlertIntelligence | 8157 |
| DataCatalogAI     | 8153 | | DataQualityAI     | 8158 |
| ExperimentEngine  | 8154 | | ReportAutomator   | 8159 |
|                   |      | | EthicsMonitor     | 8160 |

**Common Setup (every app):**
1. Run `./tools/scaffold-app.sh <app> "<Display>" <port>` first.
2. Add `cc-starter` dependencies per spec's *Key cc-starter modules*.
3. Register with Nexus Hub at startup via `tools/register-app.sh` equivalent.
4. Write Flyway migrations under `backend/src/main/resources/db/migration/`.
5. Wire LiteLLM client (`http://localhost:4000`) + Claude Sonnet 4.6.
6. Produce OpenAPI via springdoc-openapi; expose Swagger UI.
7. Frontend scaffolding: Next.js 14 + Tailwind + shadcn/ui + typed API client.
8. Dockerfile + `infra/compose.f.yml` snippet.
9. README.md with setup + env.
10. Universal + app-specific AC must all pass before PR merge.

**Success bar:** Each app's PR must pass `mvn clean verify` + frontend `npm test` in GitHub Actions CI. Coverage ≥80% on service layer.
