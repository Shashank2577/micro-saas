# VendorMonitor Detailed Specification

## Overview
VendorMonitor is an AI-powered vendor performance tracking application. It monitors SLA compliance from actual service/ticket data, tracks invoice accuracy and billing consistency, scores vendor performance over time with trend analysis, alerts on SLA breaches with evidence, and generates performance summaries for renewal negotiations.

## Database Schema

```sql
-- V1__init.sql

CREATE TABLE vendors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE, IN_REVIEW
    contact_email VARCHAR(255),
    website VARCHAR(255),
    sla_description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_vendors_tenant ON vendors(tenant_id);

CREATE TABLE contracts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE,
    value_amount DECIMAL(15,2),
    value_currency VARCHAR(3) DEFAULT 'USD',
    sla_response_time_minutes INTEGER,
    sla_uptime_percentage DECIMAL(5,2),
    auto_renew BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_contracts_tenant ON contracts(tenant_id);
CREATE INDEX idx_contracts_vendor ON contracts(vendor_id);

CREATE TABLE performance_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    record_type VARCHAR(50) NOT NULL, -- TICKET, UPTIME, INVOICE, MANUAL_REVIEW
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    metric_value DECIMAL(10,2),
    metric_unit VARCHAR(50),
    is_sla_breach BOOLEAN DEFAULT FALSE,
    description TEXT,
    evidence_url VARCHAR(1024),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_perf_records_tenant ON performance_records(tenant_id);
CREATE INDEX idx_perf_records_vendor ON performance_records(vendor_id);
CREATE INDEX idx_perf_records_date ON performance_records(recorded_at);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL, -- INFO, WARNING, CRITICAL
    status VARCHAR(50) NOT NULL, -- OPEN, ACKNOWLEDGED, RESOLVED
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    resolved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_alerts_tenant ON alerts(tenant_id);
CREATE INDEX idx_alerts_vendor ON alerts(vendor_id);
CREATE INDEX idx_alerts_status ON alerts(status);

CREATE TABLE renewal_summaries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    contract_id UUID REFERENCES contracts(id),
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    overall_score INTEGER NOT NULL, -- 0-100
    ai_summary TEXT NOT NULL,
    recommendation VARCHAR(50) NOT NULL, -- RENEW, RENEGOTIATE, CANCEL
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_renewal_summaries_tenant ON renewal_summaries(tenant_id);
CREATE INDEX idx_renewal_summaries_vendor ON renewal_summaries(vendor_id);
```

## REST Endpoints

All endpoints require the `X-Tenant-ID` header.

### Vendors
- `GET /api/vendors` - List all vendors
- `POST /api/vendors` - Create a vendor
- `GET /api/vendors/{id}` - Get vendor details
- `PUT /api/vendors/{id}` - Update vendor
- `DELETE /api/vendors/{id}` - Delete vendor

### Contracts
- `GET /api/vendors/{vendorId}/contracts` - List contracts for a vendor
- `POST /api/vendors/{vendorId}/contracts` - Create a contract
- `GET /api/contracts/{id}` - Get contract details
- `PUT /api/contracts/{id}` - Update contract

### Performance Records
- `GET /api/vendors/{vendorId}/performance` - List performance records for a vendor
- `POST /api/vendors/{vendorId}/performance` - Record a new performance metric
- `GET /api/vendors/{vendorId}/metrics-summary` - Get aggregated metrics (uptime avg, response time avg, breach count)

### Alerts
- `GET /api/alerts` - List open alerts across all vendors
- `GET /api/vendors/{vendorId}/alerts` - List alerts for a vendor
- `PUT /api/alerts/{id}/status` - Update alert status (e.g., Acknowledge/Resolve)

### AI and Reports
- `POST /api/vendors/{vendorId}/generate-summary` - Trigger AI to generate a renewal summary based on performance records and contracts
- `GET /api/vendors/{vendorId}/summaries` - List generated summaries
- `GET /api/summaries/{id}` - Get a specific summary

## Backend Services
- `VendorService`: CRUD for vendors
- `ContractService`: CRUD for contracts
- `PerformanceService`: Recording metrics, calculating aggregations, detecting SLA breaches (triggers alert creation)
- `AlertService`: Managing alert lifecycle
- `AISummaryService`: Uses `LiteLLMClient` to fetch vendor context (contract SLAs + recent performance records), sends to LLM with a prompt to analyze performance, identify trends, and recommend action (Renew/Renegotiate/Cancel) and generate a summary report.

## Frontend (Next.js)

### Pages
- `/` (Dashboard) - Overview of all vendors, top alerts, overall compliance score
- `/vendors` - List of vendors with high-level stats (active contracts, recent score)
- `/vendors/new` - Form to add a new vendor
- `/vendors/[id]` - Vendor details page. Tabs: 
  - Overview (Info, Contracts)
  - Performance (Charts showing metrics over time, recent records)
  - Alerts
  - Renewal Summaries
- `/alerts` - Global view of all alerts needing attention

### Components
- `VendorList`
- `VendorForm`
- `ContractList`
- `ContractForm`
- `PerformanceChart` (using react-chartjs-2)
- `PerformanceRecordList`
- `RecordPerformanceForm`
- `AlertList`
- `RenewalSummaryViewer`

## AI Integration Points
The backend `AISummaryService` will interact with LiteLLM.
Prompt outline: "Given the vendor details, contract SLAs, and the following list of performance records and SLA breaches over the last period, provide an overall performance score (0-100), a concise summary of their reliability and compliance, and a recommendation (RENEW, RENEGOTIATE, CANCEL) for their upcoming renewal."

## Testing & Acceptance
- Services must have 80%+ coverage.
- Controllers must be covered by integration tests (with test profile).
- Frontend components tested with Vitest and testing-library.
- Acceptance:
  - Can create vendor and contract.
  - Can log performance records.
  - Logging an SLA breaching record generates an Alert.
  - AI Summary generation succeeds and returns expected format.
