# Cluster A: Developer Intelligence — Detailed Specs

## Overview
5 AI-native SaaS apps for engineering teams. Each app addresses a specific developer workflow pain point.

---

## 1. IncidentBrain

**Tagline:** AI-powered incident root cause analysis

**Problem:** Production incidents are chaotic. Engineers scramble across logs, metrics, traces, and past tickets. Root cause analysis takes hours. Repeat incidents happen because tribal knowledge isn't captured.

**Solution:** AI agent that ingests incident context (logs, metrics, traces, alerts, git history) and outputs:
- Probable root cause with confidence score
- Timeline reconstruction
- Similar past incidents with resolutions
- Recommended remediation steps
- Auto-generated postmortem draft

**Core Features:**
1. **Incident Ingestion**: Connects to Datadog/PagerDuty/NewRelic via webhooks
2. **Context Aggregation**: Fetches correlated logs (ELK), traces (Jaeger), metrics (Prometheus), recent deploys (GitHub), error tracking (Sentry)
3. **AI Analysis**: Uses Claude Sonnet 4.6 to analyze patterns, detect anomalies, correlate events
4. **Similarity Search**: Vector search (pgvector) over historical incidents
5. **Postmorter Generator**: Outputs markdown template populated with key findings

**Integration Manifest:**
```json
{
  "emits": ["incident.opened", "incident.root-cause-identified", "incident.resolved"],
  "consumes": ["deploy.completed", "error-rate-spike", "alert-fired"],
  "capabilities": [
    {"name": "analyze-incident", "input": "incidentId", "output": "rootCauseReport"},
    {"name": "find-similar", "input": "incidentContext", "output": "similarIncidents"},
    {"name": "generate-postmortem", "input": "incidentId", "output": "markdown"}
  ]
}
```

**Data Model:**
- `Incident`: id, title, severity, status, rootCauseHypothesis, confidenceScore, timelineEvents, similarIncidents[], postmortemDraft
- `TimelineEvent`: timestamp, source, type, summary, metadata
- `RootCauseCandidate`: description, confidence, evidence[], aiReasoning

**Tech Stack:**
- Backend: Spring Boot + cc-starter
- AI: Claude Sonnet 4.6 via LiteLLM for analysis
- Vector DB: pgvector for semantic search over incidents
- Ingestion: Webhook endpoints + Polling for Datadog/PagerDuty APIs
- Frontend: Next.js with incident timeline visualization

**MVP Scope:**
- Manual incident creation (webhook ingest in v2)
- Datadog integration for logs/metrics
- GitHub integration for recent commits
- AI root cause analysis (Claude)
- Manual similarity search (vector search in v2)

**Post-MVP:**
- PagerDuty/NewRelic webhooks
- Automatic similarity matching with pgvector
- Slack integration for alerts
- Jira integration for ticket creation

---

## 2. DependencyRadar

**Tagline:** Open source dependency monitoring with upgrade intelligence

**Problem:** Teams lose track of upstream vulnerabilities. Upgrading a single package can break the build. Security patches languish because impact analysis is manual.

**Solution:** Continuous monitoring of `pom.xml`, `package.json`, `go.mod` with:
- Real-time vulnerability scanning (CVE database)
- Dependency health dashboard (outdated, deprecated, licenses)
- Upgrade impact analysis (what breaks if I upgrade X?)
- Automated PR generation for safe upgrades

**Core Features:**
1. **Repo Scanning**: Connects to GitHub (webhook on push) to parse dependency files
2. **Vulnerability Feed**: Cross-references with NVD (National Vulnerability Database) and GitHub Security Advisories
3. **Dependency Graph**: Builds transitive dependency tree (A → B → C)
4. **Impact Analyzer**: For upgrade candidate, runs test suite against new version in sandbox
5. **Auto-PR**: Creates pull request with upgrade if tests pass

**Integration Manifest:**
```json
{
  "emits": ["vulnerability-detected", "dependency-outdated", "upgrade-available"],
  "consumes": ["code.pushed", "pr.merged"],
  "capabilities": [
    {"name": "scan-dependencies", "input": "repoUrl", "output": "dependencyReport"},
    {"name": "check-upgrade", "input": "packageName+version", "output": "impactAnalysis"},
    {"name": "create-upgrade-pr", "input": "packageName+newVersion", "output": "prUrl"}
  ]
}
```

**Data Model:**
- `Dependency`: repoId, name, currentVersion, latestVersion, vulnerabilities[], outdated, deprecated
- `Vulnerability`: cveId, severity, description, affectedVersions, patchedVersions
- `UpgradeCandidate`: dependencyId, newVersion, testResults[], prUrl

