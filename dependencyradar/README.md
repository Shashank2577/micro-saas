# DependencyRadar — Open source dependency vulnerability monitoring

> Part of the [micro-saas ecosystem](../README.md) | **Cluster A** | Port 8092

## What It Does

DependencyRadar continuously scans your repositories' open source dependencies against vulnerability databases (CVE, OSV, GitHub Advisories) and surfaces actionable upgrade paths before vulnerabilities reach production. When code is pushed or a PR is merged it automatically re-scans the dependency tree, emits structured vulnerability events, and can open upgrade PRs directly against your repository.

## AI Pattern

Agent — automated scan-and-remediate pipeline that assesses upgrade impact and generates pull requests for dependency upgrades.

## Stack

- **Backend:** Spring Boot 3.3.5 + cc-starter 0.1.0-SNAPSHOT (port 8092)
- **Database:** PostgreSQL (schema: `dependencyradar`)

## Key Entities

- **Repository** — a scanned codebase with its dependency manifest
- **Dependency** — a tracked package with name, version, and ecosystem
- **Vulnerability** — a CVE/OSV finding linked to a specific dependency version
- **DependencyReport** — full scan output for a repository at a point in time
- **UpgradePR** — a generated pull request proposing a dependency upgrade

## API Endpoints

```
POST   /api/scans                          Trigger a dependency scan (input: repoUrl)
GET    /api/scans/{id}                     Get scan results / dependency report
GET    /api/vulnerabilities                List all known vulnerabilities
GET    /api/vulnerabilities/{dep}/{version} Check a specific package version
POST   /api/upgrades                       Request upgrade impact analysis
POST   /api/upgrades/{id}/pr               Create upgrade PR in repository
GET    /actuator/health                    Health check
```

## Running Locally

```bash
cd dependencyradar/backend && mvn spring-boot:run
# API: http://localhost:8092
```

## Integration

Emits and consumes events through [Nexus Hub](../nexus-hub/) (port 8090).

**Emits:** `vulnerability-detected`, `dependency-outdated`, `upgrade-available`
**Consumes:** `code.pushed`, `pr.merged`

See [integration-manifest.json](integration-manifest.json) for the full event schema.
