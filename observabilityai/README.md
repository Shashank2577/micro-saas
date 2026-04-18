# ObservabilityAI

ObservabilityAI is an AI-powered observability platform. It collects logs, metrics, and distributed traces, and uses AI to automatically correlate signals across the stack.

## Tech Stack
- **Backend**: Spring Boot 3, Java 21, PostgreSQL
- **Frontend**: Next.js 15, TypeScript, Tailwind CSS
- **AI Integration**: LiteLLM for signal correlation and analysis

## Testing
- Backend tests: `mvn -pl observabilityai/backend clean verify`
- Frontend tests: `cd observabilityai/frontend && npm test`
