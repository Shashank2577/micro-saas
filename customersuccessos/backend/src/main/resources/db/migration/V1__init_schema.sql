CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    subscription_tier VARCHAR(100),
    csm_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_accounts_tenant_id ON accounts(tenant_id);

CREATE TABLE health_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID NOT NULL,
    score INTEGER NOT NULL,
    factors TEXT,
    recorded_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_health_score_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_health_scores_tenant_id ON health_scores(tenant_id);
CREATE INDEX idx_health_scores_account_id ON health_scores(account_id);

CREATE TABLE expansion_opportunities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID NOT NULL,
    type VARCHAR(100) NOT NULL,
    description TEXT,
    estimated_value DECIMAL(19, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_expansion_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_expansion_tenant_id ON expansion_opportunities(tenant_id);
CREATE INDEX idx_expansion_account_id ON expansion_opportunities(account_id);

CREATE TABLE qbr_decks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID NOT NULL,
    content TEXT,
    status VARCHAR(50) NOT NULL,
    generated_at TIMESTAMP,
    CONSTRAINT fk_qbr_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_qbr_decks_tenant_id ON qbr_decks(tenant_id);
CREATE INDEX idx_qbr_decks_account_id ON qbr_decks(account_id);
