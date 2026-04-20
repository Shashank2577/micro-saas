# Phase-Based Autonomous Build Plan

## Phase 1: Spec & Plan
- [x] Create `.jules/SPEC.md` and `.jules/PLAN.md` in the repo root.
- [ ] Commit these files immediately.

## Phase 2: Autonomous Implementation
- Verify existing codebase in `socialintelligence/`. The backend and frontend appear to have been partially generated or implemented.
- Complete the backend logic and tests:
  - Add missing unit/integration tests for backend controllers and services.
  - Fix any compilation or logic errors.
- Complete the frontend logic and tests:
  - Ensure Vitest setup works.
  - Add missing component tests.
- Maintain `.jules/IMPLEMENTATION_LOG.md` tracking what has been built.
- Use atomic commits for every 2-3 files created/modified.

## Phase 3: Verification & Hardening
- Run backend tests: `mvn -pl socialintelligence/backend clean verify`
- Run frontend tests: `npm --prefix socialintelligence/frontend test`
- Create `.jules/VERIFICATION_REPORT.md` documenting test results and coverage.

## Phase 4: Handoff & PR
- Create `.jules/HANDOFF.md` summarizing what was implemented.
- Submit the changes using the provided `submit` tool, opening a PR with the correct title and description.
