#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${JULES_API_KEY:-}" ]]; then
  echo "JULES_API_KEY is required"
  exit 1
fi

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_MANIFEST="${REPO_ROOT}/docs/jules/prod-wave-30/wave-manifest.json"
SPEC_DIR="${REPO_ROOT}/docs/jules/prod-wave-30/specs"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
STATE_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-drain.state"
LOG_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-wave2-api.log"

if [[ ! -f "$WAVE_MANIFEST" || ! -f "$PROTOCOL_FILE" ]]; then
  echo "Missing required inputs (manifest or protocol file)."
  exit 1
fi

touch "$STATE_FILE"
: > "$LOG_FILE"

is_done() {
  local app="$1"
  grep -q "^${app}=[0-9]\{15,\}$" "$STATE_FILE"
}

build_prompt() {
  local app="$1"
  local spec_file="${SPEC_DIR}/${app}.md"
  if [[ ! -f "$spec_file" ]]; then
    return 1
  fi
  local spec_content protocol_content
  spec_content="$(cat "$spec_file")"
  protocol_content="$(cat "$PROTOCOL_FILE")"
  cat <<PROMPT_EOF
You are Jules, an autonomous software engineer working in Shashank2577/micro-saas.

CRITICAL EXECUTION MODE
- Work autonomously and tirelessly with no feedback loop.
- No clarifying questions; make reasoned assumptions and document them.
- Push commits after each phase to avoid timeout loss.
- Submit PR when complete.

CONTEXT
- App: ${app}
- Detailed spec:
${spec_content}

PROTOCOL
${protocol_content}

PHASES
- Phase 1: Expand/validate detailed spec into ${app}/.jules/DETAILED_SPEC.md, commit+push
- Phase 2: Implement backend+frontend+tests+integration contracts, commit+push
- Phase 3: Run tests, fix failures, update .jules/IMPLEMENTATION_LOG.md and HANDOFF.md, commit+push
- Phase 4: Open PR to main with references to .jules docs and acceptance criteria
PROMPT_EOF
}

dispatch_app() {
  local app="$1"
  local prompt response sid err
  if ! prompt="$(build_prompt "$app")"; then
    echo "${app}=FAILED_NO_SPEC" | tee -a "$LOG_FILE"
    return 1
  fi

  response="$(
    jq -n \
      --arg p "$prompt" \
      '{
        prompt:$p,
        sourceContext:{
          source:"sources/github/Shashank2577/micro-saas",
          githubRepoContext:{startingBranch:"main"}
        },
        automationMode:"AUTO_CREATE_PR"
      }' \
    | curl -sS -X POST "https://jules.googleapis.com/v1alpha/sessions" \
      -H "X-Goog-Api-Key: ${JULES_API_KEY}" \
      -H "Content-Type: application/json" \
      -d @-
  )"

  sid="$(echo "$response" | jq -r '.id // empty')"
  if [[ -n "$sid" && "$sid" != "null" ]]; then
    echo "${app}=${sid}" | tee -a "$LOG_FILE"
    echo "${app}=${sid}" >> "$STATE_FILE"
    return 0
  fi

  err="$(echo "$response" | jq -r '.error.message // "unknown error"')"
  echo "${app}=FAILED (${err})" | tee -a "$LOG_FILE"
  return 1
}

WAVE2_APPS=()
while IFS= read -r app; do
  [[ -n "$app" ]] && WAVE2_APPS+=("$app")
done < <(
  node -e '
    const m=require("./docs/jules/prod-wave-30/wave-manifest.json");
    for (const a of (m.wave_2||[])) console.log(a);
  '
)

echo "Dispatching Wave 2 via API key account..." | tee -a "$LOG_FILE"
for app in "${WAVE2_APPS[@]}"; do
  if is_done "$app"; then
    echo "${app}=SKIP_ALREADY_DISPATCHED" | tee -a "$LOG_FILE"
    continue
  fi
  dispatch_app "$app" || true
  sleep 5
done

echo "Done. Log: $LOG_FILE"
