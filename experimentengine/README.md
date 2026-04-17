# ExperimentEngine

**Tagline:** A/B testing with real statistical rigor — without paying Optimizely $5K/month

## Problem
SMBs run A/B tests in analytics tools with no stats, or pay $60K/yr for platforms they don't fully use.

## Solution
Experimentation platform with hypothesis gen, proper stats (frequentist + Bayesian), feature-flag integration, and peek-protection.

## Getting Started

### Prerequisites
- JDK 21+
- Node.js 20+
- Maven
- Postgres via Docker Compose
- LiteLLM mock via cc-starter

### Environment Variables
Configure these in your setup or use the defaults in `application.yml` / `.env`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

### Running Locally

**Backend:**
```bash
mvn -pl experimentengine/backend clean spring-boot:run
```

**Frontend:**
```bash
npm --prefix experimentengine/frontend run dev
```

### Testing

**Backend:**
```bash
mvn -pl experimentengine/backend clean verify
```

**Frontend:**
```bash
npm --prefix experimentengine/frontend run test
```

## Ports
- Backend: 8154
- Frontend: 8154 (via Next.js proxy)
