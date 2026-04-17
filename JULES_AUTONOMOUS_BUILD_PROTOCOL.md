# Jules Autonomous Build Protocol

## Overview

This protocol governs how Jules builds applications **completely autonomously** without any user feedback. Each session is self-contained with clear phase gates and documentation checkpoints.

---

## Key Principles

🚫 **NO USER FEEDBACK** — No questions back to chat. Jules works independently.
📋 **PHASE-GATED** — Clear phases: spec → implement → PR
📝 **DOCUMENTED** — All decisions, questions, confusions recorded in `.jules/` folder
🎯 **AUTONOMOUS** — Jules makes decisions, documents rationale, moves forward
✅ **COMPLETE** — PR created and submitted at the end

---

## Folder Structure Per App

```
{app-name}/
├── backend/               # Spring Boot application
├── frontend/              # Next.js application
├── Dockerfile
├── README.md
├── integration-manifest.json
└── .jules/               # ← NEW: Jules working folder
    ├── DETAILED_SPEC.md  # Generated detailed spec (PHASE 1)
    ├── IMPLEMENTATION_LOG.md  # What was built, decisions made
    ├── HANDOFF.md        # Questions, concerns, future work
    └── SESSION_NOTES.md  # Raw notes, timestamps
```

---

## Build Phases

### Phase 1: Detailed Spec Generation (Autonomous)

**Input:** Base spec from `.omc/sessions/{app}.json`

**Tasks:**
1. Read the base spec completely
2. Expand with implementation details:
   - Exact database schema (CREATE TABLE statements)
   - Every REST endpoint with request/response format
   - Every service method signature
   - Every React component with props/state
   - AI integration points (LiteLLM calls)
   - Error handling per endpoint
   - Edge cases and validations
3. Add acceptance test cases (testable conditions)
4. Write to `.jules/DETAILED_SPEC.md`
5. **Document decisions:** If expanding the spec, explain why

**Output:** `.jules/DETAILED_SPEC.md` (2000-3000 words, executable)

**Handoff Protocol:**
- If anything is unclear or contradictory in base spec → NOTE IN DETAILED_SPEC, but DO NOT WAIT
- Make reasonable assumptions, document them
- Continue to Phase 2

---

### Phase 2: Full Implementation (Autonomous)

**Input:** Detailed spec from Phase 1

**Tasks:**
1. **Backend (Spring Boot):**
   - Create all JPA entities with tenant_id, timestamps, indexes
   - Create all repositories (tenant-scoped queries)
   - Create all services (business logic)
   - Create all REST controllers (EVERY endpoint from spec)
   - Create LiteLLM AI client with retry/circuit breaker
   - Create event emitters/consumers (integration-manifest)
   - Create Flyway migrations (V1__init.sql)
   - Create OpenAPI documentation (springdoc-openapi)
   - Create tests: service unit tests (≥80%), controller integration tests
   - Create application.yml and Dockerfile

2. **Frontend (Next.js):**
   - Create every page listed in spec
   - Generate TypeScript types from OpenAPI
   - Create every component with proper state management
   - Create tests: component tests with vitest + Testing Library
   - Create authentication flow integration
   - Build package.json with all dependencies

3. **Integration:**
   - Create integration-manifest.json exactly matching spec
   - Ensure all "emits" and "consumes" are implemented
   - Add docker-compose entry to infra/compose.*.yml

4. **Documentation:**
   - Update README.md (purpose, setup, running locally, testing, deployment)
   - Document API in OpenAPI/Swagger
   - Document env vars required

**Output:** Complete backend + frontend + tests

**Handoff Protocol:**
- Log decisions, blockers, assumptions in `.jules/IMPLEMENTATION_LOG.md`
- If you hit a blocker → document it in HANDOFF.md and work around it
- Example: "Stripe API key not available → mocked Stripe in tests, documented for future"
- Continue to Phase 3

---

### Phase 3: Testing & Validation (Autonomous)

**Tasks:**
1. Run backend tests locally: `mvn -pl {app}/backend clean verify`
2. Run frontend tests: `npm --prefix {app}/frontend test`
3. Verify all acceptance criteria from spec are tested
4. Verify OpenAPI /swagger-ui.html loads
5. Verify every REST endpoint is documented
6. Verify no TODOs or stub returns in production code

**If tests fail:**
- Fix the code (don't ask, don't wait)
- Document the fix in IMPLEMENTATION_LOG.md
- Retry

---

### Phase 4: PR Creation (Autonomous)

**Tasks:**
1. Commit all changes:
   ```
   git add {app}/
   git commit -m "feat({app}): complete implementation with tests
   
   - Entities, repositories, services, controllers fully implemented
   - OpenAPI documentation complete
   - All acceptance criteria tested
   - Service ≥80% coverage
   - Integration with cc-starter verified"
   ```

