# PLAN

1. **Phase 1: Setup & Documentation**
   - Create the necessary tracking files in `.jules/` (e.g. `IMPLEMENTATION_LOG.md`, `SPEC.md`). I have already created `SPEC.md`.

2. **Phase 2: Database Migrations**
   - We need 8+ entities. We currently have 5 (`EmployeeCompensation`, `CompensationHistory`, `EquityModel`, `MarketData`, `CompensationCycle`).
   - I will add 3 new entities to the `V1__init.sql` migration file: `DepartmentBudget`, `PeerCompany`, `BenefitPlan`.
   - Ensure tenant isolation and standard columns (`id`, `tenant_id`, `created_at`, `updated_at`).

3. **Phase 3: Backend Entities, Repositories, Services, and Controllers**
   - **Entities**: Add `DepartmentBudget.java`, `PeerCompany.java`, `BenefitPlan.java` in `entity/`.
   - **Repositories**: Add repositories for the new entities extending `JpaRepository`.
   - **Services**: Add `DepartmentBudgetService.java`, `PeerCompanyService.java`, `BenefitPlanService.java`. This brings the total to 7+ services (we already had 7, now we'll have 9, which is fine, or we can just add 1 service to meet 8, but we will add them for the new entities). Wait, we already have 7 services, we'll have 9.
   - **Controllers**: Add controllers for the new entities: `DepartmentBudgetController.java`, `PeerCompanyController.java`, `BenefitPlanController.java`.

4. **Phase 4: Fix AI Integration**
   - Delete `com.microsaas.compensationos.service.AiService`.
   - Update `PayEquityService.java` to use `com.crosscutting.starter.ai.AiService` with `chat(new ChatRequest(...))`.
   - Ensure `cc-starter` AiService is injected properly.

5. **Phase 5: Docker Configuration**
   - Remove `compensationos/Dockerfile` and create `compensationos/backend/Dockerfile`. The new Dockerfile should copy the parent `cross-cutting` and build properly if needed, but since we are relying on a multi-stage or just a standard build, I will refer to standard Dockerfile for cc-starter dependent apps.
   - Wait, standard memory for Dockerfile: `docker-compose.yml` context is `..`, copy `cross-cutting` and `app`, use `../../cross-cutting/mvnw clean package -DskipTests` or `mvn` to build.
   - Create `compensationos/docker-compose.yml` configuring PostgreSQL and the backend service, connecting to port 8147.

6. **Phase 6: Frontend Adjustments**
   - Add simple Next.js pages for the 3 new entities (e.g., `/app/departments/page.tsx`, `/app/peers/page.tsx`, `/app/benefits/page.tsx`) as well as ensuring existing pages are functionally sound.
   - Check `integration-manifest.json` against the implementation to be 100% accurate.

7. **Phase 7: Pre-Commit & Verification**
   - Complete pre-commit steps to ensure proper testing, verification, review, and reflection are done. (using `pre_commit_instructions`)

8. **Phase 8: Handoff & Submit**
   - Run backend `mvn clean test` and frontend `npm test` locally to verify changes.
   - Create `.jules/HANDOFF.md` summarizing the work.
   - Use `submit` to push code to `feature/compensationos-pro-impl`.