**Tech Stack:**
- Backend: Spring Boot + cc-starter
- Scheduler: Spring `@Scheduled` to poll NVD/GHSA daily
- GitHub Integration: Octokit for PR creation
- Test Runner: Clone repo to temp dir, run `mvn test`/`npm test`, capture exit code
- Frontend: Next.js with dependency tree visualization (D3.js or React Flow)

**MVP Scope:**
- GitHub webhook on push
- Maven + npm dependency parsing
- NVD vulnerability lookup
- Manual "test upgrade" button (runs tests in background job)
- Dependency health dashboard

**Post-MVP:**
- Automated upgrade PRs
- Gradle/Go modules support
- License compliance checking
- Slack alerts for critical CVEs

---

## 3. DeploySignal

**Tagline:** Deployment analytics and risk assessment

**Problem:** Deployments are black boxes. Was this deploy risky? Did it cause errors? Teams lack historical context to make go/no-go decisions.

**Solution:** Deployment analytics platform that:
- Ingests deploy events (GitHub Actions, ArgoCD, Vercel)
- Computes risk score based on files changed, tests passing, time since last deploy
- Tracks deployment frequency, lead time, change failure rate (DORA metrics)
- Flags risky deploys (e.g., deploying on Friday afternoon with 500 files changed)

**Core Features:**
1. **Deploy Ingestion**: Webhook from CI/CD (GitHub Actions, Vercel, CircleCI)
2. **Risk Scoring**: Algorithm weighting: commit count to production-critical files, test failures in last 24h, deployer tenure, time of day, day of week
3. **DORA Dashboard**: Deployment frequency, lead time for changes, time to restore service, change failure rate
4. **Anomaly Detection**: Spike in error rate after deploy? Auto-rollback recommendation

**Integration Manifest:**
```json
{
  "emits": ["deploy.started", "deploy.completed", "deploy.failed", "deploy.rolled-back"],
  "consumes": ["incident.opened", "error-rate-spike", "test.failed"],
  "capabilities": [
    {"name": "calculate-risk", "input": "deployContext", "output": "riskScore"},
    {"name": "track-dora", "input": "timeRange", "output": "doraMetrics"}
  ]
}
```

**Data Model:**
- `Deployment`: id, sha, branch, status, startedAt, completedAt, riskScore, filesChanged[], testResults, rollback
- `RiskFactor`: type (test-failure, hotfix, friday-deploy, many-files), weight, description

**Tech Stack:**
- Backend: Spring Boot + cc-starter
- Ingestion: GitHub Actions webhook, Vercel webhook
- Analytics: TimescaleDB for time-series deploy data (or Postgres with Timescale extension)
- Frontend: Next.js with deploy calendar and trend charts

**MVP Scope:**
- GitHub Actions webhook ingestion
- Basic risk scoring (files changed count, test pass/fail)
- Deploy history table
- Simple DORA metrics (deploy freq, change fail rate)

**Post-MVP:**
- ML-based risk prediction
- Integration with IncidentBrain (auto-link deploy to incident)
- Team comparison dashboards

---

## 4. APIEvolver

**Tagline:** API contract management with backward compatibility checking

**Problem:** API changes break clients. Teams forget to notify downstream consumers. Breaking changes slip through to production.

**Solution:** API version control and compatibility tracking:
- Central registry of all REST/GraphQL endpoints across microservices
- Diff detection between OpenAPI specs (what changed?)
- Breaking change detection (required field removed, type changed)
- Consumer notification (notify teams when APIs they depend on change)

**Core Features:**
1. **Spec Registry**: Upload OpenAPI/GraphQL specs for each service version
2. **Spec Diff**: Compare v1 vs v2, list changed endpoints
3. **Breaking Change Detector**: Rules engine (removed required field = breaking, optional field added = non-breaking)
4. **Dependency Graph**: Service A depends on Service B's API
5. **Change Notification**: When Service B uploads v2, notify all Service A consumers

**Integration Manifest:**
```json
{
  "emits": ["api-spec-uploaded", "breaking-change-detected", "api-deprecated"],
  "consumes": ["service.registered", "api-spec-scanned"],
  "capabilities": [
    {"name": "diff-specs", "input": "oldSpec+newSpec", "output": "diffReport"},
    {"name": "detect-breaking", "input": "specDiff", "output": "breakingChanges[]"},
    {"name": "notify-consumers", "input": "serviceId+change", "output": "notificationStatus"}
  ]
}
```

