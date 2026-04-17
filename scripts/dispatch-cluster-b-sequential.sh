#!/usr/bin/env bash
set -euo pipefail

# dispatch-cluster-b-sequential.sh — Dispatch Cluster B apps to Jules ONE AT A TIME
# Usage: ./scripts/dispatch-cluster-b-sequential.sh
# This respects Jules capacity of 15 concurrent by sending apps sequentially with 20s delay between each

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO="Shashank2577/micro-saas"
BRANCH="main"
TRACKER="${REPO_ROOT}/.jules-cluster-b-sessions.json"
LOG_FILE="${REPO_ROOT}/.jules-cluster-b-dispatch.log"

# Check Jules auth
echo "🚀 Jules Sequential Orchestrator for Cluster B (9 apps, one by one)"
echo "📋 Repo: ${REPO}"
echo "📦 Branch: ${BRANCH}"
echo ""

if ! jules remote list --repo &>/dev/null; then
  echo "❌ Jules not authenticated. Run: jules login"
  exit 1
fi
echo "✅ Jules CLI authenticated"
echo ""

: > "$LOG_FILE"

dispatch_one() {
  local app="$1"
  local display="$2"
  local port="$3"
  local prompt="$4"

  echo "🛰  Dispatching ${display} (${app})..."

  local out
  out=$(jules remote new --repo "$REPO" --session "$prompt" 2>&1) || {
    echo "  ❌ Failed. Output (tail):"
    echo "$out" | tail -n 10
    echo "${app}=FAILED" >> "$LOG_FILE"
    return 1
  }

  # Extract session ID (15-20 digit numeric)
  local sid
  sid=$(echo "$out" | grep -Eo '[0-9]{15,}' | head -n1 || true)

  if [[ -z "$sid" ]]; then
    echo "  ⚠️  Dispatched but no session ID parsed. Output:"
    echo "$out" | tail -n 3
    sid="UNPARSED"
  else
    echo "  ✅ session=${sid}"
  fi

  echo "${app}=${sid}" >> "$LOG_FILE"

  # Update tracker
  if [[ -f "$TRACKER" && "$sid" != "UNPARSED" && -n "$sid" ]]; then
    python3 - "$TRACKER" "$app" "$sid" <<'PY' 2>/dev/null || true
import json, sys
path, app_id, sid = sys.argv[1], sys.argv[2], sys.argv[3]
with open(path) as f:
    data = json.load(f)
for key, obj in (data.get("apps") or {}).items():
    if obj.get("id") == app_id:
        obj["sessionId"] = sid
        obj["status"] = "running"
        break
with open(path, "w") as f:
    json.dump(data, f, indent=2)
PY
  fi

  # 20 second delay between dispatches
  sleep 20
}

# ────────── B1 InvestTracker ──────────
dispatch_one "investtracker" "InvestTracker" "8201" \
"Build InvestTracker - comprehensive investment portfolio tracking platform. Multi-brokerage support (Alpaca, IB, Fidelity, Coinbase). Real-time portfolio sync, asset allocation, performance analytics (YTD/1Y/3Y/5Y), cost basis tracking, AI-powered risk assessment, rebalancing recommendations, tax reporting, mobile dashboard, multi-portfolio support.

Use cc-starter 6-layer (entities → service → analytics → REST API → Next.js). Spring Boot 3.3.5 backend, PostgreSQL, Next.js 14 frontend. LiteLLM for AI insights. pgmq for async sync, Redis for caching. OAuth for broker auth. Full REST API, OpenAPI docs, tests, Dockerfile. 18 hours."

# ────────── B2 WealthPlan ──────────
dispatch_one "wealthplan" "WealthPlan" "8202" \
"Build WealthPlan - comprehensive wealth planning platform. Retirement income adequacy, Social Security optimization, drawdown strategies, longevity risk. Life expectancy estimation, pension value assessment, RMD calculations, withdrawal sequencing, Roth conversion analysis, qualified charitable distribution planning, healthcare cost projection, stress testing, annuity analysis.

