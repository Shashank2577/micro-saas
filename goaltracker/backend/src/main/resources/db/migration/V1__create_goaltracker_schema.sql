CREATE TABLE goals (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    target_amount DECIMAL(19, 4) NOT NULL,
    current_amount DECIMAL(19, 4) NOT NULL DEFAULT 0,
    deadline TIMESTAMP NOT NULL,
    priority INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE milestones (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    percentage DECIMAL(5, 2) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    achieved BOOLEAN NOT NULL DEFAULT FALSE,
    achieved_at TIMESTAMP
);

CREATE TABLE contributions (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    amount DECIMAL(19, 4) NOT NULL,
    source_account_id VARCHAR(255),
    destination_account_id VARCHAR(255),
    contribution_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE automated_transfers (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    amount DECIMAL(19, 4) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    source_account_id VARCHAR(255),
    destination_account_id VARCHAR(255),
    next_run_date TIMESTAMP,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE transfer_history (
    id UUID PRIMARY KEY,
    transfer_id UUID NOT NULL REFERENCES automated_transfers(id),
    amount DECIMAL(19, 4) NOT NULL,
    transfer_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE goal_sharing (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    shared_with_email VARCHAR(255) NOT NULL,
    permissions VARCHAR(50) NOT NULL
);

CREATE TABLE motivation_history (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    nudge_message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
