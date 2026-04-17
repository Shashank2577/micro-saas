#!/usr/bin/env bash
set -euo pipefail

# dispatch-30-apps-autonomous.sh — Dispatch 30 micro-SaaS apps to Jules autonomously
# This script dispatches 30 complete apps using the JULES_AUTONOMOUS_BUILD_PROTOCOL
# Each app is built fully and autonomously — NO USER FEEDBACK during the process
# Jules works independently, documents all decisions, and creates a PR at the end
#
# Usage: ./scripts/dispatch-30-apps-autonomous.sh
# Expected runtime: 14-22 hours per app sequential × 30 apps ≈ 420-660 hours total
# With 15 concurrent Jules capacity: ~28-44 hours total

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO="Shashank2577/micro-saas"
BRANCH="main"
TRACKER="${REPO_ROOT}/.jules-30-apps-autonomous.json"
LOG_FILE="${REPO_ROOT}/.jules-30-apps-dispatch.log"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"

# Verify protocol file exists
if [[ ! -f "$PROTOCOL_FILE" ]]; then
  echo "❌ Protocol file missing: $PROTOCOL_FILE"
  exit 1
fi

echo "🚀 Jules Autonomous Dispatcher — 30 Complete Apps"
echo "📋 Repo: ${REPO}"
echo "📦 Branch: ${BRANCH}"
echo "🔐 Protocol: $(basename $PROTOCOL_FILE)"
echo ""

# Check Jules auth
if ! jules remote list --repo &>/dev/null; then
  echo "❌ Jules not authenticated. Run: jules login"
  exit 1
fi
echo "✅ Jules CLI authenticated (15 concurrent capacity)"
echo ""

# Initialize log
: > "$LOG_FILE"

# Create tracker JSON
cat > "$TRACKER" << 'TRACKER_EOF'
{
  "dispatch-time": "2026-04-17T21:00:00Z",
  "dispatcher": "dispatch-30-apps-autonomous.sh",
  "protocol": "JULES_AUTONOMOUS_BUILD_PROTOCOL.md",
  "total-apps": 30,
  "concurrent-capacity": 15,
  "expected-total-duration": "28-44 hours",
  "apps": {}
}
TRACKER_EOF

