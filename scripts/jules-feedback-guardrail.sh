#!/usr/bin/env bash
set -uo pipefail

# jules-feedback-guardrail.sh
# Detects Jules sessions blocked on feedback/plan approval and auto-unblocks them.
#
# Usage:
#   JULES_API_KEYS="key1,key2,key3" bash scripts/jules-feedback-guardrail.sh
#
# Optional env:
#   DRY_RUN=1                # Do not send messages/approve plans, only report.
#   MAX_SESSIONS=50          # Limit number of sessions processed (0 = no limit).
#   LOG_FILE=...             # Override log path.
#   OUTPUT_TSV=...           # Override TSV report path.
#
# Optional args:
#   scripts/jules-feedback-guardrail.sh <state-file> [state-file...]
# If no args provided, defaults to:
#   .jules-dispatch-prod-wave-30-drain.state
#   .jules-dispatch-prod-wave-next-15.state

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LOG_FILE="${LOG_FILE:-${REPO_ROOT}/.jules-feedback-guardrail.log}"
OUTPUT_TSV="${OUTPUT_TSV:-${REPO_ROOT}/.jules-feedback-guardrail.last.tsv}"
DRY_RUN="${DRY_RUN:-0}"
MAX_SESSIONS="${MAX_SESSIONS:-0}"

DEFAULT_STATE_FILES=(
  "${REPO_ROOT}/.jules-dispatch-prod-wave-30-drain.state"
  "${REPO_ROOT}/.jules-dispatch-prod-wave-next-15.state"
)
MAX_INDEX_PAGES="${MAX_INDEX_PAGES:-5}"

timestamp_utc() {
  date -u +"%Y-%m-%dT%H:%M:%SZ"
}

log() {
  local msg="$1"
  echo "[$(timestamp_utc)] ${msg}" | tee -a "${LOG_FILE}" >/dev/null
}

add_key() {
  local key="$1"
  [[ -n "${key}" ]] || return 0
  KEYS+=("${key}")
  KEY_LABELS+=("token${#KEYS[@]}")
}

