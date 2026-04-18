# IdentityCore - Detailed Specification

## 1. Overview
IdentityCore is an AI identity intelligence platform designed to continuously analyze access patterns, detect anomalies, surface overprivileged accounts, and automate quarterly access reviews. It relies on a pipeline for continuous access pattern analysis and an agent for access review automation.

## 2. Database Schema (PostgreSQL)

### Table: users
- `id` (UUID, PK)
- `tenant_id` (UUID)
- `email` (VARCHAR)
- `full_name` (VARCHAR)
- `department` (VARCHAR)
- `role` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: access_logs
- `id` (UUID, PK)
- `tenant_id` (UUID)
- `user_id` (UUID, FK to users)
- `resource_accessed` (VARCHAR)
- `action` (VARCHAR) - e.g., READ, WRITE, DELETE
- `ip_address` (VARCHAR)
- `access_time` (TIMESTAMP)
- `status` (VARCHAR) - e.g., SUCCESS, DENIED
- `created_at` (TIMESTAMP)

### Table: anomalies
- `id` (UUID, PK)
- `tenant_id` (UUID)
- `user_id` (UUID, FK to users)
- `access_log_id` (UUID, FK to access_logs)
- `anomaly_type` (VARCHAR) - e.g., UNUSUAL_TIME, EXCESSIVE_ACCESS, LATERAL_MOVEMENT
- `severity` (VARCHAR) - HIGH, MEDIUM, LOW
- `description` (TEXT)
- `status` (VARCHAR) - OPEN, RESOLVED, IGNORED
- `detected_at` (TIMESTAMP)
- `resolved_at` (TIMESTAMP, NULLABLE)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: access_reviews
- `id` (UUID, PK)
- `tenant_id` (UUID)
- `reviewer_id` (UUID, FK to users)
- `target_user_id` (UUID, FK to users)
- `review_period_start` (TIMESTAMP)
- `review_period_end` (TIMESTAMP)
- `status` (VARCHAR) - PENDING, IN_PROGRESS, COMPLETED
- `ai_recommendation` (TEXT) - JSON string containing recommended actions
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: privileges
- `id` (UUID, PK)
- `tenant_id` (UUID)
- `user_id` (UUID, FK to users)
- `resource_name` (VARCHAR)
- `permission_level` (VARCHAR)
- `last_used_at` (TIMESTAMP)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 3. REST API Endpoints

### Users
- `GET /api/users` - List users
- `GET /api/users/{id}` - Get user details
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user

### Access Logs
- `GET /api/access-logs` - List access logs (with filtering by user, resource, time range)
- `POST /api/access-logs` - Ingest new access log

### Anomalies
- `GET /api/anomalies` - List anomalies
- `GET /api/anomalies/{id}` - Get anomaly details
- `PUT /api/anomalies/{id}/status` - Update anomaly status (e.g., mark resolved)
- `POST /api/anomalies/analyze` - Trigger manual AI analysis of recent logs

### Access Reviews
- `GET /api/reviews` - List access reviews
- `POST /api/reviews` - Create/schedule an access review
- `GET /api/reviews/{id}` - Get review details
- `PUT /api/reviews/{id}` - Submit review decisions
- `POST /api/reviews/{id}/generate-recommendation` - Ask AI to generate a recommendation for the review

### Identity Hygiene
- `GET /api/reports/hygiene` - Generate an identity hygiene report for security leadership (AI summary of overall health, overprivileged accounts, and anomalies)

## 4. Service Layer Details
- **UserService**: Manages CRUD operations for users.
- **AccessLogService**: Ingests logs and triggers the `AnomalyDetectionPipeline`.
- **AnomalyService**: Handles anomaly resolution and status tracking.
- **AccessReviewService**: Generates and manages review campaigns. Uses AI agent to generate recommendations for removing/keeping privileges.
- **AIAnalysisService**: Integrates with LiteLLM to:
    1. Analyze an `AccessLog` (or batch of logs) against baseline behavior to detect anomalies.
    2. Analyze a user's privileges vs. usage (from `AccessLog`) to recommend least-privilege adjustments during an `AccessReview`.
    3. Generate the Identity Hygiene Report from aggregated data.

## 5. React Components
- **Dashboard**: High-level overview of open anomalies, pending reviews, and hygiene score.
- **AnomalyList**: Data table showing detected anomalies, sortable by severity and time.
- **AnomalyDetail**: View specific anomaly context and take action (resolve/ignore).
- **AccessReviewBoard**: Kanban-style or list view of pending access reviews.
- **AccessReviewForm**: Review interface showing AI recommendations side-by-side with current privileges.
- **HygieneReport**: Generates and displays the AI-authored identity hygiene report.

## 6. Testing Strategy
- **Unit Tests**: Mock `TenantContext` and repositories to test logic in `AnomalyService` and `AccessReviewService`.
- **Integration Tests**: Mock LiteLLM responses, test REST controllers with `@WebMvcTest`.
- **Frontend Tests**: Test `AccessReviewForm` rendering AI recommendations correctly.

## 7. Assumptions
- LiteLLM is used for both pipeline pattern analysis (batch anomaly detection) and agentic review automation.
- The `tenant_id` is passed via `X-Tenant-ID` header.
- Access Logs are ingested from an external system (e.g., Okta, AWS IAM) or mocked via the API.
