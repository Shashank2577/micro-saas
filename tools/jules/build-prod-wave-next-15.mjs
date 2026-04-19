#!/usr/bin/env node

import fs from "node:fs";
import path from "node:path";

const cwd = process.cwd();
const reportPath = path.join(cwd, ".app-report-data.json");
if (!fs.existsSync(reportPath)) {
  console.error("Missing .app-report-data.json");
  process.exit(1);
}

const report = JSON.parse(fs.readFileSync(reportPath, "utf8"));
const byApp = new Map(report.apps.map((a) => [a.app, a]));

const current30 = new Set([
  "budgetpilot",
  "ghostwriter",
  "invoiceprocessor",
  "jobcraftai",
  "seointelligence",
  "videonarrator",
  "securitypulse",
  "supportintelligence",
  "auditready",
  "constructioniq",
  "contentos",
  "dependencyradar",
  "licenseguard",
  "nexus-hub",
  "policyforge",
  "deploysignal",
  "cashflowanalyzer",
  "complianceradar",
  "copyoptimizer",
  "dataqualityai",
  "financenarrator",
  "legalresearch",
  "performancenarrative",
  "regulatoryfiling",
  "cashflowai",
  "compbenchmark",
  "contractportfolio",
  "debtnavigator",
  "equityintelligence",
  "goaltracker"
]);

const next15 = report.apps
  .filter((a) => !current30.has(a.app))
  .sort((a, b) => a.score - b.score || a.app.localeCompare(b.app))
  .slice(0, 15)
  .map((a) => a.app);

