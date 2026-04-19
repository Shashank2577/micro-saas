# GhostWriter

GhostWriter is an AI-powered content writing assistant. It generates blog posts, social media content, email copy, and marketing materials using AI.

## Features
- Multi-format content generation (Blog, Social, Email, Marketing)
- Tone and style control
- Content history tracking

## Tech Stack
- Backend: Spring Boot 3, Java 21, Maven, PostgreSQL
- Frontend: Next.js (App Router), React, Tailwind CSS
- AI: LiteLLM proxy gateway

## Local Development

### Prerequisites
- Docker & Docker Compose
- Java 21
- Node.js 20+

### Running the Application
The easiest way to run the entire ecosystem is using Docker Compose from the root directory:
```bash
cd ../infra
docker-compose -f compose.apps.yml up ghostwriter-backend ghostwriter-frontend -d
```

### Running Locally without Docker
**Backend:**
```bash
cd backend
mvn spring-boot:run &
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev &
```

## Testing
- Backend: `cd backend && mvn test`
- Frontend: `cd frontend && npm test`

## Environment Variables
- `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` for PostgreSQL connection
- `AI_GATEWAY_URL`, `AI_API_KEY` for LiteLLM integration