dispatch_one() {
  local app="$1"
  local display="$2"
  local port="$3"
  local spec_file="$4"

  echo "🛰  Dispatching ${display} (${app})..."

  # Read spec file
  if [[ ! -f "$spec_file" ]]; then
    echo "  ❌ Spec file not found: $spec_file"
    echo "${app}=FAILED_NO_SPEC" >> "$LOG_FILE"
    return 1
  fi

  # Create comprehensive Jules prompt
  local prompt
  read -r -d '' prompt << 'JULES_PROMPT' || true
You are Jules, an autonomous development agent. You work COMPLETELY INDEPENDENTLY without any user feedback.

═══════════════════════════════════════════════════════════════════════════════
## 🚨 CRITICAL: YOU ARE WORKING AUTONOMOUSLY — NO FEEDBACK WILL BE PROVIDED
═══════════════════════════════════════════════════════════════════════════════

This means:
✅ YOU WILL RECEIVE NO QUESTIONS, NO FEEDBACK, NO APPROVALS
✅ YOU CANNOT WAIT FOR USER INTERACTION — YOU MUST WORK INDEPENDENTLY
✅ YOU CANNOT ASK CLARIFYING QUESTIONS — MAKE REASONABLE ASSUMPTIONS
✅ YOU MUST DOCUMENT ALL DECISIONS IN .jules/ FILES
✅ YOU MUST PUSH CODE REGULARLY (after each phase) SO IT'S NOT LOST IF SESSION TIMES OUT
✅ YOU MUST CREATE AND SUBMIT A PR AT THE END — NO WAITING, NO BLOCKING

═══════════════════════════════════════════════════════════════════════════════
## PREVENTING TIMEOUTS AND DATA LOSS
═══════════════════════════════════════════════════════════════════════════════

CRITICAL: Do NOT accumulate all work and push at the end.

**AFTER EACH PHASE, you MUST:**
1. Commit your work: git add {app}/ && git commit -m "phase-{N}: {description}"
2. Push to branch: git push origin feature/{app}-implementation
3. This ensures if your session times out, the work is preserved and not lost

**Commit Strategy:**
- Phase 1 complete → Commit detailed spec: "feat({app}): phase-1-detailed-spec"
- Phase 2 complete → Commit implementation: "feat({app}): phase-2-full-implementation"
- Phase 3 complete → Commit tests: "feat({app}): phase-3-testing-verification"
- Phase 4 → Create and submit PR (final push automatically happens with PR)

**If you hit a timeout or session dies:**
- All your work is already pushed to the branch
- User can continue from where you left off
- No work is lost
- You are not "leaving them hanging"

═══════════════════════════════════════════════════════════════════════════════
## PROCESS (Follow JULES_AUTONOMOUS_BUILD_PROTOCOL exactly)
═══════════════════════════════════════════════════════════════════════════════

**PHASE 1: Detailed Spec Generation** (2-3 hours)
- Read base spec completely
- Expand to detailed spec (2000-3000 words)
- Include: exact database schema, every REST endpoint, service methods, React components, AI integration points
- Save to .jules/DETAILED_SPEC.md
- 🔴 AFTER THIS PHASE: git add {app}/.jules && git commit && git push

**PHASE 2: Full Implementation** (10-16 hours)
- Backend: JPA entities, repositories, services, controllers, tests (≥80% coverage)
- Frontend: Next.js pages, components, tests
- Integration: integration-manifest.json, docker-compose
- Documentation: README.md, OpenAPI docs
- All code must be COMPLETE (no TODOs, no stubs, no placeholders)
- 🔴 AFTER THIS PHASE: git add {app}/ && git commit && git push

**PHASE 3: Testing & Validation** (1-2 hours)
- Run backend tests: mvn -pl {app}/backend clean verify
- Run frontend tests: npm --prefix {app}/frontend test
- Verify all acceptance criteria from spec are tested
- Fix failures (do NOT ask, do NOT wait)
- 🔴 AFTER THIS PHASE: git add {app}/ && git commit && git push

**PHASE 4: PR Creation & Submission** (30 minutes)
- Create PR to main branch with detailed description
- Reference .jules/DETAILED_SPEC.md in PR body
- Reference .jules/IMPLEMENTATION_LOG.md in PR body
- SUBMIT IMMEDIATELY — do not wait for review
- 🔴 THIS IS YOUR FINAL STEP — work is complete

═══════════════════════════════════════════════════════════════════════════════
## KEY RULES (MUST FOLLOW)
═══════════════════════════════════════════════════════════════════════════════

✅ Work completely autonomously — no questions, no waiting for feedback
✅ Document EVERYTHING in .jules/ folder (decisions, assumptions, blockers)
✅ PUSH AFTER EACH PHASE — don't accumulate work until the end
✅ Make reasonable assumptions if spec is ambiguous — document them
✅ If blocked, try alternative and document why in HANDOFF.md
✅ All production code must be COMPLETE (no TODOs, no stubs, no partial implementations)
✅ Tests must PASS (if failing, fix them — do not ask, do not skip)
✅ CREATE AND SUBMIT PR at the end (no waiting)
✅ DO NOT TIME OUT — push your progress regularly so work isn't lost

═══════════════════════════════════════════════════════════════════════════════
## WHAT YOU ARE BUILDING (Base Spec)
═══════════════════════════════════════════════════════════════════════════════

APP_SPEC_PLACEHOLDER

═══════════════════════════════════════════════════════════════════════════════
## JULES_AUTONOMOUS_BUILD_PROTOCOL (READ CAREFULLY)
═══════════════════════════════════════════════════════════════════════════════

PROTOCOL_PLACEHOLDER

═══════════════════════════════════════════════════════════════════════════════
## NOW BEGIN (YOU HAVE FULL AUTONOMY)
═══════════════════════════════════════════════════════════════════════════════

1. Read the base spec completely
2. Generate DETAILED_SPEC.md (Phase 1) → COMMIT & PUSH
3. Implement fully (Phase 2) → COMMIT & PUSH
4. Run tests (Phase 3) → COMMIT & PUSH
5. Create PR (Phase 4) → SUBMIT

You have complete autonomy. No feedback will be provided. No user interaction expected. Make all decisions independently. Document everything. Push regularly. Create PR at end.

Do not wait. Do not ask. Do not timeout. Build it.
JULES_PROMPT

  # Load protocol content
  local protocol_content=$(cat "$PROTOCOL_FILE")
  prompt="${prompt//PROTOCOL_PLACEHOLDER/$protocol_content}"

  # Load spec content
  local spec_content=$(cat "$spec_file")
  prompt="${prompt//APP_SPEC_PLACEHOLDER/$spec_content}"

  # Dispatch to Jules
  local out
  out=$(jules remote new --repo "$REPO" --session "$prompt" 2>&1) || {
    echo "  ❌ Dispatch failed. Output (tail):"
    echo "$out" | tail -n 10
    echo "${app}=FAILED" >> "$LOG_FILE"
    return 1
  }

  # Extract session ID
  local sid
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)

  if [[ -z "$sid" ]]; then
    echo "  ⚠️  Dispatched but no session ID parsed. Output:"
    echo "$out" | tail -n 3
    sid="UNPARSED"
  else
    echo "  ✅ session=${sid}"
  fi

  echo "${app}=${sid}" >> "$LOG_FILE"

  # Update tracker JSON
  python3 - "$TRACKER" "$app" "$sid" "$display" <<'PY' 2>/dev/null || true
