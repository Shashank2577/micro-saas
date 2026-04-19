#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WAVE_DIR="${REPO_ROOT}/docs/jules/prod-wave-next-15"
PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
REPO="Shashank2577/micro-saas"
LOG_FILE="${REPO_ROOT}/.jules-dispatch-prod-wave-next-15.log"

if [[ ! -f "${PROTOCOL_FILE}" ]]; then
  echo "Missing protocol file"; exit 1
fi
if ! jules remote list --repo >/dev/null 2>&1; then
  echo "Jules auth missing. Run: jules login"; exit 1
fi

: > "${LOG_FILE}"

dispatch_one() {
  local app="$1"
  local spec_file="${WAVE_DIR}/specs/${app}.md"
  [[ -f "${spec_file}" ]] || { echo "${app}=FAILED_NO_SPEC" | tee -a "${LOG_FILE}"; return 1; }

  local spec protocol prompt out sid
  spec="$(cat "${spec_file}")"
  protocol="$(cat "${PROTOCOL_FILE}")"
  prompt="You are Jules, autonomous and no-feedback. App: ${app}.\n\n${spec}\n\n${protocol}"
  out="$(jules remote new --repo "${REPO}" --session "${prompt}" 2>&1)" || {
    echo "${app}=FAILED" | tee -a "${LOG_FILE}"
    echo "${out}" | tail -n 8
    return 1
  }
  sid="$(echo "${out}" | grep -Eo '[0-9]{15,}' | head -n1 || true)"
  [[ -n "${sid}" ]] || sid="UNPARSED"
  echo "${app}=${sid}" | tee -a "${LOG_FILE}"
  sleep 15
}

dispatch_one "localizationos"
dispatch_one "procurebot"
dispatch_one "vendoriq"
dispatch_one "runwaymodeler"
dispatch_one "analyticsbuilder"
dispatch_one "apievolver"
dispatch_one "billingai"
dispatch_one "creatoranalytics"
dispatch_one "datacatalogai"
dispatch_one "hiresignal"
dispatch_one "interviewos"
dispatch_one "onboardflow"
dispatch_one "peopleanalytics"
dispatch_one "retentionsignal"
dispatch_one "datagovernanceos"

echo "Done. Log: ${LOG_FILE}"
