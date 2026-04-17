# BackupVault

BackupVault is a multi-tenant micro-SaaS application for automated backups, point-in-time recovery, cross-region replication, and disaster recovery.

## Setup
- Start infra: `cd ../infra && docker compose up -d`
- Build cross-cutting parent: `cd ../cross-cutting && mvn clean install`
- Build backend: `cd backend && mvn clean verify`
- Build frontend: `cd frontend && npm install && npm run build`

## Features
- Backup Policies & Executions
- Restores & PITR
- Disaster Recovery Drills
- AI Backup Optimization
