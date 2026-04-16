CREATE SCHEMA IF NOT EXISTS licenseguard;

CREATE TABLE licenseguard.repositories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    repo_url VARCHAR(255) NOT NULL,
    last_scanned_at TIMESTAMP,
    tenant_id UUID NOT NULL
);

CREATE TABLE licenseguard.dependencies (
    id UUID PRIMARY KEY,
    repository_id UUID NOT NULL REFERENCES licenseguard.repositories(id),
    name VARCHAR(255) NOT NULL,
    version VARCHAR(255),
    license VARCHAR(50),
    tenant_id UUID NOT NULL
);

CREATE TABLE licenseguard.license_violations (
    id UUID PRIMARY KEY,
    repository_id UUID NOT NULL REFERENCES licenseguard.repositories(id),
    dependency_id UUID NOT NULL REFERENCES licenseguard.dependencies(id),
    violation_type VARCHAR(50) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE licenseguard.sbom_reports (
    id UUID PRIMARY KEY,
    repository_id UUID NOT NULL REFERENCES licenseguard.repositories(id),
    generated_at TIMESTAMP NOT NULL,
    dependency_count INT NOT NULL,
    violation_count INT NOT NULL,
    report_json TEXT,
    tenant_id UUID NOT NULL
);
