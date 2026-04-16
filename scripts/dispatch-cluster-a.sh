#!/usr/bin/env bash
set -euo pipefail

# dispatch-cluster-a.sh — Dispatch all 5 Cluster A apps to Jules in parallel
# Usage: ./scripts/dispatch-cluster-a.sh

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PLAN_FILE="${REPO_ROOT}/.jules-plan-cluster-a.json"
REPO="Shashank2577/micro-saas"
BRANCH="main"

echo "🚀 Jules Orchestrator for Cluster A"
echo "📋 Repo: ${REPO}"
echo "📦 Branch: ${BRANCH}"
echo ""

# Check Jules auth
echo "🔑 Checking Jules authentication..."
if ! jules remote list --repo &>/dev/null; then
  echo "❌ Jules not authenticated. Run: jules login"
  exit 1
fi
echo "✅ Authenticated"
echo ""

# Dispatch apps in parallel
echo "📦 Dispatching 5 apps to Jules (5 concurrent max)..."
echo ""

APP_1_SESSION=$(jules new --repo "${REPO}" \
  "Build IncidentBrain - AI Incident Root Cause Analysis Platform. See spec in docs/superpowers/specs/cluster-a-detailed-specs.md. Use cc-starter framework. Bootstrap with tools/scaffold-app.sh. Focus on MVP: incident ingestion, Datadog integration, AI analysis via LiteLLM Claude Sonnet 4.6, postmorter generator." \
  --json 2>/dev/null | jq -r '.sessionId // empty')

APP_2_SESSION=$(jules new --repo "${REPO}" \
  "Build DependencyRadar - Open Source Dependency Monitoring Platform. See spec in docs/superpowers/specs/cluster-a-detailed-specs.md. Use cc-starter framework. Bootstrap with tools/scaffold-app.sh. Focus on MVP: GitHub webhook, Maven/npm parsing, NVD vulnerability lookup, dependency health dashboard." \
  --json 2>/dev/null | jq -r '.sessionId // empty')

APP_3_SESSION=$(jules new --repo "${REPO}" \
  "Build DeploySignal - Deployment Analytics and Risk Assessment Platform. See spec in docs/superpowers/specs/cluster-a-detailed-specs.md. Use cc-starter framework. Bootstrap with tools/scaffold-app.sh. Focus on MVP: GitHub Actions webhook, risk scoring algorithm, DORA metrics dashboard." \
  --json 2>/dev/null | jq -r '.sessionId // empty')

APP_4_SESSION=$(jules new --repo "${REPO}" \
  "Build APIEvolver - API Contract Management with Backward Compatibility Checking. See spec in docs/superpowers/specs/cluster-a-detailed-specs.md. Use cc-starter framework. Bootstrap with tools/scaffold-app.sh. Focus on MVP: OpenAPI spec upload, spec diff algorithm, breaking change detector, consumer registration." \
  --json 2>/dev/null | jq -r '.sessionId // empty')

APP_5_SESSION=$(jules new --repo "${REPO}" \
  "Build SecurityPulse - Code Security Scanner with Policy Enforcement. See spec in docs/superpowers/specs/cluster-a-detailed-specs.md. Use cc-starter framework. Bootstrap with tools/scaffold-app.sh. Focus on MVP: manual scan trigger, Semgrep + TruffleHog integration, policy engine, security dashboard." \
  --json 2>/dev/null | jq -r '.sessionId // empty')

echo "✅ Dispatched 5 Jules sessions"
echo ""
echo "Session IDs:"
echo "  1. IncidentBrain:  ${APP_1_SESSION}"
echo "  2. DependencyRadar: ${APP_2_SESSION}"
echo "  3. DeploySignal:   ${APP_3_SESSION}"
echo "  4. APIEvolver:     ${APP_4_SESSION}"
echo "  5. SecurityPulse:  ${APP_5_SESSION}"
echo ""

# Save session IDs for monitoring
cat > "${REPO_ROOT}/.jules-cluster-a-sessions.json" <<EOF
{
  "app1": {"id": "incidentbrain", "sessionId": "${APP_1_SESSION}"},
  "app2": {"id": "dependencyradar", "sessionId": "${APP_2_SESSION}"},
  "app3": {"id": "deploysignal", "sessionId": "${APP_3_SESSION}"},
  "app4": {"id": "apievolver", "sessionId": "${APP_4_SESSION}"},
  "app5": {"id": "securitypulse", "sessionId": "${APP_5_SESSION}"}
}
EOF

echo "📝 Sessions saved to: .jules-cluster-a-sessions.json"
echo ""
echo "⏳ Monitoring progress (check every 30 min):"
echo "  ./scripts/monitor-jules-cluster-a.sh"
echo ""
echo "🔗 Track at: https://jules.google.com"
echo ""
echo "📊 Expected completion: 4-6 hours per app (parallelized)"
echo "🎉 All PRs will appear at: https://github.com/Shashank2577/micro-saas/pulls"
