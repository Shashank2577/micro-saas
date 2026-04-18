CREATE TABLE surveys (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    scheduled_at TIMESTAMP,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE questions (
    id UUID PRIMARY KEY,
    survey_id UUID REFERENCES surveys(id),
    text TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    order_index INT NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE survey_responses (
    id UUID PRIMARY KEY,
    survey_id UUID REFERENCES surveys(id),
    employee_id UUID NOT NULL,
    team_id UUID NOT NULL,
    submitted_at TIMESTAMP NOT NULL,
    engagement_score DECIMAL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE answers (
    id UUID PRIMARY KEY,
    response_id UUID REFERENCES survey_responses(id),
    question_id UUID REFERENCES questions(id),
    rating_value INT,
    text_value TEXT,
    sentiment_score DECIMAL,
    sentiment_label VARCHAR(50),
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL,
    resolved BOOLEAN DEFAULT FALSE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE action_plans (
    id UUID PRIMARY KEY,
    alert_id UUID REFERENCES alerts(id),
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
