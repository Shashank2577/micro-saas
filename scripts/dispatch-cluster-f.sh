#!/usr/bin/env bash
set -euo pipefail

# dispatch-cluster-f.sh — Dispatch all 11 Cluster F apps to Jules with deep prompts
# Usage: ./scripts/dispatch-cluster-f.sh
#
# Prereqs:
#   - `jules login` completed (CLI authenticated)
#   - JULES_API_KEY exported (sourced from ~/.jules-env if present) — optional, used for REST polling
#   - Clean git working tree preferred (Jules branches off main)

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO="Shashank2577/micro-saas"
BRANCH="main"
SPEC="docs/superpowers/specs/cluster-f-detailed-specs.md"
DRY_RUN="${DRY_RUN:-0}"
if [[ "${1:-}" == "--dry-run" ]]; then DRY_RUN=1; fi
SAMPLE_APP="${SAMPLE_APP:-}"
if [[ "${1:-}" == "--sample" ]]; then DRY_RUN=1; SAMPLE_APP="${2:-datastoryteller}"; fi
# LIMIT=N → dispatch only the first N apps (of 11). Example: LIMIT=8 to send F1-F8 only.
LIMIT="${LIMIT:-0}"
DISPATCH_COUNT=0
UNIVERSAL_HEADER="You are building a production-grade micro-SaaS app. Read ${SPEC} (the Cluster F detailed spec) in full, especially the global conventions at top and the per-app section. This is a 2-hour session — fill it. Scaffold with ./tools/scaffold-app.sh first. Follow cc-starter patterns used in incidentbrain/, contractsense/, cashflowai/, hiresignal/, contentos/ (reference implementations). Build deep: backend + frontend + Flyway migrations + OpenAPI + tests + README + Dockerfile + integration-manifest.json. Do NOT stop at MVP — complete every REST endpoint, every domain entity, every frontend page, every acceptance criterion listed in the spec (both the 10 universal and the app-specific ACs)."

# Source env
if [[ -f "$HOME/.jules-env" ]]; then
  # shellcheck disable=SC1091
  source "$HOME/.jules-env"
fi

echo "🚀 Jules Orchestrator for Cluster F (11 apps, deep prompts)"
echo "📋 Repo: ${REPO}"
echo "📦 Branch: ${BRANCH}"
echo "📖 Spec: ${SPEC}"
echo ""

if ! jules remote list --repo &>/dev/null; then
  echo "❌ Jules CLI not authenticated. Run: jules login"
  exit 1
fi
echo "✅ CLI authenticated"
if [[ -n "${JULES_API_KEY:-}" ]]; then
  echo "🔑 JULES_API_KEY present (masked: ${JULES_API_KEY:0:6}…${JULES_API_KEY: -4})"
else
  echo "⚠️  JULES_API_KEY not set — REST monitoring disabled (dispatch still works)"
fi
echo ""

