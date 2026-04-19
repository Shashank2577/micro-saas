# EquityIntelligence Detailed Implementation Spec

## Product Intent
- **App:** `equityintelligence`
- **Domain:** Finance Operations
- **Outcome:** Cap table, vesting, and round scenario intelligence for founders and finance teams
- **Primary actors:** Founder, Finance lead, Legal ops

## Database Schema

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cap_tables (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_cap_tables_tenant ON cap_tables(tenant_id);

CREATE TABLE shareholders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_shareholders_tenant ON shareholders(tenant_id);

CREATE TABLE vesting_schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_vesting_schedules_tenant ON vesting_schedules(tenant_id);

CREATE TABLE funding_rounds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_funding_rounds_tenant ON funding_rounds(tenant_id);

CREATE TABLE dilution_scenarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_dilution_scenarios_tenant ON dilution_scenarios(tenant_id);

CREATE TABLE option_pool_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_option_pool_plans_tenant ON option_pool_plans(tenant_id);
```

## API Endpoints (REST)

For each module (cap-tables, shareholders, vesting-schedules, funding-rounds, dilution-scenarios, option-pool-plans), the following endpoints are implemented under `/api/v1/equity/{module}`:
- `GET /` - List all records for tenant
- `POST /` - Create record
- `GET /{id}` - Get record by ID
- `PATCH /{id}` - Update record
- `DELETE /{id}` - Delete record (hard or soft depending on metadata)
- `POST /{id}/validate` - Validate domain rules
- `POST /{id}/simulate` - Simulate scenarios

AI/Workflows under `/api/v1/equity`:
- `POST /ai/analyze`
- `POST /ai/recommendations`
- `POST /workflows/execute`
- `GET /health/contracts`
- `GET /metrics/summary`

## Frontend Pages (Next.js)

Pages will be in `app/dashboard/(equity)/...`
- `cap-tables/` - Table with pagination
- `cap-tables/[id]` - Detail view, editing, timeline
- `shareholders/` - Table with pagination
- `shareholders/[id]` - Detail view
- `vesting-schedules/` - Table
- `vesting-schedules/[id]` - Detail view
- `funding-rounds/` - Table
- `funding-rounds/[id]` - Detail view
- `dilution-scenarios/` - Table
- `dilution-scenarios/[id]` - Detail view
- `option-pool-plans/` - Table
- `option-pool-plans/[id]` - Detail view

## Integration Manifest

Emits:
- `equityintelligence.round.updated`
- `equityintelligence.dilution.simulated`
- `equityintelligence.vesting.alerted`

Consumes:
- `financenarrator.narrative.generated`
- `budgetpilot.reforecast.completed`
- `contractportfolio.contract.indexed`

## Testing Strategy
- Backend unit and integration tests (using `@DataJpaTest` and `@SpringBootTest` with Testcontainers).
- Frontend component tests using Vitest + Testing Library + `user-event`.
