#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 ]]; then
  echo "Usage: $0 <app-id>" >&2
  exit 2
fi

APP="$1"
REPO="Shashank2577/micro-saas"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_DIR="${REPO_ROOT}/docs/jules/prod-wave-30"
SPEC_FILE="${WAVE_DIR}/specs/${APP}.md"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"

if [[ ! -f "$SPEC_FILE" ]]; then
  echo "${APP}=FAILED_NO_SPEC"
  exit 1
fi
if [[ ! -f "$PROTOCOL_FILE" ]]; then
  echo "${APP}=FAILED_NO_PROTOCOL"
  exit 1
fi

SPEC_CONTENT="$(cat "$SPEC_FILE")"
PROTOCOL_CONTENT="$(cat "$PROTOCOL_FILE")"

PROMPT=$(cat <<'PROMPT_EOF'
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

PROMPT="${PROMPT//\$\{APP_ID\}/${APP}}"
PROMPT="${PROMPT//\$\{SPEC_CONTENT\}/${SPEC_CONTENT}}"
PROMPT="${PROMPT//\$\{PROTOCOL_CONTENT\}/${PROTOCOL_CONTENT}}"
PROMPT="${PROMPT//\$\{REPO\}/${REPO}}"

OUT="$(jules remote new --repo "$REPO" --session "$PROMPT" 2>&1)" || {
  echo "${APP}=FAILED"
  echo "$OUT" | tail -n 8 >&2
  exit 1
}

SID="$(echo "$OUT" | grep -Eo '[0-9]{15,}' | head -n1 || true)"
if [[ -z "$SID" ]]; then
  echo "${APP}=UNPARSED"
  echo "$OUT" | tail -n 6 >&2
  exit 3
fi

echo "${APP}=${SID}"
