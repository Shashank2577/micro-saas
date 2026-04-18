CREATE TABLE menu_item (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    units_sold INT NOT NULL,
    profit_margin DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_menu_item_tenant_id ON menu_item(tenant_id);

CREATE TABLE ingredient (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    current_stock DECIMAL(10, 2) NOT NULL,
    par_level DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_ingredient_tenant_id ON ingredient(tenant_id);

CREATE TABLE predictive_order (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    ingredient_id UUID NOT NULL REFERENCES ingredient(id),
    predicted_demand DECIMAL(10, 2) NOT NULL,
    recommended_order_amount DECIMAL(10, 2) NOT NULL,
    order_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    ai_confidence_score DECIMAL(5, 2),
    ai_rationale TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_predictive_order_tenant_id ON predictive_order(tenant_id);

CREATE TABLE staff_schedule (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    role VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    shift_start TIME NOT NULL,
    shift_end TIME NOT NULL,
    predicted_busyness VARCHAR(50) NOT NULL,
    recommended_staff_count INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_staff_schedule_tenant_id ON staff_schedule(tenant_id);

CREATE TABLE customer_review (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    content TEXT NOT NULL,
    review_date DATE NOT NULL,
    sentiment VARCHAR(50),
    operational_insight TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_customer_review_tenant_id ON customer_review(tenant_id);
