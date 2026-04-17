# WealthPlan Detailed Spec

## Overview
WealthPlan is an AI-powered financial planning and goal tracking platform. It helps users define financial goals, model scenarios, track progress, and receive personalized recommendations for wealth accumulation and retirement planning.

## Database Schema (PostgreSQL)

```sql
CREATE TABLE goals (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- RETIREMENT, HOME_PURCHASE, EDUCATION, CUSTOM
    target_amount DECIMAL(19, 2) NOT NULL,
    current_amount DECIMAL(19, 2) NOT NULL,
    target_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE net_worth_snapshots (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    snapshot_date DATE NOT NULL,
    total_assets DECIMAL(19, 2) NOT NULL,
    total_liabilities DECIMAL(19, 2) NOT NULL,
    net_worth DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE scenarios (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    goal_id UUID NOT NULL REFERENCES goals(id),
    name VARCHAR(255) NOT NULL,
    assumed_return_rate DECIMAL(5, 4) NOT NULL,
    inflation_rate DECIMAL(5, 4) NOT NULL,
    monthly_contribution DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE monte_carlo_results (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    scenario_id UUID NOT NULL REFERENCES scenarios(id),
    success_probability DECIMAL(5, 4) NOT NULL,
    median_ending_balance DECIMAL(19, 2) NOT NULL,
    worst_case_balance DECIMAL(19, 2) NOT NULL,
    best_case_balance DECIMAL(19, 2) NOT NULL,
    simulation_date TIMESTAMP NOT NULL
);

CREATE TABLE insurance_policies (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- LIFE, DISABILITY, UMBRELLA
    coverage_amount DECIMAL(19, 2) NOT NULL,
    premium DECIMAL(19, 2) NOT NULL,
    beneficiary VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE debt_items (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- MORTGAGE, STUDENT_LOAN, CREDIT_CARD, AUTO
    balance DECIMAL(19, 2) NOT NULL,
    interest_rate DECIMAL(5, 4) NOT NULL,
    minimum_payment DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE allocation_recommendations (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    risk_tolerance VARCHAR(50) NOT NULL, -- CONSERVATIVE, MODERATE, AGGRESSIVE
    stocks_percentage INT NOT NULL,
    bonds_percentage INT NOT NULL,
    cash_percentage INT NOT NULL,
    recommendation_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE estate_documents (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- WILL, TRUST, POWER_OF_ATTORNEY, HEALTHCARE_PROXY
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated_date DATE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## REST Endpoints

### Goal Management

- **POST /api/goals**
  - Create a new goal.
  - Request: `{ "name": "string", "type": "string", "targetAmount": number, "currentAmount": number, "targetDate": "string" }`
  - Response: `Goal` object.

- **GET /api/goals**
  - List all goals for the current tenant.
  - Response: `List<Goal>` object.

- **GET /api/goals/{id}**
  - Get a specific goal.
  - Response: `Goal` object.

- **PUT /api/goals/{id}**
  - Update a specific goal.
  - Request: `{ "name": "string", "type": "string", "targetAmount": number, "currentAmount": number, "targetDate": "string" }`
  - Response: `Goal` object.

- **DELETE /api/goals/{id}**
  - Delete a specific goal.

- **GET /api/goals/{id}/progress**
  - Get progress for a specific goal (percentage).
  - Response: `{ "progressPercentage": number }`

### Planning & Analysis

- **GET /api/planning/retirement-readiness**
  - Calculate retirement readiness score.
  - Request Params: `age`, `desiredIncome`, `currentSavings`, `monthlyContribution`.
  - Response: `{ "score": number, "projectedNestEgg": number, "requiredNestEgg": number }`

- **POST /api/planning/scenarios**
  - Model a new scenario.
  - Request: `{ "goalId": "UUID", "name": "string", "assumedReturnRate": number, "inflationRate": number, "monthlyContribution": number }`
  - Response: `Scenario` object.

- **POST /api/planning/monte-carlo**
  - Run Monte Carlo simulation for a scenario.
  - Request: `{ "scenarioId": "UUID" }`
  - Response: `MonteCarloResult` object.

- **GET /api/planning/savings-rate**
  - Calculate required savings rate for a goal.
  - Request Params: `goalId`.
  - Response: `{ "requiredMonthlySavings": number }`

- **GET /api/planning/insurance-gaps**
  - Analyze insurance coverage gaps.
  - Response: `List<GapAnalysis>` object.

- **GET /api/planning/asset-allocation/optimize**
  - Get optimized asset allocation.
  - Request Params: `age`, `riskTolerance`.
  - Response: `AllocationRecommendation` object.

### Recommendations (AI Integrated)

- **GET /api/recommendations/ai**
  - Get personalized AI recommendations based on user's full profile (goals, net worth, scenarios).
  - Integrates with LiteLLM.
  - Response: `{ "recommendations": ["string"] }`

- **GET /api/recommendations/allocation**
  - Get allocation recommendation based on age.
  - Request Params: `age`, `riskTolerance`.
  - Response: `AllocationRecommendation` object.

- **GET /api/recommendations/withdrawal-strategy**
  - Get tax-efficient withdrawal strategy.
  - Response: `{ "strategyDetails": "string" }`

- **GET /api/recommendations/debt-payoff**
  - Get optimized debt payoff plan (avalanche vs snowball).
  - Response: `{ "recommendedStrategy": "string", "payoffOrder": ["UUID"], "monthsToFreedom": number }`

### Documents

- **GET /api/documents/plan-pdf**
  - Generate comprehensive financial plan PDF.
  - Response: `application/pdf` binary stream.

- **GET /api/documents/estate-checklist**
  - Download estate planning checklist.
  - Response: `List<EstateDocument>` object.

- **GET /api/documents/export-goals**
  - Export all goals to CSV/JSON.
  - Response: `application/csv` or `application/json` stream.

## Services

- `GoalService`: CRUD operations for Goals, progress calculation.
- `PlanningService`: Retirement readiness, savings rate, scenario management.
- `MonteCarloService`: Executes Monte Carlo simulations using Apache Commons Math.
- `InsuranceService`: Gap analysis.
- `AssetAllocationService`: Calculates optimal allocations.
- `RecommendationService`: Interacts with LiteLLM to generate personalized advice.
- `DocumentService`: Generates PDFs using iTextPDF, exports CSV.

## React Components (Next.js)

- `Dashboard`: Main overview showing net worth, top goals, and progress.
- `GoalList`: Displays all goals.
- `GoalForm`: React Hook Form for creating/editing goals.
- `ScenarioModeler`: UI for configuring scenarios and running simulations.
- `SimulationChart`: Recharts component for visualizing Monte Carlo results.
- `DebtPayoffPlanner`: UI for entering debts and seeing payoff strategies.
- `InsuranceGapAnalyzer`: UI for entering policies and viewing gaps.
- `AIRecommendationPanel`: Displays AI-generated advice.
- `EstatePlanningChecklist`: Checklist UI for estate documents.

## AI Integration

- `RecommendationService` will construct a prompt containing a summary of the user's goals, net worth, and risk tolerance, and send it to the local LiteLLM instance (http://localhost:4000) using the `gemma2:9b` model (or similar configured local model) to generate personalized financial advice.

## Acceptance Criteria
- User creates retirement goal with age 65, current age 35, desired income $100k.
- System calculates required nest egg using 4% rule and Monte Carlo analysis.
- Savings rate calculator shows monthly target to achieve goal.
- User can model 3 scenarios: conservative, moderate, aggressive market returns.
- Asset allocation recommends 70/30 stocks/bonds based on 30-year horizon.
- Insurance gap analysis identifies underinsured areas.
- Debt payoff calculator shows optimal payoff order and timeline.
- Goal dashboard shows progress toward each milestone.
- AI recommends specific funds or investments to improve allocation.
- User can generate comprehensive financial plan PDF.
- Multi-scenario modeling shows outcomes in market crash scenario.
- Estate planning checklist tracks completion of documents.