const appCatalog = {
  localizationos: {
    title: "LocalizationOS",
    domain: "Creator & Content Operations",
    route: "localization",
    intent: "Localization workflow orchestration with translation memory and quality checks",
    actors: ["Localization manager", "Content ops", "Regional reviewer"],
    nouns: ["LocalizationProject", "TranslationJob", "TranslationMemory", "LocaleVariant", "QualityIssue", "ReviewTask"],
    modules: ["projects", "jobs", "memory", "variants", "qa", "reviews"],
    emits: ["localizationos.job.completed", "localizationos.issue.flagged", "localizationos.review.requested"],
    consumes: ["contentos.asset.published", "brandvoice.style.updated", "videonarrator.transcript.ready"]
  },
  procurebot: {
    title: "ProcureBot",
    domain: "Finance & Procurement",
    route: "procurement",
    intent: "Procurement workflow automation with approval tiers and vendor intelligence",
    actors: ["Procurement manager", "Finance approver", "Requestor"],
    nouns: ["PurchaseRequest", "ApprovalFlow", "PurchaseOrder", "VendorOffer", "SpendControlRule", "ProcurementEvent"],
    modules: ["requests", "approvals", "orders", "offers", "controls", "events"],
    emits: ["procurebot.request.approved", "procurebot.order.issued", "procurebot.policy.blocked"],
    consumes: ["vendoriq.risk.updated", "budgetpilot.variance.alerted", "policyforge.policy.published"]
  },
  vendoriq: {
    title: "VendorIQ",
    domain: "Finance Operations",
    route: "vendors",
    intent: "Vendor scorecards, SLA tracking, and renewal risk management",
    actors: ["Vendor manager", "Procurement lead", "Finance ops"],
    nouns: ["VendorProfile", "SLAMetric", "RenewalSchedule", "VendorRisk", "ContractTerm", "BenchmarkSnapshot"],
    modules: ["profiles", "sla", "renewals", "risk", "contracts", "benchmarks"],
    emits: ["vendoriq.risk.updated", "vendoriq.renewal.alerted", "vendoriq.sla.breached"],
    consumes: ["invoiceprocessor.invoice.approved", "contractportfolio.contract.indexed", "procurebot.order.issued"]
  },
  runwaymodeler: {
    title: "RunwayModeler",
    domain: "Finance Strategy",
    route: "runway",
    intent: "Cash runway and scenario modeling for strategic planning",
    actors: ["Founder", "CFO", "FP&A analyst"],
    nouns: ["RunwayScenario", "CashProjection", "HeadcountPlan", "SpendLeverage", "FundingAssumption", "SensitivityModel"],
    modules: ["scenarios", "projections", "headcount", "levers", "assumptions", "sensitivity"],
    emits: ["runwaymodeler.scenario.created", "runwaymodeler.runway.alerted", "runwaymodeler.plan.recommended"],
    consumes: ["cashflowai.position.updated", "budgetpilot.reforecast.completed", "equityintelligence.round.updated"]
  },
  analyticsbuilder: {
    title: "AnalyticsBuilder",
    domain: "Data & Analytics",
    route: "analytics",
    intent: "Self-serve dashboard and report builder with governed metrics",
    actors: ["Analyst", "Ops manager", "Exec stakeholder"],
    nouns: ["Dashboard", "Widget", "MetricDefinition", "QueryTemplate", "SharingPolicy", "ScheduledReport"],
    modules: ["dashboards", "widgets", "metrics", "queries", "sharing", "reports"],
    emits: ["analyticsbuilder.dashboard.published", "analyticsbuilder.report.scheduled", "analyticsbuilder.metric.changed"],
    consumes: ["datacatalogai.asset.updated", "dataqualityai.rule.failed", "usageintelligence.event.received"]
  },
  apievolver: {
    title: "APIEvolver",
    domain: "Developer Platform",
    route: "api-evolution",
    intent: "API contract lifecycle management and backward-compatibility analysis",
    actors: ["Platform engineer", "API owner", "Developer advocate"],
    nouns: ["ApiSpec", "ApiVersion", "BreakingChange", "CompatibilityReport", "DeprecationNotice", "SdkArtifact"],
    modules: ["specs", "versions", "diffs", "compatibility", "deprecations", "sdk"],
    emits: ["apievolver.breaking-change.detected", "apievolver.version.published", "apievolver.sdk.generated"],
    consumes: ["apimanager.version.published", "dependencyradar.scan.completed", "securitypulse.finding.created"]
  },
  billingai: {
    title: "BillingAI",
    domain: "Revenue Operations",
    route: "billing",
    intent: "Subscription billing orchestration, dunning, and revenue leakage prevention",
    actors: ["RevOps", "Finance manager", "Support billing specialist"],
    nouns: ["SubscriptionPlan", "InvoiceRun", "DunningFlow", "PaymentAttempt", "RevenueLeakAlert", "TaxRule"],
    modules: ["plans", "invoice-runs", "dunning", "payments", "leakage", "tax"],
    emits: ["billingai.invoice.generated", "billingai.payment.failed", "billingai.dunning.completed"],
    consumes: ["usageintelligence.event.received", "apimanager.key.generated", "notificationhub.channel.registered"]
  },
  creatoranalytics: {
    title: "CreatorAnalytics",
    domain: "Creator Intelligence",
    route: "creator-analytics",
    intent: "Content performance attribution and ROI insights for creator teams",
    actors: ["Content lead", "Growth marketer", "Creator manager"],
    nouns: ["ContentAsset", "ChannelMetric", "AttributionModel", "ROISnapshot", "AudienceSegment", "PerformanceInsight"],
    modules: ["assets", "metrics", "attribution", "roi", "segments", "insights"],
    emits: ["creatoranalytics.roi.updated", "creatoranalytics.insight.generated", "creatoranalytics.segment.identified"],
    consumes: ["contentos.asset.published", "socialintelligence.signal.detected", "seointelligence.rank.alerted"]
  },
  datacatalogai: {
    title: "DataCatalogAI",
    domain: "Data Governance",
    route: "data-catalog",
    intent: "Data asset catalog with ownership, lineage context, and semantic enrichment",
    actors: ["Data steward", "Data engineer", "Analyst"],
    nouns: ["DataAsset", "OwnershipRecord", "LineageRef", "SemanticTag", "PolicyBinding", "DiscoveryQuery"],
    modules: ["assets", "ownership", "lineage", "tags", "policies", "discovery"],
    emits: ["datacatalogai.asset.registered", "datacatalogai.policy.bound", "datacatalogai.asset.deprecated"],
    consumes: ["datalineagetracker.link.updated", "dataqualityai.drift.detected", "datagovernanceos.policy.updated"]
  },
  hiresignal: {
    title: "HireSignal",
    domain: "HR Talent",
    route: "hiring",
    intent: "Candidate fit scoring and hiring pipeline intelligence",
    actors: ["Recruiter", "Hiring manager", "People ops"],
    nouns: ["CandidateProfile", "FitSignal", "InterviewStage", "HiringDecision", "PipelineMetric", "Requisition"],
    modules: ["candidates", "signals", "stages", "decisions", "pipeline", "requisitions"],
    emits: ["hiresignal.candidate.shortlisted", "hiresignal.risk.flagged", "hiresignal.hire.confirmed"],
    consumes: ["jobcraftai.posting.published", "interviewos.score.submitted", "peopleanalytics.org.signal.updated"]
  },
  interviewos: {
    title: "InterviewOS",
    domain: "HR Talent",
    route: "interviews",
    intent: "Structured interview orchestration and evaluator consistency scoring",
    actors: ["Recruiter", "Interviewer", "Hiring manager"],
    nouns: ["InterviewPlan", "QuestionBank", "Scorecard", "EvaluationRecord", "CalibrationSignal", "DecisionPacket"],
    modules: ["plans", "questions", "scorecards", "evaluations", "calibration", "decisions"],
    emits: ["interviewos.score.submitted", "interviewos.calibration.required", "interviewos.packet.ready"],
    consumes: ["hiresignal.candidate.shortlisted", "jobcraftai.posting.published", "performancenarrative.review.finalized"]
  },
  onboardflow: {
    title: "OnboardFlow",
    domain: "HR Operations",
    route: "onboarding",
    intent: "Automated 30/60/90 onboarding workflows and completion insights",
    actors: ["People ops", "Manager", "New employee"],
    nouns: ["OnboardingPlan", "MilestoneChecklist", "TaskAssignment", "CompletionSignal", "Escalation", "ExperienceScore"],
    modules: ["plans", "checklists", "tasks", "completion", "escalations", "experience"],
    emits: ["onboardflow.plan.started", "onboardflow.milestone.completed", "onboardflow.escalation.opened"],
    consumes: ["hiresignal.hire.confirmed", "peopleanalytics.org.signal.updated", "notificationhub.channel.registered"]
  },
  peopleanalytics: {
    title: "PeopleAnalytics",
    domain: "HR Analytics",
    route: "people-analytics",
    intent: "Workforce health, productivity, and org planning analytics",
    actors: ["People analyst", "HRBP", "Leadership"],
    nouns: ["OrgSnapshot", "HeadcountMetric", "AttritionSignal", "EngagementIndicator", "PerformanceTrend", "PlanningScenario"],
    modules: ["org-snapshots", "headcount", "attrition", "engagement", "performance", "planning"],
    emits: ["peopleanalytics.org.signal.updated", "peopleanalytics.attrition.risk.detected", "peopleanalytics.scenario.generated"],
    consumes: ["performancenarrative.review.finalized", "retentionsignal.risk.detected", "onboardflow.milestone.completed"]
  },
  retentionsignal: {
    title: "RetentionSignal",
    domain: "HR Talent",
    route: "retention",
    intent: "Employee flight-risk modeling and intervention recommendation engine",
    actors: ["HRBP", "Manager", "People ops"],
    nouns: ["RetentionRisk", "InterventionPlan", "SentimentSignal", "RiskDriver", "FollowupTask", "OutcomeRecord"],
    modules: ["risk-models", "interventions", "signals", "drivers", "followups", "outcomes"],
    emits: ["retentionsignal.risk.detected", "retentionsignal.intervention.recommended", "retentionsignal.outcome.recorded"],
    consumes: ["peopleanalytics.org.signal.updated", "performancenarrative.review.finalized", "engagementpulse.signal.updated"]
  },
  datagovernanceos: {
    title: "DataGovernanceOS",
    domain: "Data Governance",
    route: "data-governance",
    intent: "Governance policy lifecycle, stewardship workflows, and compliance controls",
    actors: ["Data governance lead", "Data steward", "Compliance analyst"],
    nouns: ["GovernancePolicy", "StewardAssignment", "PolicyViolation", "ExceptionRequest", "ControlCheck", "AuditTrail"],
    modules: ["policies", "stewards", "violations", "exceptions", "controls", "audit"],
    emits: ["datagovernanceos.policy.updated", "datagovernanceos.violation.detected", "datagovernanceos.control.checked"],
    consumes: ["datacatalogai.asset.registered", "dataqualityai.rule.failed", "auditready.gap.opened"]
  }
};

