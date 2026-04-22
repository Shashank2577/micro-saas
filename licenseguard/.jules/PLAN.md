1. **Phase 1: Planning and Setup**
   - Create `.jules/` artifacts.
   - Commit as Gate.
2. **Phase 2: Full Implementation**
   - Backend: Create/update 8 entities, repositories, services, controllers.
   - Database: Complete `V1__init.sql` Flyway migration.
   - AI Pattern: Implement RAG for license compliance check in `LicenseComplianceService`.
   - Docker: Create `Dockerfile` and `docker-compose.yml`.
   - Frontend: Scaffold Next.js app in `licenseguard/frontend/`, create pages for main entities.
   - Integration: Update `integration-manifest.json`.
3. **Phase 3: Verification**
   - Test backend with `mvn clean package -DskipTests` (or run tests).
   - Test frontend with `npm run build`.
4. **Phase 4: Pre-Commit and Submit**
   - Complete pre-commit steps to ensure proper testing, verification, review, and reflection are done.
