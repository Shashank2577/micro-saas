CREATE SCHEMA IF NOT EXISTS performancenarrative;

CREATE TABLE performancenarrative.review_cycle (
    id UUID PRIMARY KEY,
    cycle_name VARCHAR(255) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE performancenarrative.employee_review (
    id UUID PRIMARY KEY,
    cycle_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    overall_rating VARCHAR(50),
    draft_narrative TEXT,
    final_narrative TEXT,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE performancenarrative.peer_feedback (
    id UUID PRIMARY KEY,
    review_id UUID NOT NULL,
    from_employee_id UUID NOT NULL,
    feedback_text TEXT NOT NULL,
    rating INTEGER NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE performancenarrative.development_goal (
    id UUID PRIMARY KEY,
    review_id UUID NOT NULL,
    goal_description TEXT NOT NULL,
    target_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);