function ensureDir(dirPath) {
  fs.mkdirSync(dirPath, { recursive: true });
}

function routeFromNoun(noun) {
  return noun.replace(/([a-z])([A-Z])/g, "$1-$2").toLowerCase().replace(/[^a-z0-9-]/g, "");
}

function pluralizeResource(resource) {
  if (/[sxz]$/.test(resource) || /(ch|sh)$/.test(resource)) return `${resource}es`;
  if (/[^aeiou]y$/.test(resource)) return `${resource.slice(0, -1)}ies`;
  return `${resource}s`;
}

function entityFields(noun) {
  return [
    ["id", "UUID", "Primary key"],
    ["tenant_id", "UUID", "Tenant key"],
    ["name", "varchar(180)", `${noun} display name`],
    ["status", "varchar(40)", "Lifecycle enum"],
    ["metadata_json", "jsonb", "Extensible attributes"],
    ["created_at", "timestamptz", "Creation time"],
    ["updated_at", "timestamptz", "Last update time"]
  ];
}

function endpoints(cfg) {
  const base = [];
  for (const noun of cfg.nouns.slice(0, 5)) {
    const resource = pluralizeResource(routeFromNoun(noun));
    base.push(`GET /api/v1/${cfg.route}/${resource}`);
    base.push(`POST /api/v1/${cfg.route}/${resource}`);
    base.push(`GET /api/v1/${cfg.route}/${resource}/{id}`);
    base.push(`PATCH /api/v1/${cfg.route}/${resource}/{id}`);
    base.push(`POST /api/v1/${cfg.route}/${resource}/{id}/validate`);
  }
  base.push(`POST /api/v1/${cfg.route}/ai/analyze`);
  base.push(`POST /api/v1/${cfg.route}/workflows/execute`);
  base.push(`GET /api/v1/${cfg.route}/metrics/summary`);
  return base;
}

