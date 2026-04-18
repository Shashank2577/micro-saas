# UsageIntelligence Detailed Spec

## Overview
UsageIntelligence is an AI product analytics platform built for the ecosystem. It provides feature adoption tracking, user journey mapping, activation funnel analysis, feature flag impact measurement, and cohort analysis. AI surfaces the "why" behind usage patterns, not just the "what."

## Domain Model
```sql
CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    properties JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    dimensions JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    recommendation TEXT NOT NULL,
    data_references JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS cohorts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    criteria JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## Backend API

- `POST /api/events` - Track a new event.
  - Body: `{ "userId": "string", "eventName": "string", "properties": {} }`
  - Returns: 201 Created
- `GET /api/metrics` - Retrieve aggregated metrics (MAU, DAU, specific events).
  - Query Params: `metricName`, `startDate`, `endDate`, `interval`
  - Returns: `List<MetricValue>`
- `GET /api/cohorts` - Retrieve all cohorts.
  - Returns: `List<Cohort>`
- `POST /api/cohorts` - Create a new cohort based on criteria.
  - Body: `{ "name": "string", "description": "string", "criteria": {} }`
  - Returns: `Cohort`
- `GET /api/insights` - Get AI-generated insights based on recent events and metrics.
  - Returns: `List<AiInsight>`

## AI Integration
- When events/metrics indicate significant changes (e.g., sudden drop in usage or a spike in a specific feature's use), the backend will query LiteLLM with aggregated data to generate an `AiInsight`.
- Prompt structure: "Given the following usage data over the last 7 days: {data}. Identify any anomalies or interesting trends, explain why they might be happening, and provide a recommendation."

## Frontend Components
- **DashboardPage**: Shows top-level metrics (DAU, MAU, Total Events) using `react-chartjs-2`.
- **EventsPage**: Shows a feed of recent events and allows filtering by event name.
- **CohortsPage**: Manage user cohorts. Create/Edit/Delete.
- **InsightsPage**: Displays AI-generated insights and recommendations.

## Acceptance Criteria
- Events can be tracked via REST API.
- Metrics can be retrieved over a specified time period.
- AI insights can be generated based on recent data.
- Dashboard displays metrics in charts.
- All API endpoints and JPA entities correctly handle multi-tenancy (`tenant_id`).