# Build a rich prompt per app.
make_prompt() {
  local app="$1"
  local display="$2"
  local port="$3"
  local section="$4"
  local one_liner="$5"
  local key_modules="$6"
  local critical_acs="$7"

  cat <<EOF
${UNIVERSAL_HEADER}

APP: ${display} — ${one_liner}
PATH: ${app}/
PORT: ${port}
PACKAGE: com.microsaas.${app}
KEY CC-STARTER MODULES: ${key_modules}
SPEC SECTION: ${SPEC} → '## ${section}' (read it completely before coding)

STEP-BY-STEP:
1. Run: ./tools/scaffold-app.sh ${app} "${display}" ${port}
2. Add cc-starter dependencies listed in the spec's 'Key cc-starter modules' (${key_modules}).
3. Write Flyway migration V1__init.sql creating every table in the 'Domain Model' section of the spec, with tenant_id FK, timestamps, and proper indexes.
4. Implement every JPA entity + repository in com.microsaas.${app}.domain.model and .repository with tenant-scoped queries.
5. Implement service layer (com.microsaas.${app}.service) covering all business logic for every REST endpoint in the spec.
6. Implement REST controllers (com.microsaas.${app}.api) for EVERY endpoint listed — do not skip any.
7. Wire AI via LiteLLM client (http://localhost:4000, model claude-sonnet-4-6) in com.microsaas.${app}.ai with retry + circuit breaker + ai_usage logging. Implement every AI pipeline described in the spec.
8. Implement event emitters/consumers in com.microsaas.${app}.event for the integration-manifest contract.
9. Add springdoc-openapi; expose /v3/api-docs and /swagger-ui.html. Document every endpoint with @Operation.
10. Build Next.js 14 frontend at ${app}/frontend/ covering every page listed in the spec. Use Tailwind + shadcn/ui. Generate TS types from OpenAPI with openapi-typescript.
11. Write tests: @SpringBootTest integration tests for every controller; service unit tests; Testcontainers for Postgres; Vitest + Testing Library for frontend components. Target ≥80% service-layer line coverage.
12. Write ${app}/Dockerfile (multi-stage: maven build → eclipse-temurin:21-jre) and add service to infra/compose.f.yml.
13. Write ${app}/README.md: purpose, ports, env vars, run locally, test, deploy.
14. Create ${app}/integration-manifest.json exactly matching the spec's 'Integration manifest' JSON.
15. At startup, register with Nexus Hub (http://localhost:8090/api/v1/apps/register).
16. Verify every acceptance criterion in the spec — all 10 universal + every app-specific AC — and write an automated test proving each.

HARD REQUIREMENTS (these are the critical ACs; all must pass):
$(printf '%b' "${critical_acs}")

QUALITY BAR:
- mvn -pl ${app}/backend clean verify must pass locally.
- npm --prefix ${app}/frontend run build && npm test must pass.
- /swagger-ui.html renders with every endpoint documented.
- At least one emits event is asserted in an integration test against a mock Nexus subscriber.
- Cross-tenant read attempts must fail (403/404) — include a test proving this.
- No TODOs or stub returns in service/controller code.

Commit per logical unit (migrations, entities, service, controller, AI, frontend, tests) and open one PR to main for the whole app.
EOF
}

dispatch_one() {
  local app="$1"; shift
  local display="$1"; shift
  local port="$1"; shift
  local section="$1"; shift
  local one_liner="$1"; shift
  local key_modules="$1"; shift
  local critical_acs="$1"; shift

  # Enforce LIMIT (only count real dispatch slots; dry-run is unaffected).
  DISPATCH_COUNT=$((DISPATCH_COUNT + 1))
  if [[ "$LIMIT" != "0" && "$DISPATCH_COUNT" -gt "$LIMIT" ]]; then
    echo "⏭  Skipping ${display} (LIMIT=${LIMIT} reached)."
    return 0
  fi

  local title="Build ${display} at ${app}/ — ${one_liner} (Cluster F, port ${port})"
  local body
  body="$(make_prompt "$app" "$display" "$port" "$section" "$one_liner" "$key_modules" "$critical_acs")"
  # Jules CLI: `--session` carries the task prompt. Prepend a title line so the
  # web UI derives a clean session name, then the full body follows.
  local full_prompt="${title}

${body}"

  if [[ "$DRY_RUN" == "1" ]]; then
    if [[ -n "$SAMPLE_APP" && "$SAMPLE_APP" != "$app" ]]; then
      return 0
    fi
    echo "────────── DRY RUN: ${display} (${app}, port ${port}) ──────────"
    echo "$full_prompt"
    echo "────────── END ──────────"
    echo ""
    return 0
  fi

  echo "🛰  Dispatching ${display}..."
  local out
  out=$(jules remote new --repo "$REPO" --session "$full_prompt" 2>&1) || {
    echo "  ❌ Failed. Output (tail):"
    echo "$out" | tail -n 10
    echo "${app}=FAILED" >> "$REPO_ROOT/.jules-cluster-f-dispatch.log"
    return 1
  }
  # Extract a session ID if the CLI prints one (format: 15–20 digit numeric).
  local sid
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)
  if [[ -z "$sid" ]]; then
    echo "  ⚠️  Dispatched but no session ID parsed. Output tail:"
    echo "$out" | tail -n 6
    sid="UNPARSED"
  else
    echo "  ✅ session=$sid"
  fi
  echo "${app}=${sid}" >> "$REPO_ROOT/.jules-cluster-f-dispatch.log"
  # Tiny delay between dispatches (rate-limit hygiene).
  sleep 3
}

: > "$REPO_ROOT/.jules-cluster-f-dispatch.log"

# ---------- F1 DataStoryTeller ----------
dispatch_one datastoryteller "DataStoryTeller" 8150 \
  "1. DataStoryTeller  — port 8150" \
  "AI data narrative + insight generation from BI/warehouse data" \
  "ai, export, webhooks, queue, notifications, storage" \
  "- Generate endpoint returns a markdown narrative referencing at least 3 metrics.\n- Attribution endpoint decomposes a 20% delta into at least 3 segment drivers with contribution %.\n- Q&A endpoint executes NL->SQL->prose end-to-end.\n- Scheduled delivery fires Slack webhook (mock assertable).\n- Chart-describe accepts a PNG and returns 50+ word grounded description."

# ---------- F2 PipelineGuardian ----------
dispatch_one pipelineguardian "PipelineGuardian" 8151 \
  "2. PipelineGuardian — port 8151" \
  "AI data pipeline monitoring with lineage-aware root cause analysis" \
  "queue, notifications, ai, webhooks, audit, search" \
  "- Volume anomaly detection (EWMA + z-score) catches seeded failures exactly.\n- Schema diff emits schema.changed.\n- Lineage query depth=3 returns correct DAG.\n- RCA endpoint returns ≥3 ranked hypotheses with citations.\n- Blast-radius returns all affected downstream assets."

# ---------- F3 MetricsHub ----------
dispatch_one metricshub "MetricsHub" 8152 \
  "3. MetricsHub — port 8152" \
  "Semantic metric registry with AI consistency checker (agent-callable)" \
  "search, audit, webhooks, ai, export" \
  "- Conflict detector surfaces semantically duplicate metrics with score >0.75.\n- Approval workflow blocks non-approvers (403).\n- GET /metrics/{name}/value matches a hand-written control query.\n- Bulk YAML import is idempotent.\n- AI description grounded in the SQL references."

# ---------- F4 DataCatalogAI ----------
dispatch_one datacatalogai "DataCatalogAI" 8153 \
  "4. DataCatalogAI — port 8153" \
  "Self-maintaining data catalog — auto-docs + PII + lineage (agent-callable)" \
  "search, storage, audit, queue, ai" \
  "- Crawler indexes fixture schema with correct asset/column counts.\n- Auto-doc produces ≥2-sentence description.\n- PII detection flags fake emails with confidence >0.85.\n- Semantic search returns the revenue-like asset in top 3 for 'monthly revenue'.\n- Lineage inference creates correct upstream/downstream edges from query log."

# ---------- F5 ExperimentEngine ----------
dispatch_one experimentengine "ExperimentEngine" 8154 \
  "5. ExperimentEngine — port 8154" \
  "A/B testing with real statistical rigor and feature-flag rollout" \
  "flags, ai, export, queue, webhooks" \
  "- Stats engine p-value within ±0.001 of scipy on 10k seeded assignments.\n- Assignment is deterministic (same unit always → same variant).\n- Peek protection emits warning on repeated early checks.\n- Conclude + rollout toggles the feature flag.\n- Guardrail breach triggers experiment.guardrail-violated event."

# ---------- F6 CustomerSignal ----------
dispatch_one customersignal "CustomerSignal" 8155 \
  "6. CustomerSignal — port 8155" \
  "AI customer intelligence: health scores, churn prediction, CLV, expansion" \
  "ai, queue, notifications, webhooks, export" \
  "- Health bucket distribution looks correct on 1k seeded customers.\n- Churn probability in [0,1] for every customer.\n- NL→segment rule JSON for 3 seed prompts.\n- Play triggers produce play.triggered event.\n- What-changed narrative names ≥2 contributing signals for a score drop."

# ---------- F7 ForecastAI ----------
dispatch_one forecastai "ForecastAI" 8156 \
  "7. ForecastAI — port 8156" \
  "Revenue/demand forecasting with calibrated CIs + what-needs-to-be-true" \
  "ai, export, notifications, queue" \
  "- 3-month MAPE <10% on 36-month synthetic data.\n- Conformal interval coverage ≥90% on holdout.\n- Scenario adjustment produces expected forecast shift.\n- WNTBT flags aggressive targets.\n- Explain-forecast narrative cites seasonal + pipeline factors."

# ---------- F8 AlertIntelligence ----------
dispatch_one alertintelligence "AlertIntelligence" 8157 \
  "8. AlertIntelligence — port 8157" \
  "Alert correlation + classification — kill alert fatigue" \
  "queue, notifications, ai, webhooks, audit, search" \
  "- 100 alerts same service in 5 min → exactly 1 correlated incident.\n- Classifier ≥80% accuracy on labeled test set.\n- Mute window suppresses matching alerts.\n- Runbook search returns top-3 for a seeded alert.\n- Critical-sev rule fires Slack webhook (mock asserted)."

# ---------- F9 DataQualityAI ----------
dispatch_one dataqualityai "DataQualityAI" 8158 \
  "9. DataQualityAI — port 8158" \
  "Continuous DQ monitoring: drift, nulls, FK, distribution shift + AI remediation" \
  "queue, notifications, audit, webhooks, ai" \
  "- Seed 5 tests; run produces correct PASS/FAIL mix.\n- Inject NULL → NOT_NULL test FAILs + auto-ticket.\n- PSI > 0.2 detected on shifted fixture.\n- Score recompute drops >10 pts on 2 critical failures.\n- Suggest-fix returns SQL referencing the failing column."

# ---------- F10 ReportAutomator ----------
dispatch_one reportautomator "ReportAutomator" 8159 \
  "10. ReportAutomator — port 8159" \
  "Automated report rendering + delivery with AI narrative commentary" \
  "export, queue, ai, notifications, storage" \
  "- Preview renders <3 s for 10-placeholder template.\n- Cron schedule fires and creates a run in-test.\n- Approval-required template blocks auto-delivery.\n- Vega-lite chart artifact produced as PNG.\n- PDF export file opens as valid PDF."

# ---------- F11 EthicsMonitor ----------
dispatch_one ethicsmonitor "EthicsMonitor" 8160 \
  "11. EthicsMonitor — port 8160" \
  "ML ethics/bias/drift monitoring for EU AI Act compliance" \
  "audit, queue, ai, export, notifications" \
  "- Demographic parity computed for 2 groups on provided batch.\n- PSI > 0.25 on input drift yields drift.detected event.\n- Gate-deploy blocks model failing fairness threshold.\n- Model card output has ≥8 Annex IV sections.\n- Audit bundle download returns zip with card + violations + metrics."

if [[ "$DRY_RUN" == "1" ]]; then
  echo ""
  echo "🔍 DRY RUN complete — no sessions dispatched."
  exit 0
fi

echo ""
echo "✅ Cluster F dispatched. Session IDs logged to: .jules-cluster-f-dispatch.log"
echo ""
echo "📊 Monitor:"
echo "  jules remote list --session"
echo ""
echo "🔗 Track at: https://jules.google.com"
echo ""
echo "📦 When sessions complete, apply patches:"
echo "  jules remote pull --session <sid> > /tmp/<app>.patch"
echo "  git apply --include='<app>/*' /tmp/<app>.patch"
echo "  git add <app>/ && git commit -m 'feat(<app>): apply Jules patch (F#)'"