function specMarkdown(app, cfg, maturity) {
  const risks = maturity?.risks || [];
  const quick = [];
  if (risks.includes("no-backend-tests")) quick.push("Add backend service/controller tests for critical paths.");
  if (risks.includes("no-frontend-tests")) quick.push("Add frontend tests for primary page and mutation flow.");
  if (risks.includes("no-db-migrations")) quick.push("Add Flyway baseline and incremental migrations.");
  if (risks.includes("no-backend-dockerfile")) quick.push("Add backend Dockerfile with Java 21 runtime.");
  if (risks.includes("no-readme")) quick.push("Add operational README with setup, env vars, and runbook.");
  if (!quick.length) quick.push("Expand non-functional and integration testing depth.");

  const entityBlock = cfg.nouns
    .map((n) => {
      const rows = entityFields(n).map((f) => `| ${f[0]} | ${f[1]} | ${f[2]} |`).join("\n");
      return `### ${n}\n| Field | Type | Notes |\n|---|---|---|\n${rows}\n`;
    })
    .join("\n");

  const ac = [
    "All module slices implemented with API + UI + tests.",
    "Strict tenant isolation and RBAC checks on all endpoints.",
    "Event emit/consume contracts implemented and validated.",
    "OpenAPI includes all endpoints from this spec.",
    "Backend and frontend test suites pass in CI.",
    "LiteLLM calls are guarded with timeout/retry/circuit-breaker.",
    "No TODO or stubbed production logic remains."
  ];

  return `# ${cfg.title} — Next-15 Production Progression Spec

## Product Intent
- App: \`${app}\`
- Domain: ${cfg.domain}
- Outcome: ${cfg.intent}
- Primary actors: ${cfg.actors.join(", ")}

## Current Snapshot
- Score: ${maturity?.score ?? "n/a"}
- Tier: ${maturity?.tier ?? "n/a"}
- Depth: ${maturity?.depth ?? "n/a"}
- Risks: ${risks.length ? risks.join(", ") : "none flagged"}
- Low-hanging fruits:
${quick.map((q) => `  - ${q}`).join("\n")}

## Scope (must be complete in one Jules session)
${cfg.modules.map((m, i) => `- [ ] ${i + 1}. ${m}: backend service + API + frontend page + tests`).join("\n")}

## Domain Model
${entityBlock}

## Required Endpoints
${endpoints(cfg).map((e) => `- ${e}`).join("\n")}

## Event Contract
- Emits: ${cfg.emits.join(", ")}
- Consumes: ${cfg.consumes.join(", ")}
- All events include: \`event_id\`, \`tenant_id\`, \`occurred_at\`, \`source_app\`, \`payload\`
- Consumers are idempotent by \`event_id\`

## Non-Functional Requirements
- P95 latency target: <300ms reads, <600ms writes
- Multi-tenant isolation on all reads/writes
- Structured logs with correlation IDs
- OpenAPI completeness and contract tests

## Acceptance Criteria
${ac.map((x, i) => `${i + 1}. ${x}`).join("\n")}

## Build & Verify
- \`mvn -pl ${app}/backend clean verify\`
- \`npm --prefix ${app}/frontend test\`
- \`npm --prefix ${app}/frontend run build\`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in \`${app}/.jules/HANDOFF.md\`
- Submit PR when done
`;
}

