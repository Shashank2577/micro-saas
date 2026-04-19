# EquityIntelligence

EquityIntelligence is a micro-SaaS application for managing cap tables, vesting schedules, funding rounds, and dilution scenarios.

## Development

### Backend (Spring Boot 3 + Java 21)
1. Ensure PostgreSQL is running.
2. Run \`mvn spring-boot:run\` from the \`backend\` directory.
3. Tests: \`mvn test\`

### Frontend (Next.js 14)
1. Run \`npm install\` then \`npm run dev\` from the \`frontend\` directory.
2. Tests: \`npm test\`

## Environment Variables

### Backend
- \`DB_HOST\` (default: localhost)
- \`DB_PORT\` (default: 5432)
- \`DB_NAME\` (default: equityintelligence)
- \`DB_USER\` (default: postgres)
- \`DB_PASSWORD\` (default: postgres)

## Integration Events
See \`integration-manifest.json\` for details on events this application emits and consumes.
