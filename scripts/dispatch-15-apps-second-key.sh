#!/usr/bin/env bash
set -euo pipefail

# dispatch-15-apps-second-key.sh — Dispatch remaining 15 apps with 2nd Jules API key
# Sequential dispatch of remaining 15 apps (same as first 15 but different key)

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO="Shashank2577/micro-saas"
BRANCH="main"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"

# Verify protocol file exists
if [[ ! -f "$PROTOCOL_FILE" ]]; then
  echo "❌ Protocol file missing: $PROTOCOL_FILE"
  exit 1
fi

echo "🚀 Jules Sequential Dispatcher — Remaining 15 Apps (2nd API Key)"
echo "📋 Repo: ${REPO}"
echo "🔑 Account: shashank.saxena91"
echo ""

# Check Jules auth
if ! JULES_API_KEY="$JULIUS_API_KEY" jules remote list --repo &>/dev/null; then
  echo "❌ Jules not authenticated with 2nd key"
  exit 1
fi
echo "✅ Jules authenticated with 2nd API key"
echo ""

dispatch_one() {
  local app="$1"
  local display="$2"
  local port="$3"
  local spec_file="$4"

  echo "🛰  Dispatching ${display} (${app})..."

  if [[ ! -f "$spec_file" ]]; then
    echo "  ❌ Spec file not found: $spec_file"
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
- Reference .julius/IMPLEMENTATION_LOG.md in PR body
- SUBMIT IMMEDIATELY — do not wait for review
- 🔴 THIS IS YOUR FINAL STEP — work is complete

═══════════════════════════════════════════════════════════════════════════════
## KEY RULES (MUST FOLLOW)
═══════════════════════════════════════════════════════════════════════════════

✅ Work completely autonomously — no questions, no waiting for feedback
✅ Document EVERYTHING in .julius/ folder (decisions, assumptions, blockers)
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
## JULIUS_AUTONOMOUS_BUILD_PROTOCOL (READ CAREFULLY)
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
  out=$(JULES_API_KEY="$JULIUS_API_KEY" jules remote new --repo "$REPO" --session "$prompt" 2>&1) || {
    echo "  ❌ Failed. Output (tail):"
    echo "$out" | tail -n 10
    return 1
  }

  # Extract session ID
  local sid
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)

  if [[ -z "$sid" ]]; then
    echo "  ⚠️  Dispatched but no session ID parsed. Output:"
    echo "$out" | tail -n 3
    return 1
  fi

  echo "  ✅ session=${sid}"

  # 30 second delay between dispatches
  sleep 30
}

# ═══════════════════════════════════════════════════════════════════════════════
# REMAINING 15 APPS — Dispatched sequentially with 30s delays
# ═══════════════════════════════════════════════════════════════════════════════

dispatch_one "taxoptimizer" "TaxOptimizer" "8203" \
  "${REPO_ROOT}/.omc/sessions/taxoptimizer-cluster-b.json"

dispatch_one "debtnavigator" "DebtNavigator" "8204" \
  "${REPO_ROOT}/.omc/sessions/debtnavigator-cluster-b.json"

dispatch_one "goaltracker" "GoalTracker" "8205" \
  "${REPO_ROOT}/.omc/sessions/goaltracker-cluster-b.json"

dispatch_one "retirementplus" "RetirementPlus" "8207" \
  "${REPO_ROOT}/.omc/sessions/retirementplus-cluster-b.json"

dispatch_one "wealthedge" "WealthEdge" "8208" \
  "${REPO_ROOT}/.omc/sessions/wealthedge-cluster-b.json"

dispatch_one "budgetmaster" "BudgetMaster" "8209" \
  "${REPO_ROOT}/.omc/sessions/budgetmaster-cluster-b.json"

dispatch_one "expenseintelligence" "ExpenseIntelligence" "8222" \
  "${REPO_ROOT}/.omc/sessions/expenseintelligence-cluster-c.json"

dispatch_one "contractsense" "ContractSense" "8223" \
  "${REPO_ROOT}/.omc/sessions/contractsense-cluster-c.json"

dispatch_one "compensationos" "CompensationOS" "8242" \
  "${REPO_ROOT}/.omc/sessions/compensationos-cluster-d.json"

dispatch_one "engagementpulse" "EngagementPulse" "8243" \
  "${REPO_ROOT}/.omc/sessions/engagementpulse-cluster-d.json"

dispatch_one "copyoptimizer" "CopyOptimizer" "8264" \
  "${REPO_ROOT}/.omc/sessions/copyoptimizer-cluster-e.json"

dispatch_one "datagovernanceos" "DataGovernanceOS" "8283" \
  "${REPO_ROOT}/.omc/sessions/datagovernanceos-cluster-f.json"

dispatch_one "datalineagetracker" "DataLineageTracker" "8285" \
  "${REPO_ROOT}/.omc/sessions/datalineagetracker-cluster-f.json"

dispatch_one "customersuccessos" "CustomerSuccessOS" "8302" \
  "${REPO_ROOT}/.omc/sessions/customersuccessos-cluster-g.json"

dispatch_one "documentintelligence" "DocumentIntelligence" "8323" \
  "${REPO_ROOT}/.omc/sessions/documentintelligence-cluster-h.json"

echo ""
echo "✅ All 15 apps dispatched with 2nd API key"
echo ""
echo "🎉 TOTAL: 30 apps now building"
echo "   - 15 apps via Shash2577"
echo "   - 15 apps via shashank.saxena91"
