# GhostWriter Specification

## Overview
GhostWriter is an AI-powered content writing assistant. It generates blog posts, social media content, email copy, and marketing materials using AI.

## Entities (8+)
1. `Document` - The generated content or draft.
2. `Project` - A grouping of related documents.
3. `Template` - A predefined structure for specific content types (e.g., Blog Post Template).
4. `Persona` - Target audience details for content tailoring.
5. `StyleGuide` - Tone and style rules for content generation.
6. `ContentRequest` - A specific request for content generation to be processed by AI.
7. `Revision` - A historical version of a document.
8. `KeywordStrategy` - SEO keywords associated with a document or project.

## Services (7+)
1. `DocumentService` - CRUD for documents.
2. `ProjectService` - CRUD for projects.
3. `TemplateService` - CRUD for templates.
4. `PersonaService` - CRUD for personas.
5. `StyleGuideService` - CRUD for style guides.
6. `ContentGenerationService` - Integrates with LiteLLM to generate content.
7. `RevisionService` - Manages document versions.

## Controllers (5+)
1. `DocumentController`
2. `ProjectController`
3. `TemplateController`
4. `PersonaController`
5. `ContentGenerationController`

## AI Integration
- `ContentGenerationService` will use `AiService` (LiteLLM wrapper from cross-cutting) to process `ContentRequest` entities and generate content based on `Template`, `Persona`, and `StyleGuide`.

## Database
- Flyway migrations to create tables for all entities.
