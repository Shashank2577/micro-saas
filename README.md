# micro-saas — AI-Native 100-App Ecosystem

> **Vision:** 100 AI-native SaaS applications, each a standalone product, all integrated through a shared hub-and-spoke platform. Built on `cc-starter` (Spring Boot) + Next.js, deployed via `freestack`, orchestrated by Jules AI.

**Repository:** [Shashank2577/micro-saas](https://github.com/Shashank2577/micro-saas)
**Current progress:** 16 apps complete · 25 in-flight (Jules) · 59 planned

---

## Architecture

```
                        ┌─────────────────┐
                        │   Nexus Hub     │  ← App Registry + Event Bus + Workflow Engine
                        │  (port 8090)    │
                        └────────┬────────┘
                                 │ events / webhooks
          ┌──────────────────────┼──────────────────────┐
          │                      │                       │
    [Cluster A]             [Cluster B]             [Cluster C–J]
  Dev Intelligence      Legal & Compliance         Finance, HR, ...
```

**Foundation stack:**
- **Backend:** Spring Boot 3.2 + `cc-starter 0.0.1-SNAPSHOT` (16 auto-configured SaaS modules: multi-tenancy, RBAC, audit, storage, queue, webhooks, AI, search, export, notifications, feature-flags, billing, analytics, scheduler, OIDC, rate-limiting)
- **Frontend:** Next.js 14 (TypeScript + Tailwind)
- **Database:** PostgreSQL — each app gets its own schema
- **Auth:** Keycloak (via cc-starter OIDC)
- **Deployment:** freestack (Docker Compose → cloud)
- **Orchestration:** Jules AI (parallel app generation), Gemini CLI

---

## Ecosystem Progress

### ✅ Foundation — Complete

| Component | Description | Status |
|-----------|-------------|--------|
| [nexus-hub/backend](nexus-hub/backend/) | App Registry, Event Bus, Workflow Engine | ✅ Built & tested |
| [nexus-hub/frontend](nexus-hub/frontend/) | Dashboard: apps, events, workflows | ✅ Built |
| [infra/](infra/) | Docker Compose: PostgreSQL, Keycloak, Redis, LiteLLM, MinIO | ✅ Running |
| [tools/scaffold-app.sh](tools/scaffold-app.sh) | Bootstrap new app from template | ✅ Ready |
| [tools/register-app.sh](tools/register-app.sh) | Register app with Nexus Hub | ✅ Ready |
| [docs/superpowers/specs/](docs/superpowers/specs/) | Full specs for all 100 apps | ✅ Written |

---

### ✅ Cluster A — Developer Intelligence (5/5 complete)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| [incidentbrain/](incidentbrain/) | AI root cause analysis from Datadog + GitHub signals | 8091 | ✅ Applied |
| [dependencyradar/](dependencyradar/) | Open source dependency vulnerability monitoring | 8092 | ✅ Applied |
| [deploysignal/](deploysignal/) | DORA metrics + deployment risk scoring | 8093 | ✅ Applied |
| [apievolver/](apievolver/) | API contract management + backward compat checker | 8094 | ✅ Applied |
| [securitypulse/](securitypulse/) | Code security scanner + policy enforcement | 8095 | ✅ Applied |

---

### 🔄 Cluster B — Legal & Compliance Intelligence (10/10 dispatched)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| contractsense/ | AI contract review — risk analysis, redlining co-pilot | 8091* | 🟡 Jules building |
| complianceradar/ | Regulatory change monitoring + gap analysis | 8092* | 🟡 Jules building |
| policyforge/ | Policy authoring, versioning, acknowledgment tracking | 8093* | 🟡 Jules building |
| auditready/ | SOC2/ISO27001/GDPR evidence collection + readiness scoring | 8094* | 🟡 Jules building |
| licenseguard/ | Software license compliance + SBOM generation | 8096 | 🟡 Jules building |
| ndaflow/ | NDA lifecycle management + AI redline suggestions | 8097 | 🟡 Jules building |
| regulatoryfiling/ | Filing obligation tracking + 30/14/7-day deadline alerts | 8098 | 🟡 Jules building |
| contractportfolio/ | Portfolio-wide contract term extraction + renewal alerts | 8099 | 🟡 Jules building |
| dataprivacyai/ | GDPR/CCPA data flow mapping + DSR automation | 8100 | 🟡 Jules building |
| legalresearch/ | Legal research memos with citation tracking | 8101 | 🟡 Jules building |

---

### 🔄 Cluster C — Finance & Operations Intelligence (10/10 dispatched)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| cashflowai/ | 90-day cash flow forecasting + shortfall alerts | 8110 | 🟡 Jules building |
| financenarrator/ | AI financial narrative generation for board decks | 8111 | 🟡 Jules building |
| expenseintelligence/ | Expense policy enforcement + subscription redundancy detection | 8112 | 🟡 Jules building |
| vendoriq/ | Vendor SLA monitoring + renewal benchmarking | 8113 | 🟡 Jules building |
| invoiceprocessor/ | Invoice extraction + PO matching + approval routing | 8114 | 🟡 Jules building |
| runwaymodeler/ | Startup runway modeling + headcount scenario planning | 8115 | 🟡 Jules building |
| procurebot/ | AI procurement — tiered approval routing + PO generation | 8116 | 🟡 Jules building |
| budgetpilot/ | Budget variance tracking with natural language alerts | 8117 | 🟡 Jules building |
| taxdataorganizer/ | Tax transaction categorization + accountant package | 8118 | 🟡 Jules building |
| equityintelligence/ | Cap table + vesting calculation + funding round modeling | 8119 | 🟡 Jules building |

---

### 🔄 Cluster D — HR & Talent Intelligence (5/8 dispatched)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| hiresignal/ | AI recruiting intelligence + candidate fit scoring | 8120 | 🟡 Jules building |
| interviewos/ | Structured interview guides + evaluator consistency | 8121 | 🟡 Jules building |
| performancenarrative/ | AI performance review drafts + calibration | 8122 | ⏳ To dispatch |
| retentionsignal/ | Employee flight risk prediction + intervention recommendations | 8123 | 🟡 Jules building |
| compbenchmark/ | Real-time compensation benchmarking + pay equity | 8124 | ⏳ To dispatch |
| onboardflow/ | 30/60/90-day onboarding plan automation | 8125 | 🟡 Jules building |
| jobcraftai/ | Bias-checked, SEO-optimized job description generation | 8126 | ⏳ To dispatch |
| peopleanalytics/ | Workforce headcount modeling + org health metrics | 8127 | 🟡 Jules building |

---

### 📋 Clusters E–J — Planned (59 apps)

| Cluster | Theme | Apps | Status |
|---------|-------|------|--------|
| E | Creator & Media Intelligence | 11 | 📋 Spec written |
| F | Customer Success Intelligence | 10 | 📋 Spec written |
| G | Sales & Revenue Intelligence | 10 | 📋 Spec written |
| H | Product & Engineering Intelligence | 10 | 📋 Spec written |
| I | Real Estate & Asset Intelligence | 10 | 📋 Spec written |
| J | Healthcare & Life Sciences Intelligence | 8 | 📋 Spec written |

**Full spec:** [docs/superpowers/specs/2026-04-15-micro-saas-ecosystem-design.md](docs/superpowers/specs/2026-04-15-micro-saas-ecosystem-design.md)

---

## What We're Building Toward

The end state is a **100-app AI SaaS ecosystem** where:

1. **Every app is standalone** — can be sold and deployed independently with its own pricing
2. **Every app integrates** — through Nexus Hub's event bus and workflow engine
3. **Multi-tenant by default** — cc-starter handles tenant isolation, RBAC, audit trail in every app
4. **AI-native** — each app has at least one AI pattern: co-pilot, RAG, extraction pipeline, or agent
5. **Zero-marginal-cost deployment** — freestack infrastructure scales each app independently

**Revenue model:** Each app prices at $49–$2,500/month depending on user type (SMB vs enterprise). At 100 apps × average $200 MRR × 50 customers = $1M MRR ecosystem.

---

## Getting Started

### Prerequisites
- Java 21, Maven 3.9
- Node.js 20, npm
- Docker Desktop
- `cc-starter` installed: `cd /path/to/cross-cutting && mvn install -DskipTests`

### Start the Platform
```bash
cd infra && docker compose up -d
# Starts: PostgreSQL, Keycloak, Redis, MinIO, LiteLLM
```

### Run Nexus Hub
```bash
cd nexus-hub/backend && mvn spring-boot:run
# API: http://localhost:8090
# Register an app:
curl -X POST http://localhost:8090/api/v1/apps/register \
  -H 'Content-Type: application/json' \
  -H 'X-Tenant-ID: 00000000-0000-0000-0000-000000000001' \
  -d '{"name":"myapp","displayName":"My App","baseUrl":"http://localhost:8099","manifest":{}}'
```

### Scaffold a New App
```bash
./tools/scaffold-app.sh myapp "My App" 8200
cd myapp/backend && mvn spring-boot:run
./tools/register-app.sh myapp http://localhost:8200
```

---

## Jules Orchestration

Apps are built in parallel using [Google Jules](https://jules.google.com) AI agent.

| File | Purpose |
|------|---------|
| [.jules-cluster-a-sessions.json](.jules-cluster-a-sessions.json) | Cluster A session IDs (all complete) |
| [.jules-cluster-b-sessions.json](.jules-cluster-b-sessions.json) | Cluster B session IDs (10 in-flight) |
| [scripts/monitor-jules-cluster-a.sh](scripts/monitor-jules-cluster-a.sh) | Monitor Cluster A Jules sessions |
| [.conductor-plan.json](.conductor-plan.json) | Nexus Hub build plan (all phases complete) |

### Apply a completed Jules session
```bash
jules remote pull --session <sessionId>          # preview diff
jules remote pull --session <sessionId> --apply  # apply patch
git add <appdir>/ && git commit -m "feat(<app>): ..."
git push origin main
```

---

## Repository Structure

```
micro-saas/
├── nexus-hub/              # Hub — App Registry, Event Bus, Workflow Engine
│   ├── backend/            # Spring Boot 3.2 (port 8090)
│   └── frontend/           # Next.js 14 dashboard
├── infra/                  # Docker Compose platform services
├── tools/                  # scaffold-app.sh, register-app.sh
├── docs/
│   └── superpowers/
│       └── specs/          # Full 100-app design specs
│           ├── 2026-04-15-micro-saas-ecosystem-design.md
│           ├── 2026-04-16-spec-factory-design.md
│           └── cluster-a-detailed-specs.md
├── scripts/                # Jules orchestration scripts
│── incidentbrain/          # Cluster A apps (applied)
├── dependencyradar/
├── deploysignal/
├── apievolver/
├── securitypulse/
│                           # Cluster B–D: applied once Jules completes
└── [contractsense, complianceradar, policyforge, ...]
```

---

## Development Principles

- **TDD where possible** — Jules writes tests before implementation
- **Schema-per-app** — each app owns its PostgreSQL schema, no cross-schema foreign keys
- **Tenant-scoped queries** — every repository method filters by `tenantId`
- **Event-driven integration** — apps communicate through Nexus Hub events, not direct API calls
- **Spec-first** — every app is fully spec'd before a line of code is written

---

*Generated and maintained by Claude Code + Jules AI. Last updated: 2026-04-16.*
