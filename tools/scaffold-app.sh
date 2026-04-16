#!/usr/bin/env bash
set -euo pipefail

APP_NAME="${1:?Usage: scaffold-app.sh <app-name> <display-name> <port>}"
DISPLAY_NAME="${2:?Provide a display name}"
PORT="${3:?Provide a backend port (e.g. 8091)}"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CC_TEMPLATE="${REPO_ROOT}/nexus-hub"
TARGET="${REPO_ROOT}/${APP_NAME}"
JAVA_PACKAGE="com.microsaas.${APP_NAME//-/}"

echo "Scaffolding app: ${DISPLAY_NAME} (${APP_NAME}) on port ${PORT}"

cp -r "${CC_TEMPLATE}" "${TARGET}"

# Delete nexus-hub specific packages
rm -rf "${TARGET}/backend/src/main/java/com/microsaas/nexushub/app"
rm -rf "${TARGET}/backend/src/main/java/com/microsaas/nexushub/workflow"
rm -rf "${TARGET}/backend/src/main/java/com/microsaas/nexushub/event"

find "${TARGET}" -type f \( -name "*.java" -o -name "*.xml" -o -name "*.yml" -o -name "*.ts" -o -name "*.json" \) | while read -r file; do
  sed -i \
    -e "s/nexus-hub/${APP_NAME}/g" \
    -e "s/NexusHub/${DISPLAY_NAME//[^a-zA-Z]/}/g" \
    -e "s/nexushub/${APP_NAME//-/}/g" \
    -e "s/8080/${PORT}/g" \
    "${file}"
done

OLD_PKG_DIR="${TARGET}/backend/src/main/java/com/microsaas/${APP_NAME//-/}"
# rename the package directory
mv "${TARGET}/backend/src/main/java/com/microsaas/nexushub" "${OLD_PKG_DIR}"

# Create integration manifest file
cat > "${TARGET}/integration-manifest.json" <<MANIFEST
{
  "app": "${APP_NAME}",
  "displayName": "${DISPLAY_NAME}",
  "version": "0.0.1",
  "baseUrl": "https://api-${APP_NAME}.YOURDOMAIN.com",
  "emits": [],
  "consumes": [],
  "capabilities": [],
  "healthEndpoint": "/actuator/health"
}
MANIFEST

echo "✓ App scaffolded at: ${TARGET}"
