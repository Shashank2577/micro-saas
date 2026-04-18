# QueryOptimizer - Detailed Specification

## Overview
QueryOptimizer is an intelligent query optimization and performance tuning platform. It helps teams identify slow queries, understand execution plans, and apply optimization recommendations.

## Database Schema (PostgreSQL)

### Table: query_fingerprints
Groups similar queries.
```sql
CREATE TABLE query_fingerprints (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    fingerprint_hash VARCHAR(255) NOT NULL,
    normalized_query TEXT NOT NULL,
    database_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, fingerprint_hash)
);
```

### Table: query_executions
Logs of individual query executions.
```sql
CREATE TABLE query_executions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id),
    raw_query TEXT NOT NULL,
    execution_time_ms DOUBLE PRECISION NOT NULL,
    database_user VARCHAR(255),
    executed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    execution_plan TEXT, -- JSON representation
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### Table: query_recommendations
AI-powered recommendations for fingerprints.
```sql
CREATE TABLE query_recommendations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id),
    recommendation_type VARCHAR(50) NOT NULL, -- INDEX, REWRITE, CACHE
    description TEXT NOT NULL,
    confidence_score DOUBLE PRECISION NOT NULL,
    impact_estimate TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### Table: index_suggestions
Index advisor suggestions.
```sql
CREATE TABLE index_suggestions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id),
    table_name VARCHAR(255) NOT NULL,
    columns_suggested TEXT NOT NULL,
    creation_statement TEXT NOT NULL,
    estimated_improvement DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

## REST API Endpoints

### 1. `POST /api/queries/upload`
Upload a slow query log file (or JSON array).
**Request:** `MultipartFile file` or JSON array of `QueryLogEntry`.
**Response:** `202 Accepted` with `UploadResponse` (jobs queued).

### 2. `GET /api/queries/fingerprints`
List query fingerprints with baseline stats.
**Request:** `page`, `size`, `sort`
**Response:** `Page<QueryFingerprintDTO>` (includes average latency, count).

### 3. `GET /api/queries/fingerprints/{id}`
Get details of a specific fingerprint, including recommendations and recent executions.
**Response:** `FingerprintDetailsDTO`

### 4. `POST /api/queries/fingerprints/{id}/analyze`
Trigger AI analysis for a fingerprint.
**Response:** `202 Accepted`

### 5. `GET /api/recommendations`
List all recommendations across fingerprints.
**Response:** `List<RecommendationDTO>`

### 6. `PUT /api/recommendations/{id}/status`
Update recommendation status (e.g., APPLIED, REJECTED).
**Request:** `{ "status": "APPLIED" }`
**Response:** `200 OK`

### 7. `GET /api/indexes`
List index suggestions.
**Response:** `List<IndexSuggestionDTO>`

## Service Layer

- `QueryAnalysisService`: Handles parsing uploaded logs, generating fingerprints (grouping similar queries), and saving executions.
- `ExecutionPlanService`: Parses raw execution plans (EXPLAIN output) into visualizable JSON trees.
- `AIRecommendationService`: Uses `LiteLLMClient` to send normalized queries and plans to an LLM, asking for rewrite suggestions, index suggestions, and confidence scores.
- `BaselineCalculationService`: Calculates average latency, P95, and P99 for fingerprints based on executions.
- `OptimizationSchedulingService`: Scheduled tasks to automatically analyze high-volume slow queries.

## AI Integration (LiteLLM)
- **Client:** `LiteLLMClient` via `RestTemplate`
- **Prompt:** "You are an expert database administrator. Analyze the following SQL query and its execution plan. Provide recommendations in JSON format containing: recommendations (type, description, confidence, impact), index_suggestions (table, columns, sql, estimated_improvement), and rewritten_query."

## Frontend Components (Next.js)
- **Layout:** Standard sidebar navigation.
- **Pages:**
  - `/` (Dashboard): Overview of total slow queries, top bottlenecks.
  - `/fingerprints`: Table of query fingerprints sorted by impact.
  - `/fingerprints/[id]`: Detail view, execution plan tree visualization, AI recommendations, and history chart.
  - `/recommendations`: Central hub for all AI recommendations.
  - `/indexes`: Index advisor list.
  - `/upload`: Drag-and-drop file upload for slow logs.
- **Components:**
  - `PlanTreeViewer`: Renders execution plan JSON.
  - `TrendChart`: Chart.js component for execution times over time.
  - `RecommendationCard`: Displays recommendation details and confidence score.

## Acceptance Criteria Handled
- User can upload slow query log: `/upload` page and API.
- System analyzes 100+ queries and groups: `QueryAnalysisService`.
- Performance baseline calculated: `BaselineCalculationService`.
- Execution plan visualized: `PlanTreeViewer` component.
- AI recommends 3+ optimizations: `AIRecommendationService` with LiteLLM.
- Index advisor: `index_suggestions` table and endpoint.
- User can compare before/after: Trend analysis via executions over time.
- Team collaboration: updating recommendation status (`status` field).
- Multi-tenant isolation: `tenant_id` on all tables.

