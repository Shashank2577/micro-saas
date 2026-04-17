# NotificationHub Detailed Specification

## 1. Overview
NotificationHub is a centralized notification delivery system that manages multi-channel messaging (email, SMS, push, in-app). It handles template management with variable substitution, notification scheduling, user preference management, batch delivery, delivery status tracking, and analytics.

## 2. Architecture
Following the cc-starter 6-layer architecture:
- **Entities**: JPA Entities mapping to PostgreSQL tables.
- **Repositories**: Spring Data JPA repositories with tenant isolation.
- **Services**: Business logic handling formatting, scheduling, and orchestrating deliveries.
- **Controllers**: REST APIs.
- **Frontend**: Next.js App Router for UI.
- **Integration**: Event emissions and capabilities in `integration-manifest.json`.

## 3. Database Schema
Using PostgreSQL. All tables include `tenant_id` for cross-cutting isolation, along with standard audit fields (`created_at`, `updated_at`).

```sql
CREATE TABLE notification_templates (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    channel VARCHAR(50) NOT NULL, -- EMAIL, SMS, PUSH, IN_APP
    subject_template TEXT,
    content_template TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_preferences (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    channel VARCHAR(50) NOT NULL,
    opted_in BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, user_id, channel)
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    template_id UUID REFERENCES notification_templates(id),
    channel VARCHAR(50) NOT NULL,
    subject TEXT,
    content TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, SCHEDULED, DELIVERING, DELIVERED, FAILED
    scheduled_for TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_deliveries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    notification_id UUID REFERENCES notifications(id) ON DELETE CASCADE,
    channel VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL, -- DELIVERED, FAILED
    provider_response TEXT,
    error_message TEXT,
    attempt_count INT DEFAULT 1,
    opened BOOLEAN DEFAULT FALSE,
    clicked BOOLEAN DEFAULT FALSE,
    delivered_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

*(Note: scheduled_notifications and notification_history are logically handled by `notifications` statuses and `notification_deliveries`)*

## 4. REST API Endpoints

### 4.1 Templates
- `POST /templates`
  - Body: `{ name, description, channel, subjectTemplate, contentTemplate }`
- `GET /templates`
  - Response: List of templates
- `PUT /templates/{templateId}`
  - Body: Updates
- `DELETE /templates/{templateId}`
- `POST /templates/{templateId}/test`
  - Body: `{ variables: {}, testUserId }`

### 4.2 Notifications
- `POST /notifications/send`
  - Body: `{ userId, templateId, channel, variables, scheduledFor }`
- `POST /notifications/batch`
  - Body: `{ userIds: [], templateId, channel, variables, scheduledFor }`
- `GET /notifications/{notificationId}`
- `GET /notifications/history`

### 4.3 Preferences
- `GET /preferences/{userId}`
- `PUT /preferences/{userId}`
- `POST /preferences/{userId}/opt-out`
  - Body: `{ channel }`

### 4.4 Analytics
- `GET /analytics/delivery-stats`
- `GET /analytics/open-rates`
- `GET /analytics/click-rates`

## 5. Services & Business Logic

- **TemplateService**: CRUD operations on templates.
- **PreferenceService**: Manage opt-ins/opt-outs. Enforce compliance.
- **NotificationService**: Trigger notification creation, substitute variables using template, handle batch logic.
- **DeliveryService**: Actually send the notification via Mock Providers (SendGrid, Twilio, FCM stubs in development). Track attempts and update status. Uses LiteLLM for optimizing content if requested (A/B testing).
- **SchedulingService**: Polling or cron task to pick up `SCHEDULED` notifications when `scheduled_for <= now()` and push them to `DeliveryService`.
- **AnalyticsService**: Aggregate counts of statuses, opened, clicked per template/channel.

## 6. Frontend
- Next.js with React Query.
- Pages:
  - `/`: Dashboard showing Analytics (Open rates, click rates).
  - `/templates`: Template management UI.
  - `/templates/new`: Template builder.
  - `/notifications`: Notification history.
  - `/preferences`: UI for a user to manage their preferences.

## 7. Next Steps / Acceptance Criteria Testing
- Tests for scheduling, delivery mock, template variable substitution, preference opt-out logic.
- Mock external APIs (Sendgrid, Twilio) for the purpose of complete delivery tests.
