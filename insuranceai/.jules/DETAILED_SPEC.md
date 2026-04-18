# Detailed Spec: InsuranceAI

## Overview
**Vertical:** Insurtech / Insurance operations
**Problem:** Claims processing is manual, slow, and expensive. Fraud is detected after payment. Underwriting relies on actuarial tables rather than behavioral signals.
**Solution:** AI insurance operations platform. Claims processing automation with triage and extraction. Fraud detection using behavioral anomaly patterns. Risk scoring for underwriting. Policy document extraction and comparison.
**AI Pattern:** Multi-modal extraction pipeline + ML (fraud detection, risk scoring).

## Database Schema

```sql
CREATE TABLE claims (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    claim_number VARCHAR(100) NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL, -- NEW, IN_REVIEW, APPROVED, REJECTED, FLAGGED_FOR_FRAUD
    description TEXT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    incident_date TIMESTAMP NOT NULL,
    filed_date TIMESTAMP NOT NULL,
    ai_fraud_score DECIMAL(5, 2), -- 0 to 100
    ai_fraud_reasoning TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_claims_tenant_id ON claims(tenant_id);

CREATE TABLE policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    policy_type VARCHAR(100) NOT NULL, -- AUTO, HOME, LIFE, HEALTH
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    premium_amount DECIMAL(15, 2) NOT NULL,
    ai_risk_score DECIMAL(5, 2), -- 0 to 100
    ai_risk_factors TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_policies_tenant_id ON policies(tenant_id);
```

## REST API Endpoints

### Claims
- `GET /api/claims` - List all claims
- `GET /api/claims/{id}` - Get claim details
- `POST /api/claims` - Create a new claim (triggers AI fraud check)
- `PUT /api/claims/{id}/status` - Update claim status

### Policies
- `GET /api/policies` - List all policies
- `GET /api/policies/{id}` - Get policy details
- `POST /api/policies` - Create a new policy (triggers AI risk scoring)

### Analytics
- `GET /api/analytics/summary` - Get overview statistics (total claims, total fraud flagged, etc.)

## Services
- `ClaimService`: Manages CRUD operations for claims. Integrates with `AIFraudService`.
- `PolicyService`: Manages CRUD operations for policies. Integrates with `AIRiskService`.
- `AIFraudService`: Uses LiteLLM to analyze claim details and calculate a fraud score.
- `AIRiskService`: Uses LiteLLM to analyze policy details and customer profile to calculate a risk score.

## Frontend Pages (Next.js)
- `/`: Dashboard showing high-level stats, recent claims, and policies flagged for high risk.
- `/claims`: List of all claims with filtering and sorting.
- `/claims/[id]`: Detailed view of a claim, including AI fraud reasoning and score.
- `/policies`: List of all policies.
- `/policies/[id]`: Detailed view of a policy, including AI risk score and factors.

## AI Prompts
- **Fraud Detection Prompt:** "Analyze the following claim details for potential fraud. Consider the amount, description, and incident timing. Provide a fraud score from 0 (lowest risk) to 100 (highest risk) and a brief reasoning. Output exactly in JSON format: {\"score\": number, \"reasoning\": string}"
- **Risk Scoring Prompt:** "Analyze the following policy details and customer profile for underwriting risk. Consider the policy type, premium, and coverage dates. Provide a risk score from 0 (lowest risk) to 100 (highest risk) and a brief list of risk factors. Output exactly in JSON format: {\"score\": number, \"factors\": string}"

## Acceptance Criteria
1. User can create a claim, and the system automatically generates a fraud score using AI.
2. User can view a dashboard showing the total number of claims and average fraud score.
3. User can create a policy, and the system automatically generates a risk score using AI.
4. All endpoints respect `X-Tenant-ID` header.
