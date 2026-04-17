# SecurityPulse — Code security scanner + policy enforcement

> Part of the [micro-saas ecosystem](../README.md) | **Cluster A** | Port 8095

## What It Does

SecurityPulse scans pull requests and merged code for security vulnerabilities, secrets, and policy violations before they land in your main branch. It integrates into the PR lifecycle to enforce configurable security policies — blocking merges on critical findings — and triages results into prioritised reports so security teams focus remediation effort on the highest-risk issues.

## AI Pattern

Agent — automated scan-triage-enforce pipeline that classifies findings by severity and applies policy decisions without human intervention.

## Stack

- **Backend:** Spring Boot 3.3.5 + cc-starter 0.1.0-SNAPSHOT (port 8095)
- **Database:** PostgreSQL (schema: `securitypulse`)

## Key Entities

- **ScanJob** — a triggered security scan against a PR or repository
- **Finding** — a specific vulnerability, secret, or policy violation with severity
- **Policy** — a configurable rule set that maps findings to enforcement actions
- **PolicyDecision** — the outcome (pass / block / warn) for a given scan result
- **TriagedReport** — a prioritised, deduplicated summary of findings for a scan

## API Endpoints

```
POST   /api/scans                     Trigger a security scan (input: prUrl)
GET    /api/scans/{id}                Get scan results
GET    /api/scans/{id}/findings       List findings for a scan
POST   /api/scans/{id}/triage         Triage findings into a prioritised report
POST   /api/policies                  Create a security policy
GET    /api/policies                  List active policies
POST   /api/policies/enforce          Apply policies to scan results
GET    /actuator/health               Health check
```

## Running Locally

```bash
cd securitypulse/backend && mvn spring-boot:run
# API: http://localhost:8095
```

## Integration

Emits and consumes events through [Nexus Hub](../nexus-hub/) (port 8090).

**Emits:** `security-scan-completed`, `vulnerability-found`, `policy-violation`
**Consumes:** `pr.opened`, `pr.merged`

See [integration-manifest.json](integration-manifest.json) for the full event schema.