2. Create PR to main:
   ```
   Title: "feat({app}): full autonomous implementation"
   
   Body:
   - Closes: N/A
   - Changes: [list key components built]
   - Testing: All tests pass, [coverage %]
   - Spec Reference: See {app}/.jules/DETAILED_SPEC.md
   - Handoff Notes: See {app}/.jules/HANDOFF.md
   ```

3. Submit PR (do NOT wait for review, Jules has no feedback loop)

**Output:** PR created, no further action needed

---

## Documentation Requirements

### `.jules/DETAILED_SPEC.md`
- **Must have:** Every entity, field, validation
- **Must have:** Every API endpoint with method, path, request, response
- **Must have:** Every service method signature and behavior
- **Must have:** Every React component structure
- **Must have:** Error scenarios and how they're handled
- **Format:** Markdown with code blocks, tables, examples

### `.jules/IMPLEMENTATION_LOG.md`
- **Log entry format:** `[HH:MM:SS] [PHASE] [COMPONENT] What was done / decision made`
- **Examples:**
  ```
  [09:15:30] [PHASE 2] Backend Created Account entity with tenant_id, encryption
  [09:17:45] [PHASE 2] Service Implemented AccountService with 8 methods (follows spec)
  [09:45:00] [PHASE 2] Frontend Created DashboardPage component with chart integrations
  [10:30:15] [PHASE 3] Testing Fixed ConstraintViolation in tests, added @Transactional
  [11:00:00] [PHASE 4] PR Created PR #123, submitted to main
  ```

### `.jules/HANDOFF.md`
- **Questions that arose:** Even if answered, document for future
- **Assumptions made:** List all assumptions (no base spec detail)
- **Future work:** What couldn't be finished, why, next steps
- **Integration notes:** Which APIs need real credentials, mocking done, etc.
- **Example:**
  ```
  ## Questions Resolved During Build
  - Q: Should subscription sync be real-time or batch?
    A: Spec didn't specify. Chose daily batch (async job at 22:00 UTC). 
    If real-time needed, use pgmq event-driven approach.
  
  ## Assumptions
  - Stripe API: Mocked in tests, needs real key for e2e testing
  - SendGrid API: Used fake API in dev, real key needed for prod
  - Plaid integration: Stubbed out, needs Plaid sandbox setup
  
  ## Future Work
  - [ ] E2E tests with real Stripe
  - [ ] Mobile app (not in scope, mentioned by spec)
  - [ ] Admin dashboard (mentioned but low priority)
  ```

### `.jules/SESSION_NOTES.md`
- Raw notes, stream of consciousness
- Timestamps
- What was tried, what worked, what didn't
- Links to documentation consulted
- NOT polished, for future reference only

---

## Error Handling & Resilience

**If blocked on X:**
1. Try alternative approach
2. Document in HANDOFF.md
3. Continue with the rest
4. Example: "Couldn't integrate real Datadog API → mocked it, works for demo"

**If tests fail:**
1. Debug and fix
2. Don't loop infinitely (max 3 attempts, then document)
3. Document in HANDOFF.md, continue to PR

**If spec is ambiguous:**
1. Make reasonable choice
2. Document choice in DETAILED_SPEC.md
3. Continue

---

## Acceptance Checklist (Before PR)

- [ ] All JPA entities created with proper tenant scoping
- [ ] All REST endpoints implemented (from spec)
- [ ] All service methods implemented (from spec)
- [ ] All React components created (from spec)
- [ ] OpenAPI docs complete (/swagger-ui.html renders)
- [ ] Tests: backend ≥80% service layer coverage
- [ ] Tests: frontend component tests for each page
- [ ] All acceptance criteria from base spec have test cases
- [ ] No TODOs or stub returns in production code
- [ ] .jules/DETAILED_SPEC.md complete
- [ ] .jules/IMPLEMENTATION_LOG.md logged
- [ ] .jules/HANDOFF.md filled out
- [ ] Git history is clean (logical commits per component)
- [ ] PR created and submitted

---

## Session Summary

At the end of the session, Jules will have:

✅ Generated a detailed, implementable spec
✅ Built backend + frontend fully
✅ Tested everything (tests passing)
✅ Documented all decisions, assumptions, questions
✅ Created a PR ready for review
✅ Left clear handoff notes for humans

**Time estimate:** 14-22 hours per app (depending on complexity)
**No feedback required:** Jules works autonomously start to finish

---

## Notes for Future Readers

If you're reviewing a Jules PR:

1. **Read `.jules/DETAILED_SPEC.md` first** — understand what was supposed to be built
2. **Check `.jules/IMPLEMENTATION_LOG.md`** — see what decisions were made and why
3. **Review `.jules/HANDOFF.md`** — understand assumptions, future work, known gaps
4. **Then review the code** — it should match the detailed spec

This structure ensures full traceability and context for every line of code Jules wrote.
