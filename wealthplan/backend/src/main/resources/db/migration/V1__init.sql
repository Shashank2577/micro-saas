CREATE TABLE goals (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
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
    type VARCHAR(50) NOT NULL,
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
    type VARCHAR(50) NOT NULL,
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
    risk_tolerance VARCHAR(50) NOT NULL,
    stocks_percentage INT NOT NULL,
    bonds_percentage INT NOT NULL,
    cash_percentage INT NOT NULL,
    recommendation_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE estate_documents (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated_date DATE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
