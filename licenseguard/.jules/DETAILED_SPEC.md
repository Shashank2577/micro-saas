# LicenseGuard Detailed Spec

## Overview
AI software license compliance platform. Scans codebases and dependency trees, maps all licenses to their legal obligations, flags incompatible license combinations, generates SBOM documentation, and tracks compliance status over time.

## Entities (8+)
1. Repository
2. Dependency
3. License
4. LicenseObligation
5. LicenseViolation
6. SbomReport
7. ScanJob
8. CompliancePolicy

## AI Pattern
- Pipeline (scan on each repo push)
- RAG (license obligation database)

## Frontend
- Dashboard showing Repositories, Dependencies, and Violations.
- View for SBOM Reports.
- Compliance Policies management.
