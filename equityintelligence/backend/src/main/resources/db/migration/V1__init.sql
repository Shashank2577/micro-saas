CREATE SCHEMA IF NOT EXISTS equityintelligence;

CREATE TABLE equityintelligence.stakeholder (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE equityintelligence.equity_grant (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    stakeholder_id UUID NOT NULL REFERENCES equityintelligence.stakeholder(id),
    grant_type VARCHAR(50) NOT NULL,
    shares BIGINT NOT NULL,
    grant_date DATE NOT NULL,
    cliff_months INTEGER NOT NULL,
    vest_months INTEGER NOT NULL,
    strike_price DECIMAL(19, 4)
);

CREATE TABLE equityintelligence.vesting_event (
    id UUID PRIMARY KEY,
    grant_id UUID NOT NULL REFERENCES equityintelligence.equity_grant(id),
    tenant_id UUID NOT NULL,
    vest_date DATE NOT NULL,
    shares_vested BIGINT NOT NULL,
    cumulative_vested BIGINT NOT NULL
);

CREATE TABLE equityintelligence.funding_round (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    round_name VARCHAR(255) NOT NULL,
    pre_money_valuation DECIMAL(19, 4) NOT NULL,
    amount_raised DECIMAL(19, 4) NOT NULL,
    share_price DECIMAL(19, 4) NOT NULL,
    closed_at TIMESTAMP
);

CREATE TABLE equityintelligence.dilution_scenario (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    scenario_data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);
