# MarketplaceHub

MarketplaceHub is an app discovery and marketplace system. It catalogs all microapps, enables app discovery, handles app ratings/reviews, and manages app installation for workspaces.

## Features
- App catalog with metadata, descriptions, screenshots, and pricing
- App categorization and tagging for discovery
- Full-text search across app descriptions and features
- App installation workflow with permission grants
- Rating and review system with moderation
- Usage statistics and trending app analytics

## Getting Started

### Prerequisites
- Java 21 & Maven 3.9
- Node.js 20 & npm

### Backend Setup
Run the following in the `backend` directory:
- `mvn clean install`
- `mvn spring-boot:run`

### Frontend Setup
Run the following in the `frontend` directory:
- `npm install`
- `npm run dev &`

### Testing
- Backend tests: `mvn clean test` (within `backend` directory)
- Frontend tests: `npm test` (within `frontend` directory)
