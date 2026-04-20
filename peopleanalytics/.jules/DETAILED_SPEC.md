# Detailed Specification - PeopleAnalytics

## 1. Overview
PeopleAnalytics is an employee analytics and performance insights platform. It aggregates HR data to provide insights on performance, engagement, retention, and team dynamics.

## 2. Data Model (Entities)
All entities will include `tenant_id` for multi-tenancy.

- **Employee**
  - `id`: UUID (Primary Key)
  - `tenant_id`: UUID
  - `external_id`: String (HRIS ID)
  - `first_name`: String (Encrypted)
  - `last_name`: String (Encrypted)
  - `email`: String (Encrypted)
  - `department`: String
  - `role`: String
  - `manager_id`: UUID (Self-reference)
  - `hire_date`: LocalDate
  - `status`: String (ACTIVE, INACTIVE)
  - `created_at`: OffsetDateTime
  - `updated_at`: OffsetDateTime

- **PerformanceMetric**
  - `id`: UUID
  - `tenant_id`: UUID
  - `employee_id`: UUID
  - `metric_type`: String (GOAL, REVIEW, KPI)
  - `value`: Double
  - `target`: Double
  - `period`: String (e.g., "2024-Q1")
  - `date`: LocalDate
  - `metadata`: JSONB

- **EngagementScore**
  - `id`: UUID
  - `tenant_id`: UUID
  - `employee_id`: UUID
  - `score`: Double (0-100)
  - `calculated_at`: OffsetDateTime
  - `source`: String (SURVEY, ACTIVITY, etc.)

- **PulseSurvey**
  - `id`: UUID
  - `tenant_id`: UUID
  - `title`: String
  - `description`: String
  - `status`: String (DRAFT, ACTIVE, CLOSED)
  - `created_at`: OffsetDateTime

- **SurveyResponse**
  - `id`: UUID
  - `tenant_id`: UUID
  - `survey_id`: UUID
  - `employee_id`: UUID
  - `score`: Integer
  - `feedback`: String (Encrypted)
  - `submitted_at`: OffsetDateTime

- **TeamHealthMetric**
  - `id`: UUID
  - `tenant_id`: UUID
  - `department`: String
  - `team_id`: String
  - `collaboration_score`: Double
  - `productivity_signal`: Double
  - `measured_at`: OffsetDateTime

- **RetentionPrediction**
  - `id`: UUID
  - `tenant_id`: UUID
  - `employee_id`: UUID
  - `risk_score`: Double (0.0 to 1.0)
  - `risk_level`: String (LOW, MEDIUM, HIGH)
  - `factors`: JSONB (AI-generated reasons)
  - `predicted_at`: OffsetDateTime

- **AnalyticsCache**
  - `id`: UUID
  - `tenant_id`: UUID
  - `cache_key`: String
  - `cache_value`: JSONB
  - `expires_at`: OffsetDateTime

## 3. API Endpoints
Base path: `/api/v1/people-analytics`

- **Employee API**
  - `GET /employees`: List employees
  - `POST /employees/sync`: HRIS bulk sync
- **Performance API**
  - `GET /performance/dashboard`: KPI trends (90 days)
  - `POST /performance/metrics`: Ingest metrics
- **Engagement API**
  - `GET /engagement/scores`: Get engagement scores
  - `POST /engagement/surveys`: Create pulse survey
  - `POST /engagement/surveys/{id}/respond`: Submit response
- **Retention API**
  - `GET /retention/risk`: Identify high-risk employees
  - `GET /retention/forecasting`: Turnover probability
- **Reporting API**
  - `GET /reports/team-health`: Departmental health reports
  - `GET /reports/export/pdf`: PDF export
  - `GET /reports/export/csv`: CSV export

## 4. Services
- `PerformanceAggregationService`: Aggregates metrics into KPI trends.
- `EngagementScoringService`: Weekly calculation of scores from survey/activity data.
- `RetentionPredictionService`: Flight risk predictive modeling via LiteLLM.
- `TeamHealthAnalysisService`: Generates team health signals.
- `PulseSurveyService`: Handles survey lifecycle and async response processing (pgmq).
- `InsightsGenerationService`: AI-powered narrative generation from data.

## 5. Technology Stack
- **Backend**: Spring Boot 3.3.5, Hibernate, PostgreSQL, Flyway, pgmq, Spring Batch.
- **Frontend**: Next.js 15, React Query, Chart.js, Tailwind CSS.
- **AI**: LiteLLM (Claude/GPT).
- **Security**: Keycloak (RBAC), PII encryption at rest.

## 6. Acceptance Criteria Verification
- [ ] Performance dashboard displays last 90 days KPI trends.
- [ ] Weekly engagement score updates.
- [ ] 70%+ accuracy in retention risk identification (tested via mock AI data).
- [ ] Pulse survey creation and analysis workflow.
- [ ] Bulk HRIS data sync endpoint.
- [ ] PDF/CSV export functionality.
- [ ] Multi-tenant isolation verified in all queries.