function sessionPayload(app, cfg, maturity) {
  return {
    app_id: app,
    title: cfg.title,
    domain: cfg.domain,
    intent: cfg.intent,
    maturity: {
      score: maturity?.score ?? null,
      tier: maturity?.tier ?? null,
      depth: maturity?.depth ?? null,
      risks: maturity?.risks ?? []
    },
    modules: cfg.modules,
    entities: cfg.nouns,
    emits: cfg.emits,
    consumes: cfg.consumes,
    route_prefix: `/api/v1/${cfg.route}`,
    acceptance_criteria: [
      "All modules implemented end-to-end with tests",
      "OpenAPI and integration-manifest aligned with implementation",
      "No TODO/stub logic in production paths",
      "PR submitted with .jules documentation bundle"
    ]
  };
}

function write(filePath, content) {
  ensureDir(path.dirname(filePath));
  fs.writeFileSync(filePath, content);
}

const baseDir = path.join(cwd, "docs/jules/prod-wave-next-15");
const specsDir = path.join(baseDir, "specs");
const sessionsDir = path.join(baseDir, "sessions");
ensureDir(specsDir);
ensureDir(sessionsDir);

for (const app of next15) {
  const cfg = appCatalog[app];
  if (!cfg) throw new Error(`Missing catalog definition for ${app}`);
  const maturity = byApp.get(app);
  write(path.join(specsDir, `${app}.md`), specMarkdown(app, cfg, maturity));
  write(path.join(sessionsDir, `${app}.json`), JSON.stringify(sessionPayload(app, cfg, maturity), null, 2));
}

