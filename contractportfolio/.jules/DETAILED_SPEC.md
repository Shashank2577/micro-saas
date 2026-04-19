# ContractPortfolio - Detailed Implementation Specification

## 1. Overview
The ContractPortfolio application serves as a Legal Intelligence module for contract extraction, risk indexing, and renewal intelligence.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE IF NOT EXISTS contract_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_contract_records_tenant ON contract_records(tenant_id);

CREATE TABLE IF NOT EXISTS clause_extractions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_clause_extractions_tenant ON clause_extractions(tenant_id);

CREATE TABLE IF NOT EXISTS renewal_alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_renewal_alerts_tenant ON renewal_alerts(tenant_id);

CREATE TABLE IF NOT EXISTS risk_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_risk_scores_tenant ON risk_scores(tenant_id);

CREATE TABLE IF NOT EXISTS obligation_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_obligation_items_tenant ON obligation_items(tenant_id);

CREATE TABLE IF NOT EXISTS counterparty_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_counterparty_profiles_tenant ON counterparty_profiles(tenant_id);
```

## 3. Backend Services & Endpoints

### 3.1 Entities
- `ContractRecord`
- `ClauseExtraction`
- `RenewalAlert`
- `RiskScore`
- `ObligationItem`
- `CounterpartyProfile`
Each entity has: `id`, `tenantId`, `name`, `status`, `metadataJson` (String mapped to JSONB), `createdAt`, `updatedAt`.

### 3.2 Services
For each entity:
- `create(dto)`
- `update(id, dto)`
- `list()`
- `getById(id)`
- `delete(id)`
- `validate(id)`
- `simulate(id)`

### 3.3 Controllers
Mapped to `/api/v1/contracts/*`:
- `contract-records`
- `clause-extractions`
- `renewal-alerts`
- `risk-scores`
- `obligation-items`
- `counterparties`

Additional endpoints:
- POST `/api/v1/contracts/ai/analyze`
- POST `/api/v1/contracts/ai/recommendations`
- POST `/api/v1/contracts/workflows/execute`
- GET `/api/v1/contracts/health/contracts`
- GET `/api/v1/contracts/metrics/summary`

### 3.4 Integration Events
**Emits:**
- `contractportfolio.contract.indexed`
- `contractportfolio.renewal.alerted`
- `contractportfolio.risk.updated`

**Consumes:**
- `documentvault.document.uploaded`
- `contractsense.contract.analyzed`
- `regulatoryfiling.filing.submitted`

## 4. Frontend Requirements
- Pages: Contracts, Clause Extractions, Obligations, Risk Scores, Renewals, Counterparties.
- Components: Tables with pagination, Create/Edit Modals, Detail Views.
- Data fetching using standard fetch API or SWR, proxying to backend.

## 5. Deployment
- Backend Dockerfile.
- Integration manifest sync.
