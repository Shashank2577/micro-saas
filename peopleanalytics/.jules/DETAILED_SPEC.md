# PeopleAnalytics Detailed Spec

## Overview
PeopleAnalytics is an employee analytics and performance insights platform designed to aggregate HR data and provide insights on employee performance, engagement, retention, and team dynamics.

## Entities and Database Schema

All entities are tenant-aware (contain a `tenant_id` UUID) and have audit fields (`created_at`, `updated_at`).

### 1. `employees`
Stores employee basic information.
```sql
CREATE TABLE employees (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_number VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    role VARCHAR(100),
    manager_id UUID,
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE, LEAVE
    hire_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_employees_tenant ON employees(tenant_id);
```

### 2. `performance_metrics`
Stores KPI tracking, review scores, and goals for employees.
```sql
CREATE TABLE performance_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    metric_type VARCHAR(50) NOT NULL, -- KPI, REVIEW, GOAL
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(10,2) NOT NULL,
    metric_date DATE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_perf_metrics_employee ON performance_metrics(employee_id);
```

### 3. `engagement_scores`
Stores calculated engagement scores.
```sql
CREATE TABLE engagement_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    score DECIMAL(5,2) NOT NULL, -- 0.0 to 100.0
    calculated_at TIMESTAMP NOT NULL,
    factors JSONB, -- JSON representation of factors
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_eng_scores_employee ON engagement_scores(employee_id);
```

### 4. `pulse_surveys`
Defines surveys distributed to employees.
```sql
CREATE TABLE pulse_surveys (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    questions JSONB NOT NULL, -- List of questions
    status VARCHAR(50) NOT NULL, -- DRAFT, ACTIVE, CLOSED
    distributed_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_pulse_surveys_tenant ON pulse_surveys(tenant_id);
```

### 5. `survey_responses`
Stores responses from employees.
```sql
CREATE TABLE survey_responses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    survey_id UUID NOT NULL REFERENCES pulse_surveys(id),
    employee_id UUID NOT NULL REFERENCES employees(id),
    responses JSONB NOT NULL, -- Key-value answers
    submitted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_survey_responses_survey ON survey_responses(survey_id);
```

### 6. `team_health_metrics`
Aggregated metrics at the department/team level.
```sql
CREATE TABLE team_health_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    department VARCHAR(100) NOT NULL,
    health_score DECIMAL(5,2) NOT NULL,
    collaboration_score DECIMAL(5,2) NOT NULL,
    productivity_score DECIMAL(5,2) NOT NULL,
    measured_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_team_health_dept ON team_health_metrics(tenant_id, department);
```

### 7. `retention_predictions`
Predicted flight risks for employees.
```sql
CREATE TABLE retention_predictions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    risk_score DECIMAL(5,2) NOT NULL, -- 0.0 to 1.0 (Probability of leaving)
    risk_level VARCHAR(50) NOT NULL, -- LOW, MEDIUM, HIGH
    factors JSONB NOT NULL,
    predicted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_retention_employee ON retention_predictions(employee_id);
```

### 8. `analytics_cache`
Stores expensive analytics query results with TTL.
```sql
CREATE TABLE analytics_cache (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    cache_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX idx_analytics_cache_key ON analytics_cache(tenant_id, cache_key);
```

## REST API Endpoints

### 1. Employees (`/api/employees`)
- `GET /api/employees` - List employees with filters (department, status).
- `POST /api/employees` - Create a new employee.
- `GET /api/employees/{id}` - Get employee details.
- `PUT /api/employees/{id}` - Update employee.
- `POST /api/employees/bulk-sync` - Sync HRIS data in bulk.

### 2. Performance Metrics (`/api/performance-metrics`)
- `GET /api/performance-metrics` - Get aggregated KPIs (can filter by employee/dept).
- `POST /api/performance-metrics` - Add new metric.

### 3. Engagement Scores (`/api/engagement`)
- `GET /api/engagement/trends` - Get historical engagement trends for dashboard.
- `POST /api/engagement/calculate` - Trigger manual scoring (calls LiteLLM).

### 4. Retention Risks (`/api/retention`)
- `GET /api/retention/risks` - Get top flight risk employees.
- `POST /api/retention/predict` - Trigger predictive model generation (calls LiteLLM).

### 5. Team Health (`/api/team-health`)
- `GET /api/team-health` - Get health metrics by department.

### 6. Pulse Surveys (`/api/surveys`)
- `GET /api/surveys` - List surveys.
- `POST /api/surveys` - Create a survey.
- `POST /api/surveys/{id}/distribute` - Distribute to employees (queues messages).
- `POST /api/surveys/{id}/responses` - Submit a response.
- `GET /api/surveys/{id}/analysis` - Get aggregated results.

### 7. Insights & Reports (`/api/reports`)
- `GET /api/reports/predictive-insights` - Calls Claude/GPT via LiteLLM for textual summary of the company's health.
- `GET /api/reports/export/pdf` - Export analytics report as PDF.
- `GET /api/reports/export/csv` - Export analytics data as CSV.

## Services & Logic
- `PerformanceAggregationService`: Consolidates KPIs.
- `EngagementScoringService`: Uses survey responses + AI to generate `score` and `factors`.
- `RetentionPredictionService`: Consumes `performance_metrics` and `engagement_scores`, queries AI provider (e.g. Scikit-learn/LiteLLM) to determine `risk_score`.
- `PulseSurveyService`: Handles survey CRUD and uses pgmq for distribution processing.
- `InsightsGenerationService`: Prepares the data payload and queries LiteLLM for summary textual insights.

## Frontend (Next.js) Components
- `DashboardPage`: Overview charts (Chart.js) showing KPI trends, Engagement scores, retention risk pie charts.
- `EmployeesPage`: Table of employees, click for details.
- `EmployeeDetail`: specific metrics for that employee.
- `TeamHealthPage`: Radar charts comparing departments.
- `SurveysPage`: Manage surveys.
- Components: `StatCard`, `TrendChart`, `RiskTable`, `SurveyForm`.

## AI Integration Points
- `/predictive-insights`: Uses `LiteLLM` to generate a narrative summary.
- `/retention/predict`: Uses `LiteLLM` to score flight risk based on metrics.

## Message Queue (pgmq)
- `pulse-survey-processing`: Queue for processing submitted responses asynchronously.
- `prediction-generation-queue`: Triggered daily via Scheduled jobs to run retention models.

## Security & Constraints
- Multi-tenancy enforced using `X-Tenant-ID` header.
- PII (first name, last name, email) encrypted at rest (using `AttributeConverter`).
- 1-hour TTL for analytics cache.

## Acceptance Criteria Handled
- Dashboards display 90 days of KPI trends.
- Weekly engagement score calculation available via cron/API.
- Predictive models for retention show probability.
- PDF and CSV reports exportable.
