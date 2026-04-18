CREATE TABLE roles (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    level INTEGER,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE skills (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE role_skills (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    role_id UUID NOT NULL REFERENCES roles(id),
    skill_id UUID NOT NULL REFERENCES skills(id),
    required_proficiency INTEGER NOT NULL,
    is_core BOOLEAN DEFAULT TRUE
);

CREATE TABLE career_paths (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    from_role_id UUID NOT NULL REFERENCES roles(id),
    to_role_id UUID NOT NULL REFERENCES roles(id),
    description TEXT
);

CREATE TABLE employees (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    current_role_id UUID REFERENCES roles(id),
    manager_id UUID REFERENCES employees(id),
    career_goals TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE employee_skills (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    skill_id UUID NOT NULL REFERENCES skills(id),
    current_proficiency INTEGER NOT NULL
);

CREATE TABLE mentorship_requests (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    mentee_id UUID NOT NULL REFERENCES employees(id),
    mentor_id UUID NOT NULL REFERENCES employees(id),
    status VARCHAR(50) NOT NULL,
    goals TEXT
);

CREATE TABLE development_plans (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    employee_id UUID NOT NULL REFERENCES employees(id),
    target_role_id UUID REFERENCES roles(id),
    status VARCHAR(50) NOT NULL,
    milestones_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE
);
