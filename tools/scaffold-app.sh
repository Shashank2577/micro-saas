#!/usr/bin/env bash
set -euo pipefail

# scaffold-app.sh — Bootstrap a new app in the micro-saas ecosystem
# Usage: ./tools/scaffold-app.sh <app-name> <display-name> <port>
# Example: ./tools/scaffold-app.sh incidentbrain "IncidentBrain" 8091

APP_NAME="${1:?Usage: scaffold-app.sh <app-name> <display-name> <port>}"
DISPLAY_NAME="${2:?Provide a display name}"
PORT="${3:?Provide a backend port (e.g. 8091)}"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CC_TEMPLATE="${HOME}/Documents/Personal/Code/cross-cutting/cc-template"
TARGET="${REPO_ROOT}/${APP_NAME}"
JAVA_PACKAGE="com.microsaas.${APP_NAME//-/}"

echo "Scaffolding app: ${DISPLAY_NAME} (${APP_NAME}) on port ${PORT}"

# 1. Copy cc-template as the base
if [ ! -d "${CC_TEMPLATE}" ]; then
  echo "ERROR: cc-template not found at ${CC_TEMPLATE}"
  exit 1
fi

cp -r "${CC_TEMPLATE}" "${TARGET}"

# 2. Replace placeholders in all files
find "${TARGET}" -type f \( -name "*.java" -o -name "*.xml" -o -name "*.yml" -o -name "*.ts" -o -name "*.json" \) | while read -r file; do
  sed -i '' \
    -e "s/cc-template/${APP_NAME}/g" \
    -e "s/CcTemplate/${DISPLAY_NAME//[^a-zA-Z]/}/g" \
    -e "s/com\.crosscutting\.template/${JAVA_PACKAGE}/g" \
    -e "s/8080/${PORT}/g" \
    "${file}"
done

# 3. Rename Java package directories
OLD_PKG_DIR="${TARGET}/backend/src/main/java/com/crosscutting/template"
NEW_PKG_DIR="${TARGET}/backend/src/main/java/${JAVA_PACKAGE//./\/}"
if [ -d "${OLD_PKG_DIR}" ]; then
  mkdir -p "$(dirname "${NEW_PKG_DIR}")"
  mv "${OLD_PKG_DIR}" "${NEW_PKG_DIR}"
fi

# 4. Create integration manifest file
cat > "${TARGET}/integration-manifest.json" <<EOF
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
EOF

# 5. Emit next steps
echo ""
echo "✓ App scaffolded at: ${TARGET}"
echo ""
echo "Next steps:"
echo "  1. cd ${TARGET}/backend && mvn spring-boot:run"
echo "  2. Edit integration-manifest.json to declare emits/consumes/capabilities"
echo "  3. Run ./tools/register-app.sh ${APP_NAME} to register with Nexus Hub"
echo "  4. Run ./freestack-init.sh to provision infrastructure"
