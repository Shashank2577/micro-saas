# ObservabilityAI Detailed Specification

## 1. Overview
ObservabilityAI is an AI-powered observability platform. It collects logs, metrics, and distributed traces, and uses AI to automatically correlate signals across the stack to answer "why is this slow?" or "what caused this error?" without manual spelunking.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE observability_signal (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    signal_type VARCHAR(50) NOT NULL, -- LOG, METRIC, TRACE
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    trace_id VARCHAR(255),
    span_id VARCHAR(255),
    payload JSONB NOT NULL,
    severity VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_obs_signal_tenant ON observability_signal(tenant_id);
CREATE INDEX idx_obs_signal_timestamp ON observability_signal(timestamp);
CREATE INDEX idx_obs_signal_trace ON observability_signal(trace_id);

CREATE TABLE observability_alert (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL, -- OPEN, RESOLVED
    ai_analysis JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_obs_alert_tenant ON observability_alert(tenant_id);
```

## 3. Backend Services & AI Integration
- **SignalIngestionService**: Handles incoming signals. Extracts traces/logs and stores them.
- **AICorrelationService**: When a spike or error rate occurs, or a user queries "what changed in the last hour?", it fetches signals and sends them to LiteLLM to generate root cause analysis.
- **AlertService**: Generates alerts based on AI analysis.

## 4. REST Endpoints
- `POST /api/signals`: Ingest logs/metrics/traces.
- `GET /api/signals?type={type}&service={service}`: Retrieve signals with filters.
- `GET /api/alerts`: Retrieve alerts.
- `POST /api/analyze`: Trigger AI analysis on a specific timeframe or trace ID.

## 5. Frontend Components (Next.js)
- **Dashboard**: High-level overview of metrics and recent alerts.
- **Signal Explorer**: Table/list to view and filter logs and traces.
- **AI Analysis View**: A chat-like or report-style component showing AI-generated root cause analysis.
- **Alerts View**: List of currently open alerts.

## 6. Acceptance Criteria
- Can ingest a signal successfully and store it with tenant context.
- AI analysis can be triggered, and it returns a mock correlation report.
- Can retrieve alerts and signals filtered by tenant ID.
- Components render correctly with mock data if needed.
