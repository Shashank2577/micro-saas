# AI-Native Spec Factory: Developer Acceleration Platform

**Created:** 2026-04-16
**Author:** Claude Code + Shashank
**Status:** Design Approved

## Overview

Automated pipeline that accelerates building the remaining 86 apps in the micro-saas ecosystem by transforming rough app concepts into fully-built, tested, and registered applications.

**Problem:** Building 86 apps manually is too slow:
- Spec creation: 86 apps × 20 min/spec = 29 hours
- Jules orchestration: Manual dispatch, monitoring, PR merging
- Testing: Verifying each app works
- Registration: Manually connecting to Nexus Hub

**Solution:** CLI-first pipeline with 5 stages:
```
Idea → Spec Generator → Spec Validator → Jules Orchestrator → Test Runner → Registered App
```

**Value proposition:** Build an entire cluster (5-10 apps) in ~6 hours instead of days.

---

## Architecture

### System Components

#### 1. Spec Generator CLI
**Purpose:** Transform rough app concepts into Jules-ready detailed specs

**Command:**
```bash
npm run spec-gen -- <appName> <cluster> <one-liner>
```

**Implementation:**
- Reads template patterns from `tools/spec-templates/`:
  - Cluster-specific templates (A-K)
  - Common sections schema
  - cc-starter module catalog
- Uses Claude Sonnet 4.6 via LiteLLM for spec generation
- Validates structure and saves to `docs/superpowers/specs/<cluster>/<appName>-spec.md`
- Output: Markdown spec + JSON sidecar with metadata

**Quality controls:**
- AI confidence score (0-1)
- Template enforcement
- Duplicate detection

#### 2. Spec Validator CLI
**Purpose:** AI-powered quality check before dispatching to Jules

**Command:**
```bash
npm run spec-validate -- <specPath>
```

**Implementation:**
- Parses spec (markdown + JSON)
- Runs parallel validation checks:
  - **Structural:** Required sections, JSON schema, naming conventions
  - **Semantic:** AI-powered checks for problem clarity, solution alignment, MVP scope realism
  - **Cross-app:** No duplicates, no event conflicts, unique ports
- Outputs validation report with pass/fail/warnings/suggestions
- Auto-fix mode with `--fix` flag

**Quality gates:**
- `confidence < 0.7` → Requires human review
- `structural = fail` → Block
- `warnings present` → Ask for confirmation

#### 3. Enhanced Jules Orchestrator
**Purpose:** Handle 5-session limit with intelligent queuing and PR management

**Command:**
```bash
npm run jules-batch -- <cluster> <concurrency>
```

**Implementation:**
- Discovers un-built apps in cluster
- Creates build queue based on dependencies
- Dispatches in waves (max 5 concurrent):
  ```javascript
  while (queue.hasApps()) {
    const wave = queue.take(5);
    const sessions = await dispatchToJules(wave);
    await waitForCompletion(sessions);
    validateAndMerge(sessionResults);
  }
  ```
- Smart session management:
  - Polls every 5 minutes
  - Timeout handling (30min in PLANNING)
  - Auto-retry with exponential backoff
  - State persistence to `.jules-orchestrator-state.json`
- PR validation & merging:
  - Pre-merge tests
  - Configurable merge strategy (auto/manual/trust)
  - Rollback on test failure
- Progress dashboard showing wave status

**Integration:**
- Reuses `.jules-plan-*.json` format
- Calls `tools/scaffold-app.sh`
- Calls `tools/register-app.sh`
- Uses `scripts/monitor-jules-*.sh` pattern

#### 4. Test Runner CLI
**Purpose:** Automated post-build validation

**Command:**
```bash
npm run app-test -- <appName>
```

**Implementation:**
- Spins up app in isolated environment
- Runs parallel test suites:
  - **Smoke tests:** Health endpoint, OpenAPI docs, manifest
  - **Integration tests:** Nexus Hub registration, event emission/consumption, capability calls
  - **cc-starter compliance:** Tenancy, auth, audit, webhooks
- Collects results:
  - Pass/fail per test suite
  - Coverage metrics
  - Performance baselines (startup time, memory, connections)
- Generates test report + logs
- Batch mode for entire cluster

