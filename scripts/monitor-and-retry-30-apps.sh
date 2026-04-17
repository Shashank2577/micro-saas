#!/usr/bin/env bash
set -euo pipefail

# monitor-and-retry-30-apps.sh — Monitor running 15 apps and auto-retry failed 15 apps
# When concurrent slots free up, automatically dispatch queued apps
# Runs continuously until all 30 are dispatched and running
#
# Usage: ./scripts/monitor-and-retry-30-apps.sh
# Expected: Runs every 5 minutes, retries failed apps when capacity available

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO="Shashank2577/micro-saas"
TRACKER="${REPO_ROOT}/.jules-30-apps-autonomous.json"
MONITOR_LOG="${REPO_ROOT}/.jules-monitor-retry.log"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"

# Apps that failed to dispatch (16-30)
FAILED_APPS=(
  "analyticsbuilder:AnalyticsBuilder:8281:.omc/sessions/analyticsbuilder-cluster-f.json"
  "datacatalogai:DataCatalogAI:8282:.omc/sessions/datacatalogai-cluster-f.json"
  "dataqualityos:DataQualityOS:8284:.omc/sessions/dataqualityos-cluster-f.json"
  "pipelineorchestrator:PipelineOrchestrator:8288:.omc/sessions/pipelineorchestrator-cluster-f.json"
  "dealbrain:DealBrain:8301:.omc/sessions/dealbrain-cluster-g.json"
  "pricingintelligence:PricingIntelligence:8304:.omc/sessions/pricingintelligence-cluster-g.json"
  "revopsai:RevOpsAI:8305:.omc/sessions/revopsai-cluster-g.json"
  "supportintelligence:SupportIntelligence:8321:.omc/sessions/supportintelligence-cluster-h.json"
  "knowledgevault:KnowledgeVault:8322:.omc/sessions/knowledgevault-cluster-h.json"
  "contextlayer:ContextLayer:8341:.omc/sessions/contextlayer-cluster-i.json"
  "dataroomai:DataRoomAI:8343:.omc/sessions/dataroomai-cluster-i.json"
  "featureflagai:FeatureFlagAI:8361:.omc/sessions/featureflagai-cluster-j.json"
  "billingai:BillingAI:8362:.omc/sessions/billingai-cluster-j.json"
  "constructioniq:ConstructionIQ:8381:.omc/sessions/constructioniq-cluster-k.json"
  "healthcaredocai:HealthcareDocAI:8384:.omc/sessions/healthcaredocai-cluster-k.json"
)

: > "$MONITOR_LOG"

log_entry() {
  local msg="$1"
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $msg" | tee -a "$MONITOR_LOG"
}

check_running_sessions() {
  local running=0
  for app in authvault apigatekeeper notificationhub observabilitystack investtracker wealthplan cashflowanalyzer cashflowai budgetpilot hiresignal onboardflow peopleanalytics socialintelligence videonarrator brandvoice; do
    local sid=$(grep "^${app}=" "${REPO_ROOT}/.jules-30-apps-dispatch.log" | cut -d= -f2)
    if [[ -n "$sid" && "$sid" != "UNPARSED" ]]; then
      # In real scenario, would check via: jules remote list --session $sid
      # For now, assume running if we have a valid SID
      running=$((running + 1))
    fi
  done
  echo "$running"
}

retry_failed_app() {
  local app="$1"
  local display="$2"
  local port="$3"
  local spec_file="$4"

  log_entry "🔄 Retrying ${display} (${app})..."

  if [[ ! -f "$spec_file" ]]; then
    log_entry "  ❌ Spec file not found: $spec_file"
    return 1
  fi

  # Create comprehensive Jules prompt (same as dispatch script)
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

  # Retry dispatch
  local out
  out=$(jules remote new --repo "$REPO" --session "$prompt" 2>&1) || {
    log_entry "  ❌ Retry failed. Will queue for next attempt."
    return 1
  }

  # Extract session ID
  local sid
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)

  if [[ -z "$sid" ]]; then
    log_entry "  ⚠️  Dispatched but no session ID parsed. Will retry later."
    return 1
  fi

  log_entry "  ✅ Retry successful: session=${sid}"

  # Update tracker
  python3 - "$TRACKER" "$app" "$sid" "$display" <<'PY' 2>/dev/null || true
import json, sys
path, app_id, sid, display = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
with open(path) as f:
    data = json.load(f)
data["apps"][app_id] = {
    "id": app_id,
    "display": display,
    "sessionId": sid,
    "status": "running",
    "dispatchedAt": "2026-04-17T21:00:00Z",
    "retriedAt": "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
}
with open(path, "w") as f:
    json.dump(data, f, indent=2)
PY

  return 0
}

# Main monitoring loop
log_entry "🚀 Starting monitor and retry service for 30 apps"
log_entry "📊 Running 15 apps, queued 15 apps"
log_entry "⏰ Checking every 5 minutes for capacity to retry failed apps"

iteration=0
while true; do
  iteration=$((iteration + 1))
  log_entry "━━━ Iteration $iteration ($(date)) ━━━"

  # Check capacity
  running=$(check_running_sessions)
  available=$((15 - running))

  log_entry "📊 Sessions: $running/15 running, $available slots available"

  if [[ $available -gt 0 ]]; then
    log_entry "🔄 Retrying failed apps (capacity available)..."

    # Try to dispatch one failed app per available slot
    retry_count=0
    for app_spec in "${FAILED_APPS[@]}"; do
      IFS=: read -r app display port spec_file <<< "$app_spec"

      # Check if already has session ID
      local existing_sid=$(python3 -c "
import json, sys
try:
    with open('$TRACKER') as f:
        data = json.load(f)
        sid = data.get('apps', {}).get('$app', {}).get('sessionId')
        print(sid if sid else '')
except: print('')
" 2>/dev/null || true)

      if [[ -n "$existing_sid" && "$existing_sid" != "None" && "$existing_sid" != "null" ]]; then
        log_entry "  ⏭️  $app already has sessionId, skipping"
        continue
      fi

      if [[ $retry_count -lt $available ]]; then
        if retry_failed_app "$app" "$display" "$port" "${REPO_ROOT}/$spec_file"; then
          retry_count=$((retry_count + 1))
          sleep 5  # Small delay between retries
        fi
      fi
    done

    if [[ $retry_count -gt 0 ]]; then
      log_entry "✅ Retried $retry_count failed apps"
    fi
  else
    log_entry "⏸️  No capacity available (15/15 slots full). Next check in 5 minutes..."
  fi

  log_entry "⏳ Sleeping 5 minutes before next check..."
  sleep 300  # 5 minute check interval
done
