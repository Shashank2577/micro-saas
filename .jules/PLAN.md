# Implementation Plan - PeopleAnalytics

## Phase 1: Foundation & Backend Core
1.  **Update POM and Configuration**
    - Update `peopleanalytics/backend/pom.xml` to use `cc-starter` and Spring Boot 3.3.5.
    - Configure `application.yml` for multi-tenancy, LiteLLM, and pgmq.
2.  **Database Migration**
    - Create Flyway migration `V1__initial_schema.sql` with all tables (Employee, PerformanceMetric, etc.).
3.  **Domain Entities & Repositories**
    - Implement JPA entities with `@TenantId` (from cc-starter) and encryption for PII.
    - Create Spring Data JPA repositories.
    - *Verification*: Verify file creation via `ls`.

## Phase 2: Business Logic & Services
4.  **Service Layer Implementation - Part 1**
    - Implement `EmployeeService` and `HRISIntegrationService` (using Spring Batch for bulk sync).
    - Implement `PerformanceAggregationService`.
    - *Verification*: `read_file` to verify implementation.
5.  **Service Layer Implementation - Part 2**
    - Implement `PulseSurveyService` with `pgmq` for async processing.
    - Implement `EngagementScoringService`.
    - *Verification*: `read_file` to verify implementation.
6.  **AI & Analytics Services**
    - Implement `RetentionPredictionService` using LiteLLM.
    - Implement `InsightsGenerationService`.
    - *Verification*: `read_file` to verify implementation.
7.  **Reporting Service**
    - Implement `ReportService` for PDF (iText) and CSV (commons-csv) exports.
    - *Verification*: `read_file` to verify implementation.

## Phase 3: API & Security
8.  **REST Controllers**
    - Implement `EmployeeController`, `PerformanceController`, `EngagementController`, `RetentionController`, and `ReportController`.
    - Apply RBAC annotations (HR, Manager, etc.).
    - *Verification*: `ls` to verify controller files.

## Phase 4: Frontend Development
9.  **Frontend Setup & Components**
    - Update `package.json` with dependencies (Chart.js, React Query).
    - Create shared UI components (layout, navigation).
    - *Verification*: `ls` frontend directory.
10. **Dashboard & Pages**
    - Implement Performance Dashboard (Chart.js).
    - Implement Employee list and Pulse Survey management pages.
    - Implement Retention Risk visualization.
    - *Verification*: `ls` frontend app directory.

## Phase 5: Verification & Handoff
11. **Testing & QA**
    - Run `mvn test` for backend.
    - Run `npm test` for frontend.
    - Manually verify API via generated OpenAPI spec.
12. **Pre-commit & Submission**
    - Complete pre-commit steps to ensure proper testing, verification, review, and reflection are done.
    - Submit PR.
