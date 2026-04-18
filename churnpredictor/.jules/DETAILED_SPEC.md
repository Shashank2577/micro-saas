# Detailed Specification: ChurnPredictor

## 1. Overview
ChurnPredictor is an AI churn prevention platform that predicts customer churn 30-90 days in advance based on usage, support sentiment, engagement trends, NPS responses, and health scores. It also triggers automated intervention playbooks.

## 2. Database Schema (PostgreSQL 16)
All tables use `tenant_id` for multitenancy.

```sql
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    mrr DECIMAL(10, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE customer_health_scores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    composite_score DECIMAL(5,2),
    usage_score DECIMAL(5,2),
    support_score DECIMAL(5,2),
    engagement_score DECIMAL(5,2),
    nps_score DECIMAL(5,2),
    calculated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE churn_predictions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    risk_segment VARCHAR(50), -- HIGH, MEDIUM, LOW
    probability_30_days DECIMAL(5,2),
    probability_60_days DECIMAL(5,2),
    probability_90_days DECIMAL(5,2),
    predicted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE intervention_playbooks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    trigger_risk_segment VARCHAR(50),
    action_type VARCHAR(100), -- EMAIL, SLACK_ALERT, CRM_TASK, OFFER
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE interventions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    playbook_id UUID REFERENCES intervention_playbooks(id),
    status VARCHAR(50), -- PENDING, COMPLETED, FAILED
    offer_details TEXT,
    effectiveness_status VARCHAR(50), -- PENDING, PREVENTED_CHURN, CHURNED
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_customers_tenant ON customers(tenant_id);
CREATE INDEX idx_customer_health_scores_tenant ON customer_health_scores(tenant_id);
CREATE INDEX idx_churn_predictions_tenant ON churn_predictions(tenant_id);
CREATE INDEX idx_intervention_playbooks_tenant ON intervention_playbooks(tenant_id);
CREATE INDEX idx_interventions_tenant ON interventions(tenant_id);
```

## 3. REST API Endpoints

### 3.1 Customers
- `GET /api/v1/customers`: List customers.
- `GET /api/v1/customers/{id}`: Get customer details with latest health score and prediction.
- `POST /api/v1/customers`: Create customer.

### 3.2 Health Scores
- `GET /api/v1/customers/{id}/health`: Get health score history for a customer.
- `POST /api/v1/customers/{id}/health/recalculate`: Trigger manual recalculation.

### 3.3 Churn Predictions
- `GET /api/v1/predictions`: List latest predictions for all customers (dashboard).
- `GET /api/v1/customers/{id}/predictions`: Get prediction history.
- `POST /api/v1/predictions/recalculate`: Batch recalculate predictions.

### 3.4 Playbooks & Interventions
- `GET /api/v1/playbooks`: List playbooks.
- `POST /api/v1/playbooks`: Create playbook.
- `GET /api/v1/interventions`: List interventions.
- `POST /api/v1/interventions/{id}/generate-offer`: Use AI to generate a retention offer.

## 4. Services

- `CustomerService`: CRUD for customers.
- `HealthScoreService`: Calculates the health score composite.
- `PredictionService`: Mocks a Python/sklearn model predicting churn probabilities based on mock historical data. 
- `InterventionService`: Evaluates rules and triggers playbooks based on risk transitions.
- `AiOfferService`: Uses LiteLLM to generate personalized retention emails/offers.

## 5. React Components

- `Dashboard`: Shows average health score, churn risk distribution, and upcoming interventions (Recharts).
- `CustomerList`: React Table showing customers, health scores, and risk segments.
- `CustomerDetail`: Drill-down view showing health score trends (Recharts) and prediction history.
- `PlaybookList`: View active playbooks.
- `InterventionHistory`: Table of triggered interventions and their status.

## 6. AI Integration
- Endpoint: `POST /api/v1/interventions/{id}/generate-offer`
- Prompt: "Generate a personalized retention email for customer {name} in {industry}. Their health score is {score} due to low {factor}. Offer them a {action_type}."

## 7. Acceptance Criteria Check
- Health score calculates correctly.
- Interventions generated properly.
- React components render Recharts.
- Multi-tenancy via cc-starter `TenantContext.require()` works.
