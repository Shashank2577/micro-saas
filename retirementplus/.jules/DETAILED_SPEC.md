# Detailed Specification for RetirementPlus

## Database Schema (PostgreSQL)

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    current_age INTEGER NOT NULL,
    retirement_age INTEGER NOT NULL,
    current_savings DECIMAL(19, 2) NOT NULL,
    desired_income DECIMAL(19, 2) NOT NULL,
    gender VARCHAR(50),
    health_status VARCHAR(50),
    family_history VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    life_expectancy INTEGER,
    safe_withdrawal_rate DECIMAL(5, 2),
    social_security_claiming_age INTEGER,
    estimated_healthcare_cost DECIMAL(19, 2),
    qcd_opportunity_age INTEGER,
    probability_of_success DECIMAL(5, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projection_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_profiles_tenant_id ON user_profiles(tenant_id);
CREATE INDEX idx_projections_user_id ON projections(user_id);
CREATE INDEX idx_projections_tenant_id ON projections(tenant_id);
```

## REST Endpoints

### 1. `POST /api/profiles`
- **Request:** Create profile with user details (age, savings, goals).
```json
{
  "userId": "uuid",
  "currentAge": 60,
  "retirementAge": 67,
  "currentSavings": 500000.00,
  "desiredIncome": 60000.00,
  "gender": "MALE",
  "healthStatus": "HEALTHY",
  "familyHistory": "MODERATE"
}
```
- **Response:** 201 Created with saved profile.

### 2. `GET /api/profiles/{userId}`
- **Response:** 200 OK with profile data.

### 3. `POST /api/projections/analyze/{userId}`
- **Action:** Generates retirement projections including SS, Life Expectancy, Safe Withdrawal.
- **Integration:** Calls LiteLLM for longevity and complex strategy predictions.
- **Response:** 200 OK
```json
{
  "lifeExpectancy": 89,
  "safeWithdrawalRate": 4.0,
  "socialSecurityClaimingAge": 67,
  "estimatedHealthcareCost": 300000.00,
  "qcdOpportunityAge": 72,
  "probabilityOfSuccess": 80.0
}
```

## Services

### 1. `ProfileService`
- `createProfile(ProfileDto dto, UUID tenantId)`
- `getProfileByUserId(UUID userId, UUID tenantId)`

### 2. `ProjectionService`
- `generateProjection(UUID userId, UUID tenantId)`
  - Calculates SWR based on current savings & desired income.
  - Calls `LiteLLMClient` for contextual longevity & healthcare estimates.
  - Returns combined projection.

## Frontend (Next.js)
- `/app/page.tsx` - Landing/Dashboard
- `/app/profile/page.tsx` - Form for inputs
- `/app/projections/page.tsx` - Display results (Charts for SWR, SS)
- `components/ProfileForm.tsx`
- `components/ResultsDashboard.tsx`

### Phase 2 Extension: Missing Requirements
**Additional Database Fields:**
- `user_profiles`: `inheritance_goal` (DECIMAL), `wants_annuity` (BOOLEAN)
- `projections`: `roth_conversion_amount` (DECIMAL), `rmd_amount` (DECIMAL), `stress_test_survival_rate` (DECIMAL), `annuity_guaranteed_income` (DECIMAL), `tax_strategy_order` (VARCHAR)

**Updated Endpoints:**
- `POST /api/profiles`: Include `inheritanceGoal` and `wantsAnnuity`.
- `POST /api/projections/analyze/{userId}`: Include new calculation results.

**Additional Service Logic:**
- Calculate RMDs based on IRS rules (simplified: start at age 73).
- Utilize `LiteLLMClient` to predict:
  - Tax-efficient withdrawal order (e.g., "Taxable -> Traditional -> Roth")
  - Roth conversion recommendations for Year 1
  - Stress testing the portfolio against 2008-style market crash
  - Annuity comparison if `wantsAnnuity` is true.
