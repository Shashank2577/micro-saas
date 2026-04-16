# micro-saas — AI-Native 100-App Ecosystem

> **Vision:** 100 AI-native SaaS applications, each a standalone product, all integrated through a shared hub-and-spoke platform. Built on `cc-starter` (Spring Boot) + Next.js, deployed via `freestack`, orchestrated by Jules AI.

**Repository:** [Shashank2577/micro-saas](https://github.com/Shashank2577/micro-saas)
**Current progress:** 36 apps complete · 15 in-flight (Jules) · 49 planned

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
- **Orchestration:** Jules AI (parallel app generation)

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

### ✅ Cluster B — Legal & Compliance Intelligence (10/10 complete)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| [contractsense/](contractsense/) | AI contract review — risk analysis, redlining co-pilot | 8096 | ✅ Applied |
| [complianceradar/](complianceradar/) | Regulatory change monitoring + gap analysis | 8092 | ✅ Applied |
| [policyforge/](policyforge/) | Policy authoring, versioning, acknowledgment tracking | 8093 | ✅ Applied |
| [auditready/](auditready/) | SOC2/ISO27001/GDPR evidence collection + readiness scoring | 8094 | ✅ Applied |
| [licenseguard/](licenseguard/) | Software license compliance + SBOM generation | 8096 | ✅ Applied |
| [ndaflow/](ndaflow/) | NDA lifecycle management + AI redline suggestions | 8097 | ✅ Applied |
| [regulatoryfiling/](regulatoryfiling/) | Filing obligation tracking + 30/14/7-day deadline alerts | 8098 | ✅ Applied |
| [contractportfolio/](contractportfolio/) | Portfolio-wide contract term extraction + renewal alerts | 8099 | ✅ Applied |
| [dataprivacyai/](dataprivacyai/) | GDPR/CCPA data flow mapping + DSR automation | 8100 | ✅ Applied |
| [legalresearch/](legalresearch/) | Legal research memos with citation tracking | 8101 | ✅ Applied |

---

### ✅ Cluster C — Finance & Operations Intelligence (10/10 complete)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| [cashflowai/](cashflowai/) | 90-day cash flow forecasting + shortfall alerts | 8110 | ✅ Applied |
| [financenarrator/](financenarrator/) | AI financial narrative generation for board decks | 8111 | ✅ Applied |
| [expenseintelligence/](expenseintelligence/) | Expense policy enforcement + subscription redundancy detection | 8112 | ✅ Applied |
| [vendoriq/](vendoriq/) | Vendor SLA monitoring + renewal benchmarking | 8113 | ✅ Applied |
| [invoiceprocessor/](invoiceprocessor/) | Invoice extraction + PO matching + approval routing | 8114 | ✅ Applied |
| [runwaymodeler/](runwaymodeler/) | Startup runway modeling + headcount scenario planning | 8115 | ✅ Applied |
| [procurebot/](procurebot/) | AI procurement — tiered approval routing + PO generation | 8116 | ✅ Applied |
| budgetpilot/ | Budget variance tracking with natural language alerts | 8117 | 🟡 Jules building |
| [taxdataorganizer/](taxdataorganizer/) | Tax transaction categorization + accountant package | 8118 | ✅ Applied |
| equityintelligence/ | Cap table + vesting calculation + funding round modeling | 8119 | 🟡 Jules building |

---

### 🔄 Cluster D — HR & Talent Intelligence (4/8 applied, 8/8 dispatched)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| [hiresignal/](hiresignal/) | AI recruiting intelligence + candidate fit scoring | 8120 | ✅ Applied |
| [interviewos/](interviewos/) | Structured interview guides + evaluator consistency | 8121 | ✅ Applied |
| [performancenarrative/](performancenarrative/) | AI performance review drafts + calibration | 8122 | ✅ Applied |
| [retentionsignal/](retentionsignal/) | Employee flight risk prediction + intervention recommendations | 8123 | ✅ Applied |
| compbenchmark/ | Real-time compensation benchmarking + pay equity | 8124 | 🟡 Jules building |
| onboardflow/ | 30/60/90-day onboarding plan automation | 8125 | 🟡 Jules building |
| jobcraftai/ | Bias-checked, SEO-optimized job description generation | 8126 | 🟡 Jules building |
| peopleanalytics/ | Workforce headcount modeling + org health metrics | 8127 | 🟡 Jules building |

---

### 🔄 Cluster E — Creator & Media Intelligence (0/11 applied, 9/11 dispatched)

| App | Description | Port | Status |
|-----|-------------|------|--------|
| contentos/ | AI content operations platform — unified calendar, repurposing, briefs | 8130 | 🟡 Jules building |
| brandvoice/ | AI brand voice consistency enforcement + style scoring | 8131 | 🟡 Jules building |
| videonarrator/ | AI video repurposing — transcript, chapters, clips, show notes | 8132 | 🟡 Jules building |
| seointelligence/ | AI SEO content gap analysis + ranking intelligence | 8133 | 🟡 Jules building |
| ghostwriter/ | AI long-form writing co-pilot trained on your voice | 8134 | 🟡 Jules building |
| socialintelligence/ | AI social monitoring + trend detection + content generation | 8135 | 🟡 Jules building |
| creatoranalytics/ | AI content ROI analytics — vanity metrics → business outcomes | 8136 | 🟡 Jules building |
| copyoptimizer/ | AI conversion copy + pre-publish performance prediction | 8137 | 🟡 Jules building |
| localizationos/ | AI content localization with cultural context + translation memory | 8138 | 🟡 Jules building |
| podcastos/ | AI podcast production — transcription, chapters, clips, analytics | 8139 | ⏳ To dispatch |
| visualcontext/ | AI visual asset intelligence — tagging, search, brand consistency | 8140 | ⏳ To dispatch |

---

### 📋 Clusters F–J — Planned (49 apps)

| Cluster | Theme | Apps | Status |
|---------|-------|------|--------|
| F | Data & Analytics Intelligence | 11 | 📋 Spec written |
| G | Sales & Revenue Intelligence | 10 | 📋 Spec written |
| H | Operations Intelligence | 6 | 📋 Spec written |
| I | New 2026 Additions | 9 | 📋 Spec written |
| J | Platform & Integration | 10 | 📋 Spec written |
| (overflow) | Customer Success, Healthcare, Real Estate | ~3 | 📋 Spec written |

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
| [.jules-cluster-b-sessions.json](.jules-cluster-b-sessions.json) | Cluster B session IDs (all complete) |
| [.jules-cluster-c-sessions.json](.jules-cluster-c-sessions.json) | Cluster C session IDs (8 applied, 2 building) |
| [.jules-cluster-d-sessions.json](.jules-cluster-d-sessions.json) | Cluster D session IDs (4 applied, 4 building) |
| [.jules-cluster-e-sessions.json](.jules-cluster-e-sessions.json) | Cluster E session IDs (9 building, 2 pending) |

### Apply a completed Jules session
```bash
jules remote pull --session <sessionId>                          # preview diff
jules remote pull --session <sessionId> > /tmp/app.patch        # save patch
git apply --whitespace=nowarn --include='appname/*' /tmp/app.patch  # apply (exclude junk files)
git add appname/ && git commit -m "feat(<app>): ..."
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
├── scripts/                # Jules orchestration scripts
│
│ # ✅ Cluster A — Developer Intelligence
├── incidentbrain/          # 8091 — AI incident root cause analysis
├── dependencyradar/        # 8092 — OSS vulnerability monitoring
├── deploysignal/           # 8093 — DORA metrics + deployment risk
├── apievolver/             # 8094 — API contract management
├── securitypulse/          # 8095 — Code security scanner
│
│ # ✅ Cluster B — Legal & Compliance
├── contractsense/          # 8096 — AI contract review
├── complianceradar/        # 8097 — Regulatory change monitoring
├── policyforge/            # 8098 — Policy authoring + versioning
├── auditready/             # 8099 — SOC2/ISO27001 evidence collection
├── licenseguard/           # 8100 — Software license compliance
├── ndaflow/                # 8101 — NDA lifecycle management
├── regulatoryfiling/       # 8102 — Filing obligation tracking
├── contractportfolio/      # 8103 — Portfolio contract management
├── dataprivacyai/          # 8104 — GDPR/CCPA data flow mapping
├── legalresearch/          # 8105 — AI legal research memos
│
│ # ✅ Cluster C — Finance & Operations
├── cashflowai/             # 8110 — 90-day cash flow forecasting
├── financenarrator/        # 8111 — AI financial narrative generation
├── expenseintelligence/    # 8112 — Expense policy enforcement
├── vendoriq/               # 8113 — Vendor SLA monitoring
├── invoiceprocessor/       # 8114 — Invoice extraction + PO matching
├── runwaymodeler/          # 8115 — Startup runway modeling
├── procurebot/             # 8116 — AI procurement automation
├── [budgetpilot/]          # 8117 — Budget variance tracking (building)
├── taxdataorganizer/       # 8118 — Tax data organization
├── [equityintelligence/]   # 8119 — Cap table + vesting (building)
│
│ # 🔄 Cluster D — HR & Talent
├── hiresignal/             # 8120 — AI recruiting intelligence
├── interviewos/            # 8121 — Structured interview guides
├── performancenarrative/   # 8122 — AI performance review drafts
├── retentionsignal/        # 8123 — Employee flight risk prediction
├── [compbenchmark/]        # 8124 — Compensation benchmarking (building)
├── [onboardflow/]          # 8125 — Onboarding automation (building)
├── [jobcraftai/]           # 8126 — AI job description generation (building)
└── [peopleanalytics/]      # 8127 — Workforce headcount modeling (building)
```

---

## Development Principles

- **TDD where possible** — Jules writes tests before implementation
- **Schema-per-app** — each app owns its PostgreSQL schema, no cross-schema foreign keys
- **Tenant-scoped queries** — every repository method filters by `tenantId`
- **Event-driven integration** — apps communicate through Nexus Hub events, not direct API calls
- **Spec-first** — every app is fully spec'd before a line of code is written
- **Patch hygiene** — Jules patches applied with `git apply --include='appname/*'` to exclude Maven cache artifacts

---

*Generated and maintained by Claude Code + Jules AI. Last updated: 2026-04-16.*