const planMd = `# Next 15 Apps — Jules Production Progression Plan

## Objective
Prepare the next 15 highest-priority post-wave apps for autonomous Jules execution with detailed specs and session payloads.

## App Order (priority)
${next15.map((a, i) => `${i + 1}. ${a} (score: ${byApp.get(a)?.score ?? "n/a"}, tier: ${byApp.get(a)?.tier ?? "n/a"})`).join("\n")}

## Deliverables
- Detailed per-app specs: \`docs/jules/prod-wave-next-15/specs/*.md\`
- Session payloads: \`docs/jules/prod-wave-next-15/sessions/*.json\`
- Dispatch script: \`scripts/dispatch-prod-wave-next-15.sh\`
- API-key dispatch script: \`scripts/dispatch-prod-wave-next-15-api.sh\`

## Readiness Gate Before Dispatch
1. Confirm current wave capacity has free slots
2. Ensure Jules auth/account context is correct for target key/account
3. Run dispatch script and monitor session ID creation
`;

write(path.join(baseDir, "NEXT_15_PLAN.md"), planMd);
write(
  path.join(baseDir, "wave-manifest.json"),
  JSON.stringify(
    {
      created_at: new Date().toISOString(),
      objective: "Next 15 app production progression pack",
      total_apps: next15.length,
      apps: next15
    },
    null,
    2
  )
);

const dispatchScript = `#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "\${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_DIR="\${REPO_ROOT}/docs/jules/prod-wave-next-15"
PROTOCOL_FILE="\${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
REPO="Shashank2577/micro-saas"
LOG_FILE="\${REPO_ROOT}/.jules-dispatch-prod-wave-next-15.log"

if [[ ! -f "\${PROTOCOL_FILE}" ]]; then
  echo "Missing protocol file"; exit 1
fi
if ! jules remote list --repo >/dev/null 2>&1; then
  echo "Jules auth missing. Run: jules login"; exit 1
fi

: > "\${LOG_FILE}"

dispatch_one() {
  local app="$1"
  local spec_file="\${WAVE_DIR}/specs/\${app}.md"
  [[ -f "\${spec_file}" ]] || { echo "\${app}=FAILED_NO_SPEC" | tee -a "\${LOG_FILE}"; return 1; }

  local spec protocol prompt out sid
  spec="$(cat "\${spec_file}")"
  protocol="$(cat "\${PROTOCOL_FILE}")"
  prompt="You are Jules, autonomous and no-feedback. App: \${app}.\\n\\n\${spec}\\n\\n\${protocol}"
  out="$(jules remote new --repo "\${REPO}" --session "\${prompt}" 2>&1)" || {
    echo "\${app}=FAILED" | tee -a "\${LOG_FILE}"
    echo "\${out}" | tail -n 8
    return 1
  }
  sid="$(echo "\${out}" | grep -Eo '[0-9]{15,}' | head -n1 || true)"
  [[ -n "\${sid}" ]] || sid="UNPARSED"
  echo "\${app}=\${sid}" | tee -a "\${LOG_FILE}"
  sleep 15
}

${next15.map((a) => `dispatch_one "${a}"`).join("\n")}

echo "Done. Log: \${LOG_FILE}"
`;

write(path.join(cwd, "scripts/dispatch-prod-wave-next-15.sh"), dispatchScript);
fs.chmodSync(path.join(cwd, "scripts/dispatch-prod-wave-next-15.sh"), 0o755);

