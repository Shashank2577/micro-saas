# CashflowAnalyzer Detailed Specification

## 1. Overview
**App Name:** CashflowAnalyzer
**Port:** 8206
**Theme:** Personal Finance & Wealth Management
**Purpose:** Personal cash flow analysis and optimization platform. Provides visibility into income and spending patterns, identifies optimization opportunities, and recommends actions.

## 2. Database Schema (PostgreSQL)

```sql
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    plaid_account_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    subtype VARCHAR(50),
    mask VARCHAR(4),
    current_balance DECIMAL(15, 2),
    available_balance DECIMAL(15, 2),
    iso_currency_code VARCHAR(3) DEFAULT 'USD',
    last_synced_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES accounts(id),
    plaid_transaction_id VARCHAR(255) UNIQUE NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(255) NOT NULL,
    merchant_name VARCHAR(255),
    category_id UUID,
    is_recurring BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    parent_category_id UUID REFERENCES categories(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE budgets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    category_id UUID REFERENCES categories(id) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    month INTEGER NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recurring_charges (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    merchant_name VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    last_payment_date DATE NOT NULL,
    next_payment_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cash_flow_snapshots (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    total_income DECIMAL(15, 2) NOT NULL,
    total_expenses DECIMAL(15, 2) NOT NULL,
    net_cash_flow DECIMAL(15, 2) NOT NULL,
    savings_rate DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE spending_patterns (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    category_id UUID REFERENCES categories(id) NOT NULL,
    trend_type VARCHAR(50) NOT NULL,
    description TEXT,
    percentage_change DECIMAL(5, 2),
    analysis_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    impact_amount DECIMAL(15, 2) NOT NULL,
    impact_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance and tenant scoping
CREATE INDEX idx_transactions_tenant_date ON transactions(tenant_id, date);
CREATE INDEX idx_accounts_tenant ON accounts(tenant_id);
```

## 3. REST Endpoints

### Accounts API (`/api/v1/accounts`)
- `POST /connect`: Connect Plaid account. Body: `{"public_token": "..."}`
- `DELETE /{id}`: Disconnect account.
- `POST /{id}/sync`: Trigger transaction sync.
- `GET /`: List accounts.
- `GET /{id}/balance`: Get account balance.

### Transactions API (`/api/v1/transactions`)
- `GET /`: List transactions with pagination and filters (date range, category).
- `POST /categorize`: Bulk categorize transactions (ML triggered).
- `PATCH /{id}/category`: Update category. Body: `{"category_id": "..."}`
- `GET /search`: Search by merchant/name.
- `GET /{id}`: Details.

### Analytics API (`/api/v1/analytics`)
- `GET /cash-flow`: Get statement for range.
- `GET /expense-breakdown`: Breakdown by category.
- `GET /income-summary`: Income streams.
- `GET /savings-rate`: Calculates rate over last 3 months.
- `GET /trends`: Spending trends.

### Budgeting API (`/api/v1/budgets`)
- `POST /`: Set budget. Body: `{"category_id": "...", "amount": 100, "month": 4, "year": 2026}`
- `GET /`: Get budgets.
- `GET /variance`: Compare budget to actuals.

### Optimization API (`/api/v1/optimization`)
- `GET /recurring`: Identify recurring charges.
- `GET /subscriptions`: Subscription audit.
- `GET /recommendations`: Get AI recommendations.
- `POST /recommendations/{id}/calculate`: Calculate impact modeling.

## 4. Service Methods (Backend)
- **AccountAggregationService**: `connectAccount(String token)`, `syncTransactions(UUID accountId)`.
- **TransactionCategorizationService**: `categorizeTransactions(List<Transaction> txns)` - Calls LiteLLM.
- **CashFlowAnalysisService**: `generateMonthlySnapshot(int month, int year)`, `calculateSavingsRate()`.
- **BudgetingService**: `calculateVariance(int month, int year)`.
- **SpendingPatternService**: `detectTrends(LocalDate since)`.
- **OptimizationService**: `generateRecommendations()` - Uses LiteLLM for "Eliminate $50 unused subscription...".

## 5. React Components (Frontend)
- `DashboardPage`: Overview cards (Income, Expense, Savings Rate).
- `CashFlowChart`: Recharts area/bar chart for cash flow.
- `TransactionBrowser`: React Table with sorting, filtering, editing categories.
- `BudgetVariance`: Progress bars for budget vs actual.
- `OptimizationPanel`: List of AI recommendations with action buttons.
- `SubscriptionManager`: List of recurring charges.

## 6. AI Integration (LiteLLM)
- **Categorization**: System prompt: "Categorize the following transaction into standard personal finance categories: Housing, Food, Transport, etc. Transaction: {merchant_name} {amount}".
- **Recommendations**: System prompt: "Analyze these spending patterns and suggest optimization. E.g. Identify unused subscriptions, suggest budget cuts."

## 7. Acceptance Criteria Check
1. User connects 2 bank accounts and credit card via Plaid (Account API).
2. System syncs last 90 days of transactions (500+ transactions) (Sync Service).
3. Dashboard shows total monthly income, expenses (Analytics API).
4. Expense breakdown shows percentages (Analytics API).
5. Savings rate displayed: 27% of gross income (Analytics API).
6. System identifies recurring subscription charges (Optimization API).
7. ML categorization accuracy: 95%+ with user correction feedback (Categorization Service).
8. Monthly budget shows target vs actual with variance alerts (Budget API).
9. Spending pattern analysis shows 20% increase in food spending last month (Analytics API).
10. User receives recommendation: 'Eliminate $50 unused subscription, gain $600/year' (Optimization API).
11. Cash flow projection shows conservative estimate for next 3 months (Analytics API).
12. PDF export generates professional financial statement (Analytics API - Future/Mocked).