Use cc-starter 6-layer. Spring Boot 3.3.5, PostgreSQL, Next.js 14. LiteLLM for recommendations. pgmq for async calculations. Full API, tests, OpenAPI. 20 hours."

# ────────── B3 TaxOptimizer ──────────
dispatch_one "taxoptimizer" "TaxOptimizer" "8203" \
"Build TaxOptimizer - tax optimization platform. Tax planning, entity structure analysis, trust planning, SALT strategies, charitable giving optimization, tax-loss harvesting identification, estimated quarterly tax planning, multi-state tax analysis, retirement tax strategies.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM for tax recommendations. Full API, tax calculation engine, scenario modeling. 19 hours."

# ────────── B4 DebtNavigator ──────────
dispatch_one "debtnavigator" "DebtNavigator" "8204" \
"Build DebtNavigator - debt management and payoff platform. Debt portfolio tracking, payoff strategy optimization (avalanche, snowball), credit score monitoring, interest savings calculation, refinancing opportunities, consolidation analysis, credit card balance transfer finder.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM for payoff strategies. Full API, scenario modeling. 17 hours."

# ────────── B5 GoalTracker ──────────
dispatch_one "goaltracker" "GoalTracker" "8205" \
"Build GoalTracker - financial goals tracking. Goal definition (house, education, retirement), target date & amount, savings rate calculator, milestone tracking, progress visualization, AI-powered adjustment suggestions, multi-goal prioritization, scenario impact analysis.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM for goal strategies. Full API, charts, progress tracking. 16 hours."

# ────────── B6 CashflowAnalyzer ──────────
dispatch_one "cashflowanalyzer" "CashflowAnalyzer" "8206" \
"Build CashflowAnalyzer - personal cash flow analysis. Income/expense tracking, cash flow forecasting, seasonal pattern detection, budget management, spending category analysis, AI recommendations for optimization, alert rules, multi-account aggregation.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM for recommendations. pgmq for async analysis. Full API, forecasting engine. 16 hours."

# ────────── B7 RetirementPlus ──────────
dispatch_one "retirementplus" "RetirementPlus" "8207" \
"Build RetirementPlus - retirement planning. Life expectancy estimation, Social Security benefit optimization, withdrawal sequencing, tax-efficient withdrawal strategy, healthcare cost projection, RMD calculations, longevity risk modeling, stress testing, annuity analysis, legacy planning.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM. Full calculation engine, scenario modeling, reports. 18 hours."

# ────────── B8 WealthEdge ──────────
dispatch_one "wealthedge" "WealthEdge" "8208" \
"Build WealthEdge - high-net-worth wealth management. Net worth tracking (traditional + alternative assets), real estate portfolio, alternative investments, tax planning, charitable giving strategies, family wealth coordination, trust/estate planning, succession planning, business interests valuation, risk concentration analysis, benchmarking, advisor coordination workspace.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM. Encryption for valuations. Full portfolio management. 20 hours."

# ────────── B9 BudgetMaster ──────────
dispatch_one "budgetmaster" "BudgetMaster" "8209" \
"Build BudgetMaster - personal budgeting platform. Budget template creation, category-based tracking, variance analysis, AI-powered recommendations, alerts for overspending, rolling budget periods, goal vs actual comparison, forecast vs actual, multi-currency support.

Use cc-starter 6-layer. Spring Boot, PostgreSQL, Next.js. LiteLLM for recommendations. Full API, budget engine. 15 hours."

echo ""
echo "✅ Cluster B dispatched sequentially. Session IDs logged to: ${LOG_FILE}"
echo ""
echo "📊 Monitor progress:"
echo "  julius remote list --session"
echo ""
echo "🔗 Track at: https://jules.google.com"
echo ""
echo "📦 Expected completion: 18-20h per app (sequential)"
echo "🎉 PRs will appear at: https://github.com/Shashank2577/micro-saas/pulls"
