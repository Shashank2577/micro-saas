CREATE TABLE employee_comp (
    id UUID PRIMARY KEY,
    employee_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    level VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    base_salary NUMERIC(15,2) NOT NULL,
    total_comp NUMERIC(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    effective_date DATE NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE market_benchmarks (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    level VARCHAR(255) NOT NULL,
    p25 NUMERIC(15,2) NOT NULL,
    p50 NUMERIC(15,2) NOT NULL,
    p75 NUMERIC(15,2) NOT NULL,
    p90 NUMERIC(15,2) NOT NULL,
    source VARCHAR(255),
    benchmarked_at DATE NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE comp_gaps (
    id UUID PRIMARY KEY,
    employee_id VARCHAR(255) NOT NULL,
    gap_amount NUMERIC(15,2) NOT NULL,
    gap_pct NUMERIC(5,2) NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    recommended_range_min NUMERIC(15,2) NOT NULL,
    recommended_range_max NUMERIC(15,2) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE pay_equity_audits (
    id UUID PRIMARY KEY,
    group_dimension VARCHAR(50) NOT NULL,
    avg_gap_pct NUMERIC(5,2) NOT NULL,
    flagged_count INT NOT NULL,
    audit_date DATE NOT NULL,
    tenant_id UUID NOT NULL
);
