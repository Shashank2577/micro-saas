# EngagementPulse Detailed Specification

## 1. Data Model (PostgreSQL)

All tables must include `tenant_id` for multi-tenancy isolation and standard auditing fields (`created_at`, `updated_at`).

```sql
CREATE TABLE surveys (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL, -- DRAFT, ACTIVE, COMPLETED
    scheduled_at TIMESTAMP,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE questions (
    id UUID PRIMARY KEY,
    survey_id UUID REFERENCES surveys(id),
    text TEXT NOT NULL,
    type VARCHAR(50) NOT NULL, -- RATING, FREE_TEXT
    order_index INT NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE survey_responses (
    id UUID PRIMARY KEY,
    survey_id UUID REFERENCES surveys(id),
    employee_id UUID NOT NULL,
    team_id UUID NOT NULL,
    submitted_at TIMESTAMP NOT NULL,
    engagement_score DECIMAL, -- Calculated aggregate score
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE answers (
    id UUID PRIMARY KEY,
    response_id UUID REFERENCES survey_responses(id),
    question_id UUID REFERENCES questions(id),
    rating_value INT, -- 1-5 for RATING questions
    text_value TEXT, -- For FREE_TEXT questions
    sentiment_score DECIMAL, -- Extracted via LiteLLM (-1.0 to 1.0)
    sentiment_label VARCHAR(50), -- POSITIVE, NEUTRAL, NEGATIVE
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL, -- LOW, MEDIUM, HIGH
    resolved BOOLEAN DEFAULT FALSE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE action_plans (
    id UUID PRIMARY KEY,
    alert_id UUID REFERENCES alerts(id),
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- SUGGESTED, IN_PROGRESS, COMPLETED
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 2. API Endpoints

### Surveys
- `POST /api/surveys`: Create a new survey.
  - Request: `{"title": "...", "description": "...", "questions": [{"text": "...", "type": "RATING", "orderIndex": 1}]}`
  - Response: Created Survey.
- `GET /api/surveys`: List surveys.
- `GET /api/surveys/{id}`: Get survey details.
- `POST /api/surveys/{id}/distribute`: Distribute survey (async job).

### Responses
- `POST /api/responses`: Submit a survey response.
  - Request: `{"surveyId": "...", "employeeId": "...", "teamId": "...", "answers": [{"questionId": "...", "ratingValue": 4, "textValue": null}]}`
  - Response: Created Response. The backend will asynchronously calculate sentiment for text answers and update engagement score.

### Analytics & Reports
- `GET /api/analytics/teams/{teamId}`: Get real-time engagement score and sentiment breakdown for a team.
- `GET /api/analytics/trends`: Get historical engagement trends over time.
- `GET /api/reports/export`: Download report as CSV (returns a string payload or binary file response).

### Alerts & Actions
- `GET /api/alerts`: Get unresolved alerts for low engagement.
- `POST /api/alerts/{id}/resolve`: Mark alert resolved.
- `POST /api/alerts/{alertId}/action-plans`: Add an action plan to an alert.

## 3. Services and AI Integration
- `SurveyService`: Manages survey CRUD and distribution scheduling.
- `ResponseProcessingService`: Processes submissions, calculates `engagement_score` (average of ratings normalized to 0-100), and invokes `SentimentAnalysisService`.
- `SentimentAnalysisService`: Uses LiteLLM to analyze `text_value` in answers.
  - Prompt: "Analyze the following employee feedback and classify its sentiment as POSITIVE, NEUTRAL, or NEGATIVE, along with a score from -1.0 to 1.0. Feedback: '{text}'"
  - Updates the answer record with the result.
- `AlertingService`: Periodically checks average engagement scores per team. If a team's score drops below a threshold (e.g., 60/100), it creates an Alert.

## 4. Frontend Components (Next.js)

### Pages
- `/`: Dashboard showing overall company engagement score, trend line chart, and recent alerts.
- `/surveys`: List of surveys, "Create Survey" button.
- `/surveys/create`: Form builder with dynamic questions array.
- `/reports`: Detailed charts for team comparisons and CSV export button.
- `/alerts`: Inbox-style view of manager alerts and action planning forms.

### Components
- `ScoreCard`: Displays a large numeric score with a trend indicator (up/down arrow).
- `TrendChart`: Line chart (react-chartjs-2) showing engagement over time.
- `SurveyFormBuilder`: Complex component for managing `questions` state.
- `SentimentBadge`: UI pill showing "Positive" (green), "Neutral" (gray), "Negative" (red).

## 5. Security and Tenancy
- Standard `@TenantId` filtering on all JPA queries.
- `X-Tenant-ID` header required on all REST API calls.
- Controller integration tests will mock `SecurityContext` and provide valid `TenantContext`.

## 6. Acceptance Criteria Mapped
1. Admin can create survey -> `/api/surveys`
2. Survey sent to target audience -> `POST /api/surveys/{id}/distribute` (mocked async worker)
3. Real-time engagement score -> `ResponseProcessingService`
4. Sentiment analysis -> `SentimentAnalysisService` via LiteLLM
5. Team dashboards -> `/api/analytics/teams/{teamId}`
6. Trend analysis -> `/api/analytics/trends`
7. Alerts -> `AlertingService` and `/api/alerts`
8. Multi-tenant -> `cc-starter`
9. Export -> `/api/reports/export`
