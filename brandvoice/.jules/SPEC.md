# BrandVoice Spec
BrandVoice is an application designed to help companies maintain consistent brand messaging across various channels using AI.

## Entities
1. BrandProfile (id, tenantId, name, description, industry, createdAt, updatedAt)
2. BrandGuideline (id, tenantId, brandProfileId, category, rule, createdAt)
3. ToneOfVoice (id, tenantId, brandProfileId, adjective, definition, usageExample)
4. VocabularyList (id, tenantId, brandProfileId, type, word, alternative)
5. ContentAsset (id, tenantId, title, content, type, status, aiAnalysisScore, createdAt, updatedAt)
6. AnalysisReport (id, tenantId, contentAssetId, score, feedback, generatedAt)
7. ContentProject (id, tenantId, name, status, targetAudience, dueDate)
8. Campaign (id, tenantId, name, objective, startDate, endDate, status)

## Core Domain Feature
An AI-powered content analysis service that checks content against brand guidelines, tone of voice, and vocabulary to provide a score and feedback.
