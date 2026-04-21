# featureflagai - PLAN

## Phase 1: Planning and Setup
- Create `SPEC.md` and `PLAN.md`.
- Initial commit for Phase 1.

## Phase 2: Implement Backend Entities and Repositories
- Entities: `RolloutMetrics.java`, `FlagSegment.java`, `FlagAuditLog.java`
- Repositories: `RolloutMetricsRepository.java`, `FlagSegmentRepository.java`, `FlagAuditLogRepository.java`
- Flyway: `V1__init.sql` for all missing tables.

## Phase 3: Implement Backend Services and Controllers
- Services: `SegmentTargetingService.java`, `ImpactAnalysisService.java`, `FlagCleanupService.java`
- Update `RolloutService.java` and `FeatureFlagController.java`.

## Phase 4: Implement Frontend Pages
- Pages: `/`, `/flags/[id]`, `/impact`, `/cleanup`.
- Components: `CreateFlagModal.tsx`, `RolloutControl.tsx`, `SegmentList.tsx`.

## Phase 5: Verification and Submission
- `mvn -pl featureflagai/backend clean verify`
- `npm --prefix featureflagai/frontend run test`
- Create `HANDOFF.md` and submit.
