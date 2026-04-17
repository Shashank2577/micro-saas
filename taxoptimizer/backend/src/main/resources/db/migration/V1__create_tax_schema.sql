-- Create tax_profiles
CREATE TABLE tax_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    filing_status VARCHAR(50),
    dependents_count INTEGER DEFAULT 0,
    state_residence VARCHAR(50),
    business_type VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_tax_profiles_tenant_user ON tax_profiles(tenant_id, user_id);

-- Create income_sources
CREATE TABLE income_sources (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    source_type VARCHAR(50) NOT NULL, -- W2, 1099, CAPITAL_GAINS
    amount DECIMAL(19, 4) NOT NULL,
    description VARCHAR(255),
    employer_name VARCHAR(255),
    tax_year INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_income_sources_profile ON income_sources(profile_id);

-- Create expenses
CREATE TABLE expenses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    category VARCHAR(100) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    description VARCHAR(255),
    is_business_expense BOOLEAN DEFAULT FALSE,
    tax_year INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_expenses_profile ON expenses(profile_id);

-- Create capital_transactions
CREATE TABLE capital_transactions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    asset_name VARCHAR(100) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- BUY, SELL
    quantity DECIMAL(19, 4) NOT NULL,
    price_per_share DECIMAL(19, 4) NOT NULL,
    transaction_date DATE NOT NULL,
    tax_year INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_capital_transactions_profile ON capital_transactions(profile_id);

-- Create tax_scenarios
CREATE TABLE tax_scenarios (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    scenario_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create tax_calculations
CREATE TABLE tax_calculations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    tax_year INTEGER NOT NULL,
    calculation_type VARCHAR(50) NOT NULL, -- QUARTERLY, ANNUAL_ESTIMATE
    federal_liability DECIMAL(19, 4) NOT NULL,
    state_liability DECIMAL(19, 4) NOT NULL,
    total_liability DECIMAL(19, 4) NOT NULL,
    calculation_details JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create accountant_collaborations
CREATE TABLE accountant_collaborations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    accountant_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, ACTIVE, ARCHIVED
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create compliance_checklist
CREATE TABLE compliance_checklist (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES tax_profiles(id),
    jurisdiction VARCHAR(50) NOT NULL,
    task_name VARCHAR(255) NOT NULL,
    deadline DATE NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
