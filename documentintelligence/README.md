# Document Intelligence

Document Intelligence is an AI document processing platform. It ingests various document types (PDF, Word, Excel, images, scanned forms), extracts structured data, routes to appropriate workflows, answers questions about document contents, and tracks processing status.

## Features
- Multi-format document ingestion (PDF, Word, Excel, PNG, JPG, TIFF, scanned forms)
- OCR for scanned documents and images
- Structured data extraction (tables, key-value pairs, forms)
- Document classification (detect document type automatically)
- Semantic search within documents
- Question answering about document contents
- Processing status tracking

## Local Development

### Prerequisites
- Java 21
- Node.js 20+
- Maven
- Docker Desktop (for Postgres, Redis, etc.)

### Backend Setup
1. Ensure the shared `cc-starter` library is installed locally:
   cd ../cross-cutting && mvn clean install -DskipTests
2. Navigate to the backend directory:
   cd backend
3. Run the Spring Boot application:
   mvn spring-boot:run &
The backend will start on `http://localhost:8153`. Swagger UI is available at `http://localhost:8153/swagger-ui.html`.

### Frontend Setup
1. Navigate to the frontend directory:
   cd frontend
2. Install dependencies:
   npm install
3. Start the Next.js development server:
   npm run build && npm start &

## Testing
- Backend: mvn clean verify
- Frontend: npm test
