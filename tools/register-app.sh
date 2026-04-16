#!/usr/bin/env bash
set -euo pipefail

# register-app.sh — Register an app with Nexus Hub
# Usage: ./tools/register-app.sh <app-name>
# Reads integration-manifest.json from <app-name>/integration-manifest.json

APP_NAME="${1:?Usage: register-app.sh <app-name>}"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MANIFEST_FILE="${REPO_ROOT}/${APP_NAME}/integration-manifest.json"
HUB_URL="${HUB_URL:-http://localhost:8090}"
TENANT_ID="${TENANT_ID:-00000000-0000-0000-0000-000000000001}"

if [ ! -f "${MANIFEST_FILE}" ]; then
  echo "ERROR: integration-manifest.json not found at ${MANIFEST_FILE}"
  exit 1
fi

echo "Registering ${APP_NAME} with Nexus Hub at ${HUB_URL}..."

DISPLAY_NAME=$(jq -r '.displayName' "${MANIFEST_FILE}")
BASE_URL=$(jq -r '.baseUrl' "${MANIFEST_FILE}")
MANIFEST=$(cat "${MANIFEST_FILE}")

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${HUB_URL}/api/v1/apps/register" \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: ${TENANT_ID}" \
  -d "{\"name\": \"${APP_NAME}\", \"displayName\": \"${DISPLAY_NAME}\", \"baseUrl\": \"${BASE_URL}\", \"manifest\": ${MANIFEST}}")

HTTP_CODE=$(echo "${RESPONSE}" | tail -1)
BODY=$(echo "${RESPONSE}" | head -1)

if [ "${HTTP_CODE}" == "200" ]; then
  echo "✓ Registered successfully. App ID: $(echo "${BODY}" | jq -r '.id')"
else
  echo "ERROR: Registration failed with HTTP ${HTTP_CODE}"
  echo "${BODY}"
  exit 1
fi
