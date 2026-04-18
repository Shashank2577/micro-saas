# TenantManager Detailed Specification

## 1. Overview
TenantManager is an AI tenant operations platform that allows SaaS builders in the ecosystem to manage their end customers (tenants). It monitors health per tenant across deployed apps, automates onboarding with configurable milestones, tracks usage analytics, predicts churn using AI patterns, and manages tenant provisioning and deprovisioning.

## 2. Domain Entities

### `CustomerTenant`
Represents an end customer using the ecosystem apps.
- `id`: UUID (Primary Key)
- `tenant_id`: UUID (System tenant ID for isolation)
- `external_tenant_id`: UUID (The UUID that the customer uses in their requests)
- `name`: String
- `status`: String (ACTIVE, INACTIVE, ONBOARDING, DEPROVISIONING)
- `health_score`: Integer (0-100)
- `churn_risk`: String (LOW, MEDIUM, HIGH)
- `created_at`: Timestamp
- `updated_at`: Timestamp

### `OnboardingMilestone`
Represents a step in the onboarding process for a tenant.
- `id`: UUID (Primary Key)
- `tenant_id`: UUID (System tenant ID for isolation)
- `customer_tenant_id`: UUID (Foreign Key to CustomerTenant)
- `title`: String
- `description`: String
- `status`: String (PENDING, COMPLETED, SKIPPED)
- `completed_at`: Timestamp
- `created_at`: Timestamp
- `updated_at`: Timestamp

### `TenantEvent`
Represents an event that happened to a tenant, useful for analytics.
- `id`: UUID (Primary Key)
- `tenant_id`: UUID (System tenant ID for isolation)
- `customer_tenant_id`: UUID (Foreign Key to CustomerTenant)
- `event_type`: String (e.g., "USAGE_SPIKE", "PAYMENT_FAILED", "USER_ADDED")
- `description`: String
- `occurred_at`: Timestamp

## 3. Backend (Spring Boot)
Port: 8080

### REST Endpoints
- `GET /api/tenants`
  - Returns a list of `CustomerTenant` objects for the current system tenant.
- `POST /api/tenants`
  - Body: `{ name: String }`
  - Provisions a new tenant, sets status to ONBOARDING, generates `external_tenant_id`.
- `GET /api/tenants/{id}`
  - Returns a single `CustomerTenant` by ID.
- `PUT /api/tenants/{id}/status`
  - Body: `{ status: String }`
  - Updates tenant status.
- `GET /api/tenants/{id}/milestones`
  - Returns a list of onboarding milestones for the tenant.
- `POST /api/tenants/{id}/milestones`
  - Body: `{ title: String, description: String }`
  - Adds a new milestone.
- `PUT /api/milestones/{milestoneId}/complete`
  - Marks a milestone as COMPLETED.
- `GET /api/tenants/{id}/events`
  - Returns events for a tenant.
- `POST /api/tenants/{id}/analyze-health`
  - Triggers the AI pipeline to recalculate `health_score` and `churn_risk` based on recent events and updates the tenant.

### Services
- `TenantService`: Handles CRUD for tenants and coordinates provisioning.
- `MilestoneService`: Handles CRUD for milestones.
- `EventService`: Manages tenant events.
- `AiHealthService`: Uses `LiteLLM` to analyze tenant events and calculate health score and churn risk.

### AI Integration
- Pattern: Pipeline (health monitoring)
- Prompt: Analyze the following events for tenant {name}. Calculate a health score from 0-100 and a churn risk (LOW, MEDIUM, HIGH). Provide the response as JSON: `{"healthScore": 85, "churnRisk": "LOW"}`.
- Model: `claude-3-haiku-20240307` or `gpt-3.5-turbo` via LiteLLM.

## 4. Frontend (Next.js)

### Pages
- `/`: Dashboard showing aggregated metrics (total active tenants, average health score, high churn risk tenants).
- `/tenants`: List view of all tenants with status and health score.
- `/tenants/[id]`: Detailed view of a tenant, their onboarding milestones, recent events, and an "Analyze Health" button.

### Components
- `TenantList`: Table to display tenants.
- `MilestoneTracker`: Component to display and update milestone status.
- `HealthScoreIndicator`: Visual indicator of health score (red, yellow, green).

## 5. Integration
- The app will emit events like `tenant.provisioned`, `tenant.deprovisioned`, `tenant.churn_risk_high`.
- Will consume events from other apps (e.g., `invoice.paid`, `usage.spike`) to log `TenantEvent`s.

## 6. Acceptance Criteria
- Can create a new tenant and see it in the list.
- Can add and complete onboarding milestones.
- Can trigger AI health analysis, and it updates the tenant's health score.
- The UI properly displays the tenant details and health metrics.
- All entities isolate data by `tenant_id` from the context.