**Integration with CI/CD:**
- Runs after Jules PR merge
- Blocks merge on failure (configurable)
- Posts results to PR comments

#### 5. Pipeline Orchestrator
**Purpose:** One-command pipeline from idea → tested app

**Command:**
```bash
npm run pipeline -- <appName> <cluster> <one-liner>
```

**Implementation:**
```javascript
1. Spec Generator    → Creates detailed spec
2. Spec Validator    → Checks quality, auto-fixes
3. Jules Orchestrator→ Dispatches, waits for PR
4. Test Runner       → Validates app
5. Nexus Register    → Registers app
```

**Features:**
- State persistence (`<pipeline-state>/<appName>.json`)
- Resume capability (`--resume-from=<stage>`)
- Batch mode for entire clusters (`--batch <cluster>`)
- Progress tracking with ETA

---

## Data Flow

### Input
```
App name: LogStream
Cluster: B
One-liner: "Centralized log aggregation with AI anomaly detection"
```

### Stage 1: Spec Generator Output
```markdown
---
cluster: B
appName: LogStream
generatedAt: 2026-04-16T10:00:00Z
aiModel: claude-sonnet-4.6
confidence: 0.87
---

# LogStream: AI-Native Log Aggregation

**Tagline:** Centralized log management with AI-powered anomaly detection

**Problem:** ...
**Solution:** ...
...
```

### Stage 2: Spec Validator Output
```json
{
  "valid": true,
  "confidence": 0.92,
  "checks": {
    "structural": "pass",
    "problem_clarity": "pass",
    "solution_alignment": "pass",
    "mvp_scope": "warning - may be too ambitious",
    "integration_compatibility": "pass",
    "port_availability": "pass"
  },
  "warnings": ["Consider reducing MVP scope"],
  "suggestions": ["Split into LogStream + LogAI"]
}
```

### Stage 3: Jules Orchestrator Output
```
✅ Jules session created: 982374829374892374
⏳ Waiting for Jules build... (typically 4-6 hours)
✅ PR created: https://github.com/Shashank2577/micro-saas/pull/42
✅ Tests passed
✅ Merged to main
```

### Stage 4: Test Runner Output
```json
{
  "appName": "logstream",
  "status": "pass",
  "duration": "2m 34s",
  "tests": {
    "smoke": "pass (5/5)",
    "integration": "pass (3/3)",
    "cc-starter": "pass (4/4)"
  },
  "coverage": {
    "api": "85%",
    "workflows": "60%"
  }
}
```

### Stage 5: Final Output
```
✅ LogStream is live at: http://localhost:8092
✅ Registered with Nexus Hub: http://localhost:8080/apps/logstream

🎉 Pipeline complete in 4h 23m
```

---

## Directory Structure

```
micro-saas/
├── tools/
│   ├── spec-generator/
│   │   ├── cli.js
│   │   ├── generator.js
│   │   ├── templates/
│   │   │   ├── cluster-a-template.md
│   │   │   ├── cluster-b-template.md
│   │   │   └── ...
│   │   └── common-sections.json
│   ├── spec-validator/
│   │   ├── cli.js
│   │   ├── validator.js
│   │   ├── checks/
│   │   │   ├── structural.js
│   │   │   ├── semantic.js
│   │   │   └── cross-app.js
│   │   └── schemas/
│   │       ├── integration-manifest.json
│   │       └── data-model.json
│   ├── jules-orchestrator/
│   │   ├── cli.js
│   │   ├── orchestrator.js
│   │   ├── queue.js
│   │   ├── session-manager.js
│   │   └── pr-handler.js
│   ├── app-tester/
│   │   ├── cli.js
│   │   ├── tester.js
│   │   ├── suites/
│   │   │   ├── smoke-tests.js
│   │   │   ├── integration-tests.js
│   │   │   └── cc-starter-tests.js
│   │   └── reporters/
│   │       ├── json-reporter.js
│   │       └── console-reporter.js
│   └── pipeline/
│       ├── cli.js
│       └── orchestrator.js
├── docs/superpowers/specs/
│   ├── cluster-a/
│   ├── cluster-b/
│   └── ...
├── test-reports/
│   ├── logstream-test-report.json
│   └── logstream-logs/
├── .pipeline-state/
│   └── logstream.json
└── package.json
```

