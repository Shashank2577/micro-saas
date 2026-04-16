#!/usr/bin/env bash
set -euo pipefail

# monitor-jules-cluster-a.sh — Check status of Cluster A Jules sessions
# Usage: ./scripts/monitor-jules-cluster-a.sh

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SESSIONS_FILE="${REPO_ROOT}/.jules-cluster-a-sessions.json"

if [ ! -f "${SESSIONS_FILE}" ]; then
  echo "❌ Sessions file not found. Run dispatch-cluster-a.sh first."
  exit 1
fi

echo "📊 Cluster A Jules Session Status"
echo "================================="
echo ""

# Get session IDs from saved file
APP_1_SESSION=$(jq -r '.app1.sessionId' "${SESSIONS_FILE}")
APP_2_SESSION=$(jq -r '.app2.sessionId' "${SESSIONS_FILE}")
APP_3_SESSION=$(jq -r '.app3.sessionId' "${SESSIONS_FILE}")
APP_4_SESSION=$(jq -r '.app4.sessionId' "${SESSIONS_FILE}")
APP_5_SESSION=$(jq -r '.app5.sessionId' "${SESSIONS_FILE}")

# Check each session status
check_session() {
  local app_name=$1
  local session_id=$2

  local output=$(jules remote pull --session "${session_id}" --json 2>/dev/null || echo "{}")
  local state=$(echo "${output}" | jq -r '.state // "UNKNOWN"')
  local title=$(echo "${output}" | jq -r '.title // "N/A"')

  local status_icon=""
  case "${state}" in
    PLANNING) status_icon="📋" ;;
    IN_PROGRESS) status_icon="🟡" ;;
    COMPLETED) status_icon="✅" ;;
    FAILED) status_icon="❌" ;;
    AWAITING_USER_INPUT) status_icon="⏸️" ;;
    *) status_icon="❓" ;;
  esac

  echo "${status_icon} ${app_name}"
  echo "   State: ${state}"
  echo "   Session: ${session_id}"

  if [ "${state}" = "COMPLETED" ]; then
    local pr_url=$(echo "${output}" | jq -r '.pullRequest.url // "N/A"')
    echo "   PR: ${pr_url}"
  fi

  if [ "${state}" = "FAILED" ]; then
    local error=$(echo "${output}" | jq -r '.error.message // "Unknown error"')
    echo "   Error: ${error}"
  fi

  echo ""
}

check_session "IncidentBrain" "${APP_1_SESSION}"
check_session "DependencyRadar" "${APP_2_SESSION}"
check_session "DeploySignal" "${APP_3_SESSION}"
check_session "APIEvolver" "${APP_4_SESSION}"
check_session "SecurityPulse" "${APP_5_SESSION}"

# Summary
echo "================================="
echo "💡 Tip: Check details at https://jules.google.com"