import json, sys
path, app_id, sid, display = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
with open(path) as f:
    data = json.load(f)
data["apps"][app_id] = {
    "id": app_id,
    "display": display,
    "sessionId": sid if sid != "UNPARSED" else None,
    "status": "running" if sid != "UNPARSED" else "failed",
    "dispatchedAt": "2026-04-17T21:00:00Z"
}
with open(path, "w") as f:
    json.dump(data, f, indent=2)
PY

  # 30 second delay between dispatches (respects 15 concurrent limit, allows tracking)
  sleep 30
}

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER A: Infrastructure & Shared Services (4 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "authvault" "AuthVault" "8110" \
  "${REPO_ROOT}/.omc/sessions/authvault-cluster-a.json"

dispatch_one "apigatekeeper" "APIGateway" "8111" \
  "${REPO_ROOT}/.omc/sessions/apigatekeeper-cluster-a.json"

dispatch_one "notificationhub" "NotificationHub" "8113" \
  "${REPO_ROOT}/.omc/sessions/notificationhub-cluster-a.json"

dispatch_one "observabilitystack" "ObservabilityStack" "8114" \
  "${REPO_ROOT}/.omc/sessions/observabilitystack-cluster-a.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER B: Personal Finance & Wealth Management (3 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "investtracker" "InvestTracker" "8201" \
  "${REPO_ROOT}/.omc/sessions/investtracker-cluster-b.json"

dispatch_one "wealthplan" "WealthPlan" "8202" \
  "${REPO_ROOT}/.omc/sessions/wealthplan-cluster-b.json"

dispatch_one "cashflowanalyzer" "CashflowAnalyzer" "8206" \
  "${REPO_ROOT}/.omc/sessions/cashflowanalyzer-cluster-b.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER C: Finance & Operations (2 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "cashflowai" "CashflowAI" "8220" \
  "${REPO_ROOT}/.omc/sessions/cashflowai-cluster-c.json"

dispatch_one "budgetpilot" "BudgetPilot" "8221" \
  "${REPO_ROOT}/.omc/sessions/budgetpilot-cluster-c.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER D: HR & Talent (3 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "hiresignal" "HireSignal" "8241" \
  "${REPO_ROOT}/.omc/sessions/hiresignal-cluster-d.json"

dispatch_one "onboardflow" "OnboardFlow" "8242" \
  "${REPO_ROOT}/.omc/sessions/onboardflow-cluster-d.json"

dispatch_one "peopleanalytics" "PeopleAnalytics" "8245" \
  "${REPO_ROOT}/.omc/sessions/peopleanalytics-cluster-d.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER E: Marketing & Creative (3 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "socialintelligence" "SocialIntelligence" "8261" \
  "${REPO_ROOT}/.omc/sessions/socialintelligence-cluster-e.json"

dispatch_one "videonarrator" "VideoNarrator" "8262" \
  "${REPO_ROOT}/.omc/sessions/videonarrator-cluster-e.json"

dispatch_one "brandvoice" "BrandVoice" "8263" \
  "${REPO_ROOT}/.omc/sessions/brandvoice-cluster-e.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER F: Data & Analytics (4 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "analyticsbuilder" "AnalyticsBuilder" "8281" \
  "${REPO_ROOT}/.omc/sessions/analyticsbuilder-cluster-f.json"

dispatch_one "datacatalogai" "DataCatalogAI" "8282" \
  "${REPO_ROOT}/.omc/sessions/datacatalogai-cluster-f.json"

dispatch_one "dataqualityos" "DataQualityOS" "8284" \
  "${REPO_ROOT}/.omc/sessions/dataqualityos-cluster-f.json"

dispatch_one "pipelineorchestrator" "PipelineOrchestrator" "8288" \
  "${REPO_ROOT}/.omc/sessions/pipelineorchestrator-cluster-f.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER G: Sales & Revenue (3 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "dealbrain" "DealBrain" "8301" \
  "${REPO_ROOT}/.omc/sessions/dealbrain-cluster-g.json"

dispatch_one "pricingintelligence" "PricingIntelligence" "8304" \
  "${REPO_ROOT}/.omc/sessions/pricingintelligence-cluster-g.json"

dispatch_one "revopsai" "RevOpsAI" "8305" \
  "${REPO_ROOT}/.omc/sessions/revopsai-cluster-g.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER H: Operations & Support (2 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "supportintelligence" "SupportIntelligence" "8321" \
  "${REPO_ROOT}/.omc/sessions/supportintelligence-cluster-h.json"

dispatch_one "knowledgevault" "KnowledgeVault" "8322" \
  "${REPO_ROOT}/.omc/sessions/knowledgevault-cluster-h.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER I: AI Infrastructure (2 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "contextlayer" "ContextLayer" "8341" \
  "${REPO_ROOT}/.omc/sessions/contextlayer-cluster-i.json"

dispatch_one "dataroomai" "DataRoomAI" "8343" \
  "${REPO_ROOT}/.omc/sessions/dataroomai-cluster-i.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER J: Platform Core (2 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "featureflagai" "FeatureFlagAI" "8361" \
  "${REPO_ROOT}/.omc/sessions/featureflagai-cluster-j.json"

dispatch_one "billingai" "BillingAI" "8362" \
  "${REPO_ROOT}/.omc/sessions/billingai-cluster-j.json"

# ═══════════════════════════════════════════════════════════════════════════════
# CLUSTER K: Vertical Solutions (2 apps)
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "constructioniq" "ConstructionIQ" "8381" \
  "${REPO_ROOT}/.omc/sessions/constructioniq-cluster-k.json"

dispatch_one "healthcaredocai" "HealthcareDocAI" "8384" \
  "${REPO_ROOT}/.omc/sessions/healthcaredocai-cluster-k.json"

# ═══════════════════════════════════════════════════════════════════════════════
# Dispatch Complete
# ═══════════════════════════════════════════════════════════════════════════════

echo ""
echo "✅ All 30 apps dispatched autonomously"
echo ""
echo "📊 Session tracking:"
echo "  Log file: ${LOG_FILE}"
echo "  Tracker: ${TRACKER}"
echo ""
echo "🔗 Monitor progress:"
echo "  View session logs: cat ${LOG_FILE}"
echo "  Jules dashboard: https://jules.google.com"
echo ""
echo "⏱️  Expected completion:"
echo "  Per app: 14-22 hours"
echo "  All 30 sequential: ~420-660 hours"
echo "  All 30 with 15 concurrent: ~28-44 hours"
echo ""
echo "📦 What to expect:"
echo "  1. Each app builds completely autonomously (no feedback needed)"
echo "  2. Jules documents everything in {app}/.jules/ folder"
echo "  3. Each app creates a PR to main when complete"
echo "  4. Review .jules/DETAILED_SPEC.md and .jules/IMPLEMENTATION_LOG.md for details"
echo ""
echo "🎉 All PRs will appear at: https://github.com/Shashank2577/micro-saas/pulls"
