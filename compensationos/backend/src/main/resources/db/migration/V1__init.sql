CREATE TABLE market_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    role VARCHAR(255) NOT NULL,
    level VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    p25_salary DECIMAL(10, 2),
    p50_salary DECIMAL(10, 2),
    p75_salary DECIMAL(10, 2),
    p90_salary DECIMAL(10, 2),
    data_source VARCHAR(100),
    effective_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_market_data_tenant_role ON market_data(tenant_id, role);

CREATE TABLE employee_compensation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(255) NOT NULL,
    level VARCHAR(50) NOT NULL,
    department VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    base_salary DECIMAL(10, 2) NOT NULL,
    bonus_target_percent DECIMAL(5, 2),
    equity_grant_value DECIMAL(10, 2),
    currency VARCHAR(3) DEFAULT 'USD',
    gender VARCHAR(50),
    ethnicity VARCHAR(100),
    hire_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employee_comp_tenant ON employee_compensation(tenant_id);

CREATE TABLE compensation_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    change_date DATE NOT NULL,
    previous_base DECIMAL(10, 2),
    new_base DECIMAL(10, 2),
    reason VARCHAR(255),
    approved_by UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comp_hist_tenant_emp ON compensation_history(tenant_id, employee_id);

CREATE TABLE equity_models (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    total_shares BIGINT,
    available_shares BIGINT,
    current_valuation DECIMAL(10, 2),
    vesting_schedule_months INT DEFAULT 48,
    cliff_months INT DEFAULT 12,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compensation_cycles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    cycle_name VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date DATE,
    total_budget DECIMAL(15, 2),
    status VARCHAR(50) DEFAULT 'DRAFT', -- DRAFT, ACTIVE, COMPLETED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE department_budgets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    fiscal_year VARCHAR(10) NOT NULL,
    total_budget DECIMAL(15, 2) NOT NULL,
    allocated_budget DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE peer_companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    industry VARCHAR(100),
    employee_count INT,
    annual_revenue DECIMAL(15, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE benefit_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    plan_type VARCHAR(100),
    monthly_cost DECIMAL(10, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
