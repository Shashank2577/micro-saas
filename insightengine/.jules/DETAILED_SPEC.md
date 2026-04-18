# InsightEngine - Detailed Specification

## 1. Overview
InsightEngine is an AI-powered insight discovery and alerting platform. It automatically surfaces important patterns, anomalies, and recommendations from data.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE metric_data (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    segment VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_metric_data_tenant_metric ON metric_data(tenant_id, metric_name, timestamp);

CREATE TABLE insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL, -- ANOMALY, CORRELATION, TREND, SEGMENT, COMPARISON
    title VARCHAR(255) NOT NULL,
    description TEXT,
    explanation TEXT,
    recommended_action TEXT,
    impact_score DOUBLE PRECISION NOT NULL, -- 0.0 to 1.0
    confidence_score DOUBLE PRECISION NOT NULL, -- 0.0 to 1.0
    metric_names TEXT[] NOT NULL,
    status VARCHAR(50) DEFAULT 'NEW', -- NEW, ACKNOWLEDGED, RESOLVED
    assigned_to UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_insights_tenant_score ON insights(tenant_id, impact_score DESC);

CREATE TABLE insight_comments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    insight_id UUID NOT NULL REFERENCES insights(id),
    user_id UUID NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE alert_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    metric_name VARCHAR(255),
    condition_type VARCHAR(50) NOT NULL, -- ANOMALY_SEVERITY, TREND_ACCELERATION, etc.
    threshold DOUBLE PRECISION NOT NULL,
    slack_channel VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE custom_discovery_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    definition JSONB NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3. REST Endpoints

### Insights API
- `GET /api/insights`: List insights (supports filtering by type, status, sorting by impact_score).
- `GET /api/insights/{id}`: Get single insight details.
- `PUT /api/insights/{id}/status`: Update insight status (e.g., to ACKNOWLEDGED).
- `POST /api/insights/{id}/assign`: Assign insight to a user.
- `POST /api/insights/{id}/comments`: Add a comment.
- `GET /api/insights/{id}/comments`: List comments for an insight.

### Alerts API
- `GET /api/alerts/rules`: List alert rules.
- `POST /api/alerts/rules`: Create an alert rule.
- `PUT /api/alerts/rules/{id}`: Update an alert rule.
- `DELETE /api/alerts/rules/{id}`: Delete an alert rule.

### Custom Rules API
- `GET /api/discovery/rules`: List custom discovery rules.
- `POST /api/discovery/rules`: Create custom rule.
- `DELETE /api/discovery/rules/{id}`: Delete custom rule.

### Data Ingestion API
- `POST /api/data/metrics`: Ingest new metric data.
- `POST /api/discovery/scan`: Manually trigger a discovery scan.

## 4. Services (Spring Boot)

### `InsightDiscoveryService`
- `void runContinuousDiscovery()`: Scheduled job to pull recent metric data, run statistical analysis (anomalies, correlations, trends), and generate Insights.
- `List<Insight> discoverAnomalies(UUID tenantId, LocalDateTime since)`
- `List<Insight> discoverCorrelations(UUID tenantId, LocalDateTime since)`

### `AiExplanationService`
- `InsightEnrichment generateExplanationAndRecommendation(InsightRawData data)`: Uses LiteLLM to generate contextual explanation and recommended action.

### `AlertService`
- `void evaluateAlertRules(Insight insight)`: Checks if a newly generated insight triggers any active alert rules.
- `void sendSlackNotification(String channel, Insight insight)`

## 5. React Components (Next.js)

### Pages
- `/`: Dashboard showing top ranked insights.
- `/insights`: Full list of insights with filtering.
- `/insights/[id]`: Detailed view of an insight, explanations, recommendations, and comments.
- `/alerts`: Manage alert rules.
- `/settings`: Manage custom discovery rules.

### Components
- `InsightCard`: Displays title, type, impact score, and snippet.
- `InsightDetail`: Shows full narrative, chart (if applicable), and recommended actions.
- `CommentSection`: For team collaboration on an insight.
- `AlertRuleForm`: Form to configure triggers and Slack integration.

## 6. AI Integration
- Client: `LiteLLMClient`
- Prompt Context: "You are an AI data analyst. Explain this statistical anomaly/trend in business terms and recommend an action..."

## 7. Acceptance Criteria & Tests
- [x] Background job generates insights and saves them to DB.
- [x] Insights are ranked by impact score (unit tested).
- [x] LiteLLM returns explanation and recommendation strings.
- [x] AlertService fires Slack notification (mocked) if threshold exceeded.
- [x] Multi-tenant isolation verified via `TenantContext` in repository tests.

