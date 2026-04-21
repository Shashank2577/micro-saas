# BrandVoice Implementation Plan

## Phase 1: Setup and Spec
1. Create `SPEC.md` and `PLAN.md` (Done).
2. Commit these initial files.

## Phase 2: Backend Implementation
1. **Entities & Repositories**: Verify or create JPA entities (`BrandProfile`, `ContentAudit`, `AuditResult`, `AuditFinding`) and their repositories.
2. **Services**: Implement `BrandProfileService`, `AuditProcessingService`, `BrandVoiceAnalysisService`, `BrandVoiceSuggestionService`, `PdfExportService`.
3. **Controllers**: Implement REST endpoints in `BrandProfileController`, `ContentAuditController`, `AuditResultController`, `IntegrationController`, `WebhookController`, `PdfExportController`.
4. **Database Migration**: Ensure `V1__init.sql` reflects the correct schema.
5. **Configuration**: Update `pom.xml` for dependencies (iText, etc.) and `application.yml`.

## Phase 3: Frontend Implementation
1. **Types & API Client**: Generate/write TypeScript types and API client functions.
2. **Pages**: Implement Dashboard (`/`), Brand Profiles (`/profiles`, `/profiles/[id]`), Audits (`/audits`, `/audits/new`, `/audits/[id]`).
3. **Components**: UI components for forms, tables, charts (Chart.js for compliance trends), and results view.

## Phase 4: Integration & Tests
1. **Backend Tests**: Write unit/integration tests for services and controllers. Verify coverage.
2. **Frontend Tests**: Write component tests using vitest.
3. **Integration Manifest**: Update `integration-manifest.json`.

## Phase 5: Hardening & PR
1. Run full backend verify (`mvn clean verify`).
2. Run frontend verify (`npm test`).
3. Generate `VERIFICATION_REPORT.md` and `HANDOFF.md`.
4. Create PR.
