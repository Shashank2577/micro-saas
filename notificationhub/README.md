# NotificationHub

A centralized notification delivery system that manages multi-channel messaging (email, SMS, push, in-app). Features include template management with variable substitution, notification scheduling, user preference management, batch delivery, delivery status tracking, and analytics.

## Tech Stack
*   **Backend:** Spring Boot 3, Java 21, PostgreSQL, Redis
*   **Frontend:** Next.js (App Router), TypeScript, Tailwind CSS, React Query
*   **Integrations:** SendGrid (Email), Twilio (SMS), LiteLLM (Content Optimization)

## Setup and Running Locally

1.  **Database & Redis:** Ensure PostgreSQL and Redis are running. You can use Docker Compose from the root directory.
2.  **Backend:**
    *   `cd backend`
    *   Set required environment variables (see below).
    *   `mvn spring-boot:run`
3.  **Frontend:**
    *   `cd frontend`
    *   `npm install`
    *   `npm run dev`

## Environment Variables
*   `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` for PostgreSQL.
*   `SPRING_REDIS_HOST`, `SPRING_REDIS_PORT` for Redis.
*   `SENDGRID_API_KEY`: For sending real emails.
*   `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN`, `TWILIO_PHONE_NUMBER`: For sending real SMS.
*   `LITELLM_URL`, `LITELLM_API_KEY`, `LITELLM_MODEL`: For content optimization (A/B testing).

## Testing
*   **Backend:** `cd backend && mvn clean verify`
*   **Frontend:** `cd frontend && npm test`
