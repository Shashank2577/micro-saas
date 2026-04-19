#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_MANIFEST="${REPO_ROOT}/docs/jules/prod-wave-30/wave-manifest.json"
STATE_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-drain.state"
LOG_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-drain.log"
APPS_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-30-drain.apps"

if [[ ! -f "$WAVE_MANIFEST" ]]; then
  echo "Missing manifest: $WAVE_MANIFEST"
  exit 1
fi

if ! jules remote list --repo >/dev/null 2>&1; then
  echo "Jules auth missing. Run: jules login"
  exit 1
fi

node -e '
  const m=require("./docs/jules/prod-wave-30/wave-manifest.json");
  const all=[...(m.wave_1||[]), ...(m.wave_2||[])];
  for (const a of all) console.log(a);
' > "$APPS_FILE"

touch "$STATE_FILE"
: > "$LOG_FILE"

is_done() {
  local app="$1"
  grep -q "^${app}=[0-9]\{15,\}$" "$STATE_FILE"
}

pending_count() {
  local count=0
  while IFS= read -r app; do
    [[ -z "$app" ]] && continue
    if ! is_done "$app"; then
      count=$((count + 1))
    fi
  done < "$APPS_FILE"
  echo "$count"
}

echo "Starting drain dispatcher. Total apps: $(wc -l < "$APPS_FILE")" | tee -a "$LOG_FILE"
echo "Existing done: $(grep -Ec '=[0-9]{15,}$' "$STATE_FILE" || true)" | tee -a "$LOG_FILE"

attempt=0
while [[ "$(pending_count)" -gt 0 ]]; do
  attempt=$((attempt + 1))
  echo "---- Attempt ${attempt} | pending=$(pending_count) ----" | tee -a "$LOG_FILE"

  while IFS= read -r app; do
    [[ -z "$app" ]] && continue
    if is_done "$app"; then
      continue
    fi

    out="$("${REPO_ROOT}/scripts/dispatch-prod-wave-30-single.sh" "$app" 2>&1)" || true
    echo "$out" | tee -a "$LOG_FILE"

    if echo "$out" | grep -qE "^${app}=[0-9]{15,}$"; then
      echo "$out" >> "$STATE_FILE"
      sleep 15
    else
      sleep 5
    fi
  done < "$APPS_FILE"

  if [[ "$(pending_count)" -gt 0 ]]; then
    echo "Pending apps remain. Sleeping 180s before retry." | tee -a "$LOG_FILE"
    sleep 180
  fi
done

echo "All apps dispatched with valid session IDs." | tee -a "$LOG_FILE"
echo "State file: $STATE_FILE"
