# BudgetMaster Detailed Spec

## 1. Overview
BudgetMaster is a personal budgeting and expense control platform built on the micro-saas ecosystem. It focuses on zero-based budgeting, real-time tracking, alerts, and AI-driven optimization recommendations.

## 2. Database Schema (PostgreSQL)
All tables must include: `id` (UUID, primary key), `tenant_id` (VARCHAR), `created_at` (TIMESTAMP), `updated_at` (TIMESTAMP).

*   **`bm_budgets`**
    *   `id` (UUID), `tenant_id` (VARCHAR), `name` (VARCHAR), `monthly_income` (DECIMAL), `type` (VARCHAR: FIXED, INCOME_BASED), `rolling_mode` (BOOLEAN), `month` (INTEGER), `year` (INTEGER)
*   **`bm_categories`**
    *   `id` (UUID), `tenant_id` (VARCHAR), `budget_id` (UUID, FK), `name` (VARCHAR), `allocated_amount` (DECIMAL), `type` (VARCHAR: EXPENSE, SAVINGS, IRREGULAR), `carryover` (BOOLEAN)
*   **`bm_transactions`**
    *   `id` (UUID), `tenant_id` (VARCHAR), `budget_id` (UUID, FK), `category_id` (UUID, FK, nullable), `amount` (DECIMAL), `date` (DATE), `description` (VARCHAR), `status` (VARCHAR: PENDING, CLEARED)
*   **`bm_alerts`**
    *   `id` (UUID), `tenant_id` (VARCHAR), `category_id` (UUID, FK), `threshold_percent` (DECIMAL), `threshold_amount` (DECIMAL), `triggered` (BOOLEAN)
*   **`bm_targets`** (Savings/Emergency Fund Goals)
    *   `id` (UUID), `tenant_id` (VARCHAR), `name` (VARCHAR), `target_amount` (DECIMAL), `current_amount` (DECIMAL), `deadline` (DATE)
*   **`bm_family_members`** (Family Budget Collaboration)
    *   `id` (UUID), `tenant_id` (VARCHAR), `name` (VARCHAR), `role` (VARCHAR: OWNER, SPOUSE, CHILD), `individual_allowance` (DECIMAL)
*   **`bm_irregular_expenses`**
    *   `id` (UUID), `tenant_id` (VARCHAR), `name` (VARCHAR), `amount` (DECIMAL), `due_date` (DATE), `frequency` (VARCHAR: ANNUAL, QUARTERLY)

## 3. REST APIs

**Budgets**
*   `POST /api/budgets` - Create a budget.
    *   Req: `name`, `monthlyIncome`, `type`, `rollingMode`, `month`, `year`.
*   `GET /api/budgets` - Get budgets for tenant.
*   `GET /api/budgets/{id}` - Get budget details (includes categories and spending summary).
*   `GET /api/budgets/templates` - Get budget templates (minimalist, comfort, luxury).

**Categories**
*   `POST /api/budgets/{budgetId}/categories` - Add category.
    *   Req: `name`, `allocatedAmount`, `type`, `carryover`.
*   `PUT /api/budgets/{budgetId}/categories/{id}` - Update category.

**Transactions**
*   `POST /api/transactions` - Record a transaction.
    *   Req: `budgetId`, `categoryId` (optional), `amount`, `date`, `description`.
*   `GET /api/transactions` - List transactions (filters by budget, category).

**Alerts**
*   `POST /api/alerts` - Configure spending alert for a category.
    *   Req: `categoryId`, `thresholdPercent`, `thresholdAmount`.
*   `GET /api/alerts` - List alerts.

**Optimization & AI**
*   `GET /api/optimization/recommendations/{budgetId}` - Get AI suggestions to optimize budget (calls LiteLLM).
*   `POST /api/transactions/categorize` - Suggest category for an uncategorized transaction (calls LiteLLM).

**Targets & Family**
*   `POST /api/targets` - Create a savings target.
*   `POST /api/family-members` - Add a family member.
*   `POST /api/irregular-expenses` - Plan irregular expense.

## 4. AI Integrations
*   **LiteLLM**: Used for budget optimization and category prediction.
    *   Prompt (Optimization): "Given this budget allocation and recent spending, suggest ways to increase savings rate."
    *   Prompt (Categorization): "Given a transaction description, suggest the best budget category."

## 5. React Components (Next.js)
*   **`DashboardPage`**: Main view. Shows real-time budget vs actual, 12-month rolling forecast chart, and alerts.
*   **`BudgetSetupPage`**: Form to create zero-based budget. Allows template selection.
*   **`CategoryManager`**: Component to allocate funds (Envelope system visualization).
*   **`TransactionList`**: Shows transactions, highlights uncategorized ones for review checklist.
*   **`OptimizationPanel`**: Displays AI recommendations.
*   **`TargetTracker`**: Visualizes progress toward savings/emergency goals.
*   **`FamilyCollaborationView`**: Shows shared budget and individual allowances.
*   **`IrregularExpensePlanner`**: Calendar-like view of upcoming irregular expenses.

## 6. Integration & Async
*   **Pgmq / Spring Events**: Alert evaluation happens asynchronously when a transaction is recorded. If a category's spending exceeds its alert threshold, an event is emitted.
*   **Redis**: Cache budget summaries and template data to improve performance.

## 7. Error Handling
*   `404 Not Found` if accessing a resource not belonging to `tenant_id`.
*   `400 Bad Request` if budget allocation exceeds monthly income (for strict zero-based budgets).
*   Validation errors returned in standard problem-detail format.