collect_keys() {
  KEYS=()
  KEY_LABELS=()

  if [[ -n "${JULES_API_KEYS:-}" ]]; then
    while IFS= read -r k; do
      add_key "${k}"
    done < <(printf "%s" "${JULES_API_KEYS}" | tr ',' '\n' | sed 's/^[[:space:]]*//; s/[[:space:]]*$//' | awk 'NF>0')
  fi

  add_key "${JULES_API_KEY:-}"
  add_key "${JULES_API_KEY_1:-}"
  add_key "${JULES_API_KEY_2:-}"
  add_key "${JULES_API_KEY_3:-}"

  if [[ ${#KEYS[@]} -eq 0 ]]; then
    echo "No API keys found. Set JULES_API_KEYS or JULES_API_KEY(_1.._3)." >&2
    exit 1
  fi
}

collect_state_files() {
  STATE_FILES=()
  if [[ $# -gt 0 ]]; then
    for f in "$@"; do
      if [[ -f "${f}" ]]; then
        STATE_FILES+=("${f}")
      else
        echo "State file not found: ${f}" >&2
      fi
    done
  else
    for f in "${DEFAULT_STATE_FILES[@]}"; do
      [[ -f "${f}" ]] && STATE_FILES+=("${f}")
    done
  fi

  if [[ ${#STATE_FILES[@]} -eq 0 ]]; then
    echo "No state files found to scan." >&2
    exit 1
  fi
}

build_session_list() {
  SESSION_LIST_FILE="$(mktemp)"
  cat "${STATE_FILES[@]}" \
    | awk -F'=' 'NF==2 && $1 != "" && $2 != "" { gsub(/\r/,"",$1); gsub(/\r/,"",$2); print $1 "\t" $2 }' \
    | awk -F'\t' '!seen[$2]++ { print $0 }' \
    > "${SESSION_LIST_FILE}"
}

probe_session() {
  local session_id="$1"
  FOUND_TOKEN=""
  FOUND_TOKEN_LABEL=""
  FOUND_STATE=""
  FOUND_UPDATE_TIME=""
  FOUND_RESPONSE=""

  local i key label resp
  for ((i = 0; i < ${#KEYS[@]}; i++)); do
    key="${KEYS[$i]}"
    label="${KEY_LABELS[$i]}"
    resp="$(
      curl -sS --connect-timeout 2 --max-time 6 \
        "https://jules.googleapis.com/v1alpha/sessions/${session_id}?fields=name,state,updateTime" \
        -H "X-Goog-Api-Key: ${key}" 2>/dev/null || true
    )"

    if [[ -z "${resp}" ]]; then
      continue
    fi
    if echo "${resp}" | jq -e '.error' >/dev/null 2>&1; then
      continue
    fi

    FOUND_TOKEN="${key}"
    FOUND_TOKEN_LABEL="${label}"
    FOUND_STATE="$(echo "${resp}" | jq -r '.state // "UNKNOWN"')"
    FOUND_UPDATE_TIME="$(echo "${resp}" | jq -r '.updateTime // ""')"
    FOUND_RESPONSE="${resp}"
    return 0
  done
  return 1
}

latest_agent_message() {
  local session_id="$1"
  local key="$2"
  local resp
  resp="$(
    curl -sS --connect-timeout 2 --max-time 8 \
      "https://jules.googleapis.com/v1alpha/sessions/${session_id}/activities?pageSize=100" \
      -H "X-Goog-Api-Key: ${key}" 2>/dev/null || true
  )"

  if [[ -z "${resp}" ]]; then
    echo ""
    return 0
  fi
  if echo "${resp}" | jq -e '.error' >/dev/null 2>&1; then
    echo ""
    return 0
  fi

  echo "${resp}" | jq -r '
    [ .activities[]? | select(.agentMessaged.agentMessage != null) | {t:(.createTime // ""), m:.agentMessaged.agentMessage} ]
    | sort_by(.t)
    | (last.m // "")
  ' | tr '\n' ' ' | sed 's/[[:space:]]\+/ /g' | cut -c1-1200
}

send_autonomy_reply() {
  local app="$1"
  local session_id="$2"
  local key="$3"
  local latest_msg="$4"

  local reply
  reply=$(cat <<EOF
Autonomy directive for this session:
- Continue execution fully autonomously. Do not wait for additional user feedback.
- Do not ask clarifying questions.
- If requirements are ambiguous, choose conservative production-safe defaults and proceed.
- Record assumptions, trade-offs, and unresolved items in ${app}/.jules/HANDOFF.md.
- If blocked by missing credentials/permissions/external services, implement mocks/stubs/fallback paths, keep tests passing, and document exact production follow-ups in ${app}/.jules/HANDOFF.md.
- Complete remaining implementation/testing steps and open/update the PR.
Latest question context: ${latest_msg}
EOF
)

  if [[ "${DRY_RUN}" == "1" ]]; then
    return 0
  fi

  local resp
  resp="$(
    jq -n --arg prompt "${reply}" '{prompt:$prompt}' \
      | curl -sS --connect-timeout 2 --max-time 8 \
          -X POST "https://jules.googleapis.com/v1alpha/sessions/${session_id}:sendMessage" \
          -H "X-Goog-Api-Key: ${key}" \
          -H "Content-Type: application/json" \
          -d @- 2>/dev/null || true
  )"

  if [[ -n "${resp}" ]] && echo "${resp}" | jq -e '.error' >/dev/null 2>&1; then
    return 1
  fi
  return 0
}

approve_plan() {
  local session_id="$1"
  local key="$2"
  if [[ "${DRY_RUN}" == "1" ]]; then
    return 0
  fi

  local resp
  resp="$(
    curl -sS --connect-timeout 2 --max-time 6 \
      -X POST "https://jules.googleapis.com/v1alpha/sessions/${session_id}:approvePlan" \
      -H "X-Goog-Api-Key: ${key}" \
      -H "Content-Type: application/json" \
      -d '{}' 2>/dev/null || true
  )"

  if [[ -n "${resp}" ]] && echo "${resp}" | jq -e '.error' >/dev/null 2>&1; then
    return 1
  fi
  return 0
}

key_for_label() {
  local label="$1"
  local i
  for ((i = 0; i < ${#KEYS[@]}; i++)); do
    if [[ "${KEY_LABELS[$i]}" == "${label}" ]]; then
      echo "${KEYS[$i]}"
      return 0
    fi
  done
  return 1
}

build_session_index() {
  SESSION_INDEX_FILE="$(mktemp)"
  : > "${SESSION_INDEX_FILE}"

  local i key label page page_token url resp next_token
  for ((i = 0; i < ${#KEYS[@]}; i++)); do
    key="${KEYS[$i]}"
    label="${KEY_LABELS[$i]}"
    page=0
    page_token=""
    while :; do
      page=$((page + 1))
      if [[ "${page}" -gt "${MAX_INDEX_PAGES}" ]]; then
        break
      fi

      url="https://jules.googleapis.com/v1alpha/sessions?pageSize=200&fields=sessions(name,state,updateTime),nextPageToken"
      if [[ -n "${page_token}" ]]; then
        url="${url}&pageToken=${page_token}"
      fi

      resp="$(
        curl -sS --connect-timeout 2 --max-time 8 \
          "${url}" \
          -H "X-Goog-Api-Key: ${key}" 2>/dev/null || true
      )"

      if [[ -z "${resp}" ]] || echo "${resp}" | jq -e '.error' >/dev/null 2>&1; then
        break
      fi

      echo "${resp}" | jq -r --arg token "${label}" '
        .sessions[]?
        | [(.name // "" | split("/") | last), $token, (.state // "UNKNOWN"), (.updateTime // "")]
        | @tsv
      ' >> "${SESSION_INDEX_FILE}"

      next_token="$(echo "${resp}" | jq -r '.nextPageToken // ""')"
      if [[ -z "${next_token}" || "${next_token}" == "${page_token}" ]]; then
        break
      fi
      page_token="${next_token}"
    done
  done
}

prefetch_probe() {
  local session_id="$1"
  local row token_label token_key state update_time
  row="$(awk -F $'\t' -v id="${session_id}" '$1 == id {print $2 "\t" $3 "\t" $4; exit}' "${SESSION_INDEX_FILE}")"
  if [[ -z "${row}" ]]; then
    return 1
  fi

  token_label="$(printf "%s" "${row}" | awk -F $'\t' '{print $1}')"
  state="$(printf "%s" "${row}" | awk -F $'\t' '{print $2}')"
  update_time="$(printf "%s" "${row}" | awk -F $'\t' '{print $3}')"
  token_key="$(key_for_label "${token_label}" || true)"

  if [[ -z "${token_key}" ]]; then
    return 1
  fi

  FOUND_TOKEN="${token_key}"
  FOUND_TOKEN_LABEL="${token_label}"
  FOUND_STATE="${state}"
  FOUND_UPDATE_TIME="${update_time}"
  FOUND_RESPONSE=""
  return 0
}

main() {
  collect_keys
  collect_state_files "$@"
  build_session_list
  build_session_index

  : > "${OUTPUT_TSV}"
  printf "app\tsession_id\ttoken\tstate\taction\tupdate_time\tnote\n" >> "${OUTPUT_TSV}"

  local processed=0
  local replied=0
  local approved=0
  local skipped=0
  local missing=0
  local errors=0
  local total=0
  total="$(wc -l < "${SESSION_LIST_FILE}" | tr -d ' ')"

  log "guardrail start: keys=${#KEYS[@]} state_files=${#STATE_FILES[@]} dry_run=${DRY_RUN} total_sessions=${total}"

  while IFS=$'\t' read -r app session_id; do
    [[ -n "${app}" && -n "${session_id}" ]] || continue
    if [[ "${MAX_SESSIONS}" != "0" && "${processed}" -ge "${MAX_SESSIONS}" ]]; then
      break
    fi
    processed=$((processed + 1))

    if ! prefetch_probe "${session_id}" && ! probe_session "${session_id}"; then
      missing=$((missing + 1))
      printf "%s\t%s\tnone\tMISSING\tNO_ACCESS\t\tSession not accessible with provided keys\n" \
        "${app}" "${session_id}" >> "${OUTPUT_TSV}"
      continue
    fi

    local action="SKIP"
    local note=""
    case "${FOUND_STATE}" in
      "AWAITING_USER_FEEDBACK")
        local latest_msg
        latest_msg="$(latest_agent_message "${session_id}" "${FOUND_TOKEN}")"
        if send_autonomy_reply "${app}" "${session_id}" "${FOUND_TOKEN}" "${latest_msg}"; then
          action="AUTO_REPLIED"
          note="Autonomy directive sent"
          replied=$((replied + 1))
        else
          action="ERROR_SEND_MESSAGE"
          note="sendMessage failed"
          errors=$((errors + 1))
        fi
        ;;
      "AWAITING_PLAN_APPROVAL")
        if approve_plan "${session_id}" "${FOUND_TOKEN}"; then
          action="AUTO_APPROVED_PLAN"
          note="approvePlan sent"
          approved=$((approved + 1))
        else
          action="ERROR_APPROVE_PLAN"
          note="approvePlan failed"
          errors=$((errors + 1))
        fi
        ;;
      *)
        action="SKIP_${FOUND_STATE}"
        note="No action needed"
        skipped=$((skipped + 1))
        ;;
    esac

    printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\n" \
      "${app}" "${session_id}" "${FOUND_TOKEN_LABEL}" "${FOUND_STATE}" "${action}" "${FOUND_UPDATE_TIME}" "${note}" \
      >> "${OUTPUT_TSV}"

    if (( processed % 10 == 0 )); then
      echo "Progress: ${processed}/${total}"
    fi
  done < "${SESSION_LIST_FILE}"

  rm -f "${SESSION_LIST_FILE}"
  rm -f "${SESSION_INDEX_FILE}"

  log "guardrail done: processed=${processed} replied=${replied} approved=${approved} skipped=${skipped} missing=${missing} errors=${errors}"

  echo "Processed: ${processed}"
  echo "Auto-replied feedback blocks: ${replied}"
  echo "Auto-approved plans: ${approved}"
  echo "Skipped: ${skipped}"
  echo "Missing: ${missing}"
  echo "Errors: ${errors}"
  echo "Report: ${OUTPUT_TSV}"
}

main "$@"
