# Implementation Plan for BudgetPilot

## Backend Implementation Steps

1.  **Project Setup & Entities:**
    *   Verify `pom.xml` dependencies (Lombok, Testcontainers, JPA, Web).
    *   Create base packages: `com.microsaas.budgetpilot.model`, `com.microsaas.budgetpilot.repository`, `com.microsaas.budgetpilot.service`, `com.microsaas.budgetpilot.controller`.
    *   Implement JPA Entities in `model` package:
        *   `Budget` (id, tenantId, name, fiscalYear, totalAmount, status, timestamps)
        *   `BudgetItem` (id, tenantId, budget, category, department, allocatedAmount, actualAmount, timestamps)
        *   `Expense` (id, tenantId, budgetItem, amount, description, date, timestamps)
        *   `Variance` (id, tenantId, budgetItem, varianceAmount, variancePercentage, explanation, dateCalculated)
    *   Ensure all entities include `@Entity`, `@Table`, and `@EntityListeners(AuditingEntityListener.class)` if needed, and tenant scoping.

2.  **Repositories:**
    *   Create Spring Data JPA repositories in `repository` package:
        *   `BudgetRepository`
        *   `BudgetItemRepository`
        *   `ExpenseRepository`
        *   `VarianceRepository`
    *   Add custom methods to filter by `tenantId` (e.g., `findAllByTenantId()`).

3.  **Flyway Migrations:**
    *   Create `V1__init.sql` in `backend/src/main/resources/db/migration/` containing the schema definitions specified in `DETAILED_SPEC.md`.

4.  **Services:**
    *   Implement interfaces and concrete classes in `service` package:
        *   `BudgetService` (CRUD for Budget)
        *   `BudgetItemService` (CRUD for BudgetItem)
        *   `VarianceMonitorService` (Calculate variance, generate explanation via `AiService`)
        *   `BudgetProposalService` (Generate proposals via `AiService`)
    *   Integrate `TenantContext` to enforce data isolation.
    *   Integrate `QueueService` for emitting variance alerts.

5.  **Controllers (REST API):**
    *   Create REST controllers in `controller` package:
        *   `BudgetController` (endpoints for `/api/v1/budgets`)
        *   `BudgetItemController` (endpoints for `/api/v1/items`)
        *   `AIController` (endpoints for `/api/v1/ai/*`)
    *   Add OpenAPI documentation annotations.

6.  **Testing & Configuration:**
    *   Create tests for Services using Mockito and Spring Boot Test.
    *   Create tests for Controllers.
    *   Ensure `application.yml` and test properties are correctly configured for H2/PostgreSQL and LiteLLM.

## Frontend Implementation Steps

1.  **Setup & Types:**
    *   Ensure Tailwind CSS and required libraries (e.g., Lucide React, Axios or SWR/React Query) are installed.
    *   Create TypeScript types for API responses (e.g., `Budget`, `BudgetItem`, `Expense`, `Variance`).

2.  **API Client:**
    *   Create helper functions to fetch data from the backend.

3.  **Components:**
    *   Create `BudgetTable.tsx`.
    *   Create `VarianceAlert.tsx`.
    *   Create `ProposalGenerator.tsx`.
    *   Create `VarianceChart.tsx` (using Recharts or similar).

4.  **Pages:**
    *   `app/dashboard/page.tsx`: Overview.
    *   `app/budgets/page.tsx`: List budgets.
    *   `app/budgets/[id]/page.tsx`: Details of a budget.
    *   `app/planning/page.tsx`: Proposal generation UI.