**Data Model:**
- `ApiSpec`: serviceId, version, specType (OPENAPI, GRAPHQL), specUrl, contentHash, uploadedAt
- `ApiChange`: specId, oldVersion, newVersion, changes[], breakingChanges[]
- `ApiDependency`: consumerServiceId, providerServiceId, usedEndpoints[], sensitiveToBreakingChanges

**Tech Stack:**
- Backend: Spring Boot + cc-starter
- Spec Parser: `swagger-parser` for OpenAPI, `graphql-parse` for GraphQL
- Diff Engine: Custom semantic diff for JSON specs
- Frontend: Next.js with spec visualization (diff side-by-side view)

**MVP Scope:**
- Manual OpenAPI spec upload (JSON file)
- Spec diff between two versions
- Manual breaking change rules (simple regex/list)
- Manual consumer registration (Service B uses Service A's API)

**Post-MVP:**
- GitHub integration (auto-scan `src/main/resources/openapi.yaml` on push)
- Automatic breaking change detection using OpenAPI diff tool
- Email/Slack notifications to consumers

---

## 5. SecurityPulse

**Tagline:** Code security scanner with policy enforcement

**Problem:** Security tools are siloed (SAST, SCA, secrets). Issues pile up. Teams don't know what to fix first. Policies (e.g., "no secrets in code") aren't enforced.

**Solution:** Unified security scanner that:
- Runs multiple security tools (SAST: Semgrep, SCA: Dependabot, secrets: TruffleHog)
- Aggregates findings into single dashboard
- Enforces policies (block PR if secret detected, high severity SAST fails)
- Triages with AI (group similar findings, suggest fixes)

**Core Features:**
1. **PR Scanning**: GitHub Actions app that scans on every PR
2. **Tool Orchestration**: Runs Semgrep (SAST), TruffleHog (secrets), custom (security patterns)
3. **Policy Engine**: Rule-based (e.g., "block PR if secret found OR critical SAST")
4. **AI Triage**: Claude Sonnet 4.6 groups findings, filters FPs, suggests code fixes
5. **Issue Tracker**: Track findings over time, trend security posture

**Integration Manifest:**
```json
{
  "emits": ["security-scan-completed", "vulnerability-found", "policy-violation"],
  "consumes": ["pr.opened", "pr.merged"],
  "capabilities": [
    {"name": "scan-pr", "input": "prUrl", "output": "scanResults"},
    {"name": "enforce-policy", "input": "scanResults", "output": "policyDecision"},
    {"name": "triage-findings", "input": "findings", "output": "triagedReport"}
  ]
}
```

**Data Model:**
- `SecurityScan`: prId, sha, branch, status, findings[], policyDecision
- `Finding`: tool (SEMGREP, TRUFFLEHOG), severity, type, filePath, line, description, aiSuggestion
- `Policy`: name, rules[], action (BLOCK, WARN, ALLOW)

**Tech Stack:**
- Backend: Spring Boot + cc-starter
- GitHub App: Probot for PR integration
- Scanner Integration: Semgrep CLI, TruffleHog CLI
- AI: Claude Sonnet 4.6 for triage and fix suggestions
- Frontend: Next.js with security dashboard

**MVP Scope:**
- Manual scan (paste branch name/SHA)
- Semgrep + TruffleHog integration
- Basic policy engine (block on secrets, warn on high severity)
- Findings table

**Post-MVP:**
- GitHub Actions app (automatic scan on PR)
- SCA integration (OWASP Dependency Check)
- AI-powered fix suggestions (patch generation)
- Trend analysis (security posture over time)

---

## Cluster A: Integration Workflow

**Data Flow Example:**
1. **DeploySignal** detects deployment → emits `deploy.completed`
2. **IncidentBrain** monitors error rate spike → ingests logs → emits `incident.opened`
3. **APIEvolver** scans new deploy → detects breaking API change → emits `breaking-change-detected`
4. **SecurityPulse** scans PR → emits `security-scan-completed`
5. **Nexus Hub** receives all events → triggers workflow: "On incident.opened + deploy.completed, analyze impact"

**Shared Infrastructure:**
- All use cc-starter for tenancy, auth, RBAC, audit
- All emit webhook events via Nexus Hub event bus
- All expose OpenAPI specs (auto-consumed by APIEvolver)
- All register with Nexus Hub on startup

**Parallel Build Strategy with Jules:**
- Max 5 concurrent sessions (Jules limit)
- Each app ~4-6 hours implementation time
- Cluster A total: ~20-30 hours of parallel execution
- **Approach:** 2 waves (Wave 1: IncidentBrain, DependencyRadar, DeploySignal | Wave 2: APIEvolver, SecurityPulse)
