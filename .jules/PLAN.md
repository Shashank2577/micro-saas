# Development Plan for SupportIntelligence

1.  **Project Skeleton Creation:**
    *   Create backend Maven project `supportintelligence/backend` using cc-starter parent.
    *   Create frontend Next.js project `supportintelligence/frontend` using app router.
    *   Set up docker-compose file for PostgreSQL, Keycloak, etc. (re-use from other apps).
2.  **Database & Entity Layer:**
    *   Create `V1__init.sql` Flyway migration.
    *   Create JPA entities (`SupportTicket`, `ResponseSuggestion`, `EscalationSignal`, `TicketPattern`, `ProductIssue`, `AgentMetric`).
3.  **Service & AI Layer:**
    *   Implement AI integration with LiteLLM for `ResponseSuggestionService`, `EscalationDetectionService`, `SentimentAnalysisService`.
    *   Implement basic mock endpoints or actual service logic for metric tracking.
4.  **API Layer:**
    *   Create REST controllers for all entities.
    *   Setup Swagger UI.
5.  **Frontend Implementation:**
    *   Setup Tailwind, Shadcn UI components.
    *   Create Dashboard (`/dashboard`), Tickets (`/tickets`), Escalations (`/escalations`), and Insights (`/insights`) pages.
    *   Create Ticket Detail View with WebSocket / Real-time response suggestion component.
6.  **Integration Manifest:**
    *   Create `integration-manifest.json` describing events (`ticket.created`, etc.).
7.  **Testing & Refinement:**
    *   Run backend tests `mvn test`.
    *   Run frontend tests.
    *   Pre-commit verification.
8.  **Handoff:**
    *   Create `.jules/HANDOFF.md`.
