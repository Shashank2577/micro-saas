# AGENTS.md

## Project Overview
Building a portfolio of 91 AI-native SaaS applications on a shared foundation stack (freestack + cc-starter). Hub-and-spoke architecture with Nexus Hub as central orchestrator.

## Tech Stack
- **Backend:** Spring Boot 3.3.5 (Java 21), Maven
- **Frontend:** Next.js (App Router), TypeScript, Tailwind CSS
- **Infrastructure:** Terraform, GitHub Actions, Docker Compose
- **Database:** PostgreSQL (Neon in prod), pgmq for queues, pgvector for search
- **Auth:** Keycloak
- **Storage:** MinIO (S3-compatible)
- **AI:** LiteLLM (multi-provider orchestration)
- **Testing:** JUnit 5, Jest, Playwright
- **Package Manager:** Maven (backend), npm (frontend)

## Code Conventions
- **Java:** camelCase for variables, PascalCase for classes, UPPER_SNAKE_CASE for constants
- **TypeScript/React:** camelCase for variables/functions, PascalCase for components
- **Linter:** ESLint + Prettier (frontend), Checkstyle + Spotless (backend)
- **File Organization:**
  - Backend: Domain-driven design with entities, services, repositories, controllers
  - Frontend: App Router structure with /app, /components, /lib

## Build & Test
- **Backend:** `mvn clean install`, `mvn test`, `mvn spring-boot:run`
- **Frontend:** `npm install`, `npm run build`, `npm test`, `npm run dev`
- **Integration:** `docker-compose up -d` to start local platform

## Important Notes
- All apps use cc-starter parent POM with 16 pre-built modules
- Tenancy is multi-tenant by default (tenant_id on all tables)
- All entities emit webhook events via pgmq
- Nexus Hub manages app registry and cross-app workflows
- Every app has an integration manifest defining its API contracts
- Environment variables override application.properties for secrets

## Foundation Stack Locations
- **freestack:** `~/Documents/Personal/Code/freestack` (deployment templates)
- **cross-cutting:** `~/Documents/Personal/Code/cross-cutting` (cc-starter modules)
- **current project:** Micro-SaaS ecosystem (apps + Nexus Hub)

## Project Phases
1. **Foundation:** Setup cc-starter, Docker platform, Nexus Hub, scaffold tools
2. **Cluster A:** Developer Intelligence apps (5 apps)
3. **Remaining clusters:** Execute in waves of up to 30 parallel Jules sessions