---

## Technology Stack

- **CLI Framework:** oclif (Node.js)
- **AI/LLM:** Claude Sonnet 4.6 via LiteLLM (already in Docker stack)
- **Storage:** Filesystem (specs, test reports, state)
- **Testing:** Jest for unit tests, integration tests call live apps
- **HTTP:** axios for API calls to Nexus Hub, Jules, apps under test
- **Async:** Promises + async/await for parallel operations
- **State:** JSON files for persistence

**Dependencies:**
```json
{
  "dependencies": {
    "oclif": "^4.0.0",
    "axios": "^1.6.0",
    "openai": "^4.0.0",  // For LiteLLM API
    "chalk": "^5.3.0",   // CLI colors
    "ora": "^8.0.0",     // CLI spinners
    "inquirer": "^9.2.0" // Interactive prompts
  },
  "devDependencies": {
    "jest": "^29.7.0"
  }
}
```

---

## Integration with Existing Tools

### Reuses
- `tools/scaffold-app.sh` - App bootstrapping
- `tools/register-app.sh` - Nexus Hub registration
- `.jules-plan-*.json` format - Jules orchestration plans
- `scripts/monitor-jules-*.sh` pattern - Session monitoring
- Docker platform (`infra/docker-compose.yml`) - Testing environment

### Builds On
- Nexus Hub API (`http://localhost:8080/api/v1/...`)
- cc-starter modules (tenancy, auth, webhooks, etc.)
- LiteLLM (`http://localhost:4000`) - AI generation
- Jules CLI (`jules remote new`)

---

## Success Criteria

1. **Speed:** Generate complete spec in <30 seconds
2. **Quality:** Validated specs have >0.8 confidence, pass all structural checks
3. **Throughput:** Process 5 apps in parallel (Jules limit), full cluster in ~6 hours
4. **Reliability:** >90% of Jules-built apps pass all tests on first try
5. **Usability:** One command builds and tests an app end-to-end

---

## MVP Scope

**Phase 1 - Core Pipeline (Week 1):**
- Spec Generator with basic template-based generation
- Spec Validator with structural checks
- Simple Jules Orchestrator (dispatch + wait + report)
- Basic Test Runner (smoke tests only)
- Manual Pipeline orchestration (run each stage separately)

**Phase 2 - Enhanced Quality (Week 2):**
- AI-powered spec validation (semantic checks)
- Smart Jules session management (retry, timeout, state persistence)
- Integration tests in Test Runner
- Automated Pipeline orchestrator (one-command flow)

**Phase 3 - Scale & Polish (Week 3):**
- Batch processing for entire clusters
- Auto-merge strategies for PRs
- Performance baselines and regression detection
- Progress dashboards and ETA calculation
- Comprehensive test coverage

---

## Post-MVP Enhancements

- **Interactive mode:** Web UI for spec editing and validation
- **Idea generator:** AI brainstorms new app concepts based on market gaps
- **Marketplace:** Share specs between micro-saas ecosystems
- **Analytics:** Track which AI prompts produce best specs, optimize over time
- **Multi-account Jules:** Orchestrate across multiple Jules accounts (30+ concurrent sessions)
- **Self-improvement:** Use successful Jules builds as training data for better spec generation

---

## Risk Mitigation

**Risk:** AI-generated specs have inconsistent quality
**Mitigation:** Confidence scores + validation checks + human review gate for low-confidence specs

**Risk:** Jules sessions hang or fail silently
**Mitigation:** Timeouts, exponential backoff retry, state persistence, detailed logging

**Risk:** Tests pass but apps don't work in production
**Mitigation:** Real integration tests with Nexus Hub, cc-starter compliance checks, performance baselines

**Risk:** Pipeline too complex to debug when things break
**Mitigation:** Each stage is independent and runnable separately, detailed logs at each step, clear error messages

---

## Next Steps

1. Implement Spec Generator CLI
2. Implement Spec Validator CLI
3. Enhance existing Jules orchestration scripts
4. Implement Test Runner CLI
5. Build Pipeline Orchestrator
6. Test end-to-end with Cluster B apps
7. Iterate based on feedback
