#!/usr/bin/env bash
set -euo pipefail

REPO="Shashank2577/micro-saas"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_DIR="${REPO_ROOT}/docs/jules/prod-wave-30"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
TRACKER="${REPO_ROOT}/.jules-dispatch-prod-wave-30-wave2.json"
LOG_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-wave2.log"

if [[ ! -f "$PROTOCOL_FILE" ]]; then echo "Missing protocol file"; exit 1; fi
if ! jules remote list --repo >/dev/null 2>&1; then echo "Jules auth missing. Run: jules login"; exit 1; fi
: > "$LOG_FILE"
echo "{\"wave\":\"dispatch-prod-wave-30-wave2\",\"apps\":{}}" > "$TRACKER"

dispatch_one() {
  local app="$1"
  local spec_file="${WAVE_DIR}/specs/${app}.md"
  [[ -f "$spec_file" ]] || { echo "Missing spec for ${app}" | tee -a "$LOG_FILE"; return 1; }
  local spec_content
  spec_content=$(cat "$spec_file")
  local protocol_content
  protocol_content=$(cat "$PROTOCOL_FILE")
  local prompt
  prompt=$(cat <<'PROMPT_EOF'
You are Jules, an autonomous software engineer working in ${REPO}.

CRITICAL EXECUTION MODE
- Work autonomously and tirelessly with no feedback loop.
- No clarifying questions; make reasoned assumptions and document them.
- Push commits after each phase to avoid timeout loss.
- Submit PR when complete.

CONTEXT
- App: ${APP_ID}
- Detailed spec:
${SPEC_CONTENT}

PROTOCOL
${PROTOCOL_CONTENT}

PHASES
- Phase 1: Expand/validate detailed spec into ${APP_ID}/.jules/DETAILED_SPEC.md, commit+push
- Phase 2: Implement backend+frontend+tests+integration contracts, commit+push
- Phase 3: Run tests, fix failures, update .jules/IMPLEMENTATION_LOG.md and HANDOFF.md, commit+push
- Phase 4: Open PR to main with references to .jules docs and acceptance criteria

PROMPT_EOF
)
  prompt="${prompt//\$\{APP_ID\}/${app}}"
  prompt="${prompt//\$\{SPEC_CONTENT\}/${spec_content}}"
  prompt="${prompt//\$\{PROTOCOL_CONTENT\}/${protocol_content}}"
  prompt="${prompt//\$\{REPO\}/${REPO}}"
  local out sid
  out=$(jules remote new --repo "$REPO" --session "$prompt" 2>&1) || {
    echo "${app}=FAILED" | tee -a "$LOG_FILE";
    echo "$out" | tail -n 10;
    return 1;
  }
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)
  [[ -n "$sid" ]] || sid="UNPARSED"
  echo "${app}=${sid}" | tee -a "$LOG_FILE"
  sleep 15
}

dispatch_one "deploysignal"
dispatch_one "cashflowanalyzer"
dispatch_one "complianceradar"
dispatch_one "copyoptimizer"
dispatch_one "dataqualityai"
dispatch_one "financenarrator"
dispatch_one "legalresearch"
dispatch_one "performancenarrative"
dispatch_one "regulatoryfiling"
dispatch_one "cashflowai"
dispatch_one "compbenchmark"
dispatch_one "contractportfolio"
dispatch_one "debtnavigator"
dispatch_one "equityintelligence"
dispatch_one "goaltracker"

echo "Dispatch completed for dispatch-prod-wave-30-wave2"
echo "Log: $LOG_FILE"
