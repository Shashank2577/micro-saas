# RegulatoryFiling Application

## Overview
RegulatoryFiling handles compliance automation, allowing tracking of filing obligations, jurisdiction schedules, and submission packets.

## Requirements
- Java 21
- PostgreSQL 16
- Node.js 18+

## Running the Application
1. Start the database (PostgreSQL on port 5432).
2. Run the backend: `mvn -pl regulatoryfiling/backend clean spring-boot:run`
3. Run the frontend: `npm --prefix regulatoryfiling/frontend run dev`

## Known Environment Variables
- `DB_HOST`: Database host (default: localhost)
- `DB_PORT`: Database port (default: 5432)
- `DB_NAME`: Database name (default: regulatoryfiling)
- `DB_USER`: Database user (default: postgres)
- `DB_PASSWORD`: Database password (default: postgres)
- `LITELLM_URL`: URL for LiteLLM AI provider
