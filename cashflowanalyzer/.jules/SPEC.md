# CashflowAnalyzer - Detailed Specification

## Application Overview
CashflowAnalyzer is a personal cash flow analysis and optimization platform. It provides deep visibility into income and spending patterns, identifies optimization opportunities, and recommends actions to improve financial health.

## Core Features
- **Bank Aggregation:** Connect accounts via Plaid API.
- **Transaction Sync:** Pull last 90 days initially, daily afterward. Max 5000 transactions/month/user.
- **Categorization:** ML-based learning using LiteLLM (TensorFlow/PyTorch referenced, but LiteLLM specified for AI integration).
- **Cash Flow Analytics:** Statement generation, expense breakdown, fixed vs variable.
- **Budgeting:** Comparison of actual vs budget using 12-month rolling average for trends.
- **Optimization:** Recurring charge identification, subscription audit, recommendations with impact modeling.

## Technical Architecture
- **Backend:** Spring Boot 3.3.5 (Java 21), PostgreSQL, cc-starter, LiteLLM.
- **Frontend:** Next.js (App Router), TypeScript, Tailwind, Recharts, React Query, React Table.
- **Infrastructure:** pgmq (async categorization), Redis (caching).

## Database Schema (PostgreSQL)

### `accounts`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `plaid_item_id` (String)
- `plaid_account_id` (String)
- `name` (String)
- `mask` (String)
- `type` (String)
- `subtype` (String)
- `balance_current` (Decimal)
- `balance_available` (Decimal)
- `iso_currency_code` (String)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

### `transactions`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `account_id` (UUID, FK to accounts)
- `plaid_transaction_id` (String)
- `amount` (Decimal)
- `date` (Date)
- `name` (String)
- `merchant_name` (String)
- `category_id` (UUID, FK to categories)
- `pending` (Boolean)
- `is_recurring` (Boolean)
- `created_at` (Timestamp)

### `categories`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `name` (String)
- `type` (String: INCOME, EXPENSE, TRANSFER)
- `is_fixed` (Boolean)

### `budgets`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `category_id` (UUID, FK to categories)
- `amount` (Decimal)
- `month` (Date)
- `created_at` (Timestamp)

### `recurring_charges`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `merchant_name` (String)
- `amount` (Decimal)
- `frequency` (String)
- `category_id` (UUID, FK to categories)
- `is_active` (Boolean)

### `cash_flow_snapshots`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `month` (Date)
- `total_income` (Decimal)
- `total_expenses` (Decimal)
- `savings_rate` (Decimal)
- `created_at` (Timestamp)

### `spending_patterns`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `category_id` (UUID, FK to categories)
- `trend_percentage` (Decimal)
- `analysis_period` (String)

### `recommendations`
- `id` (UUID, PK)
- `tenant_id` (String, indexed)
- `type` (String)
- `description` (String)
- `potential_savings` (Decimal)
- `is_implemented` (Boolean)
- `created_at` (Timestamp)

## API Endpoints

### Account Management
- `POST /api/accounts/connect` - Connect Plaid account
- `POST /api/accounts/disconnect` - Remove account
- `POST /api/accounts/sync` - Trigger transaction sync
- `GET /api/accounts` - List accounts
- `GET /api/accounts/{id}/balance` - Get specific account balance

### Transactions
- `GET /api/transactions` - List transactions (paginated, filtered)
- `POST /api/transactions/{id}/categorize` - Trigger ML categorization
- `PUT /api/transactions/{id}/category` - Manually update category
- `GET /api/transactions/search` - Search by text/merchant
- `GET /api/transactions/{id}` - Get transaction details

### Analytics
- `GET /api/analytics/cash-flow` - Get cash flow statement
- `GET /api/analytics/expenses` - Get expense breakdown
- `GET /api/analytics/income` - Get income summary
- `GET /api/analytics/savings-rate` - Get savings rate
- `GET /api/analytics/trends` - Get spending trends

### Budgeting
- `POST /api/budgets` - Set budget for category
- `GET /api/budgets` - Get current budgets
- `GET /api/budgets/compare` - Compare actual vs budget
- `GET /api/budgets/variance` - Get budget variance alerts

### Optimization
- `GET /api/optimization/recurring` - Find recurring charges
- `GET /api/optimization/subscriptions` - Analyze subscriptions
- `GET /api/optimization/recommendations` - Get recommendations
- `POST /api/optimization/impact` - Calculate recommendation impact

## Services
- `AccountAggregationService`: Plaid API integration (mocked for now).
- `TransactionSyncService`: Fetches transactions, applies 90-day/daily rules.
- `TransactionCategorizationService`: Uses LiteLLM to categorize transactions asynchronously via pgmq.
- `CashFlowAnalysisService`: Calculates aggregates, savings rates (3-month avg).
- `BudgetingService`: Manages budgets, calculates variances (12-month rolling avg for trends).
- `SpendingPatternService`: Identifies trends and anomalies.
- `OptimizationService`: Generates savings recommendations.

## Next.js Frontend
- **Pages**: `/dashboard`, `/transactions`, `/analytics`, `/budget`, `/optimization`.
- **Components**: `DashboardMetrics`, `TransactionTable`, `CashFlowChart` (Recharts), `ExpenseBreakdownPie`, `BudgetComparison`, `RecommendationList`.
- **State Management**: React Query for API data fetching.