const apiDispatchScript = `#!/usr/bin/env bash
set -euo pipefail

if [[ -z "\${JULES_API_KEY:-}" ]]; then
  echo "JULES_API_KEY is required"
  exit 1
fi

REPO_ROOT="$(cd "$(dirname "\${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_DIR="\${REPO_ROOT}/docs/jules/prod-wave-next-15"
SPEC_DIR="\${WAVE_DIR}/specs"
PROTOCOL_FILE="\${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
LOG_FILE="\${REPO_ROOT}/.jules-dispatch-prod-wave-next-15-api.log"
STATE_FILE="\${REPO_ROOT}/.jules-dispatch-prod-wave-next-15.state"

if [[ ! -f "\${PROTOCOL_FILE}" ]]; then
  echo "Missing protocol file"; exit 1
fi

touch "\${STATE_FILE}"
: > "\${LOG_FILE}"

is_done() {
  local app="$1"
  grep -q "^\${app}=[0-9]\\{15,\\}$" "\${STATE_FILE}"
}

build_prompt() {
  local app="$1"
  local spec_file="\${SPEC_DIR}/\${app}.md"
  [[ -f "\${spec_file}" ]] || return 1

  local spec_content protocol_content
  spec_content="$(cat "\${spec_file}")"
  protocol_content="$(cat "\${PROTOCOL_FILE}")"
  cat <<PROMPT_EOF
You are Jules, an autonomous software engineer working in Shashank2577/micro-saas.

CRITICAL EXECUTION MODE
- Work autonomously and tirelessly with no feedback loop.
- No clarifying questions; make reasoned assumptions and document them.
- Push commits after each phase to avoid timeout loss.
- Submit PR when complete.

CONTEXT
- App: \${app}
- Detailed spec:
\${spec_content}

PROTOCOL
\${protocol_content}

PHASES
- Phase 1: Expand/validate detailed spec into \${app}/.jules/DETAILED_SPEC.md, commit+push
- Phase 2: Implement backend+frontend+tests+integration contracts, commit+push
- Phase 3: Run tests, fix failures, update .jules/IMPLEMENTATION_LOG.md and HANDOFF.md, commit+push
- Phase 4: Open PR to main with references to .jules docs and acceptance criteria
PROMPT_EOF
}

dispatch_one() {
  local app="$1"
  local prompt response sid err
  if ! prompt="$(build_prompt "\${app}")"; then
    echo "\${app}=FAILED_NO_SPEC" | tee -a "\${LOG_FILE}"
    return 1
  fi

  response="$(
    jq -n \
      --arg p "\${prompt}" \
      '{
        prompt:$p,
        sourceContext:{
          source:"sources/github/Shashank2577/micro-saas",
          githubRepoContext:{startingBranch:"main"}
        },
        automationMode:"AUTO_CREATE_PR"
      }' \
    | curl -sS -X POST "https://jules.googleapis.com/v1alpha/sessions" \
      -H "X-Goog-Api-Key: \${JULES_API_KEY}" \
      -H "Content-Type: application/json" \
      -d @-
  )"

  sid="$(echo "\${response}" | jq -r '.id // empty')"
  if [[ -n "\${sid}" && "\${sid}" != "null" ]]; then
    echo "\${app}=\${sid}" | tee -a "\${LOG_FILE}"
    echo "\${app}=\${sid}" >> "\${STATE_FILE}"
    return 0
  fi

  err="$(echo "\${response}" | jq -r '.error.message // "unknown error"')"
  echo "\${app}=FAILED (\${err})" | tee -a "\${LOG_FILE}"
  return 1
}

echo "Dispatching next 15 apps via API key..." | tee -a "\${LOG_FILE}"
while IFS= read -r app; do
  [[ -n "\${app}" ]] || continue
  if is_done "\${app}"; then
    echo "\${app}=SKIP_ALREADY_DISPATCHED" | tee -a "\${LOG_FILE}"
    continue
  fi
  dispatch_one "\${app}" || true
  sleep 5
done < <(
  node -e '
    const m=require("./docs/jules/prod-wave-next-15/wave-manifest.json");
    for (const a of (m.apps||[])) console.log(a);
  '
)

echo "Done. Log: \${LOG_FILE}"
`;

write(path.join(cwd, "scripts/dispatch-prod-wave-next-15-api.sh"), apiDispatchScript);
fs.chmodSync(path.join(cwd, "scripts/dispatch-prod-wave-next-15-api.sh"), 0o755);

console.log(`Generated next-15 plan pack for ${next15.length} apps.`);
