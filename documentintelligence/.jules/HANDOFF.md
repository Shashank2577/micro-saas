# Handoff Notes

## Assumptions Made
- AI logic (LiteLLM extraction and embeddings) and OCR (Tesseract) have been mocked out in the service layer for autonomous execution, as external API keys and local binaries were not guaranteed to be present.
- pgvector operations are mocked, as running actual similarity queries requires a running PG16 instance with pgvector extension loaded.
- Authentication utilizes the `X-Tenant-ID` header directly mapped via `cc-starter`.

## Future Work
- Implement actual LLM call using LiteLLM SDK / RestTemplate in `ExtractionService` and `QAService`.
- Implement actual Tesseract OCR call in `OCRService`.
- Replace in-memory vector mock with `pgvector` native queries once DB environment is fully provisioned.
- E2E tests with a real database and AI backend.
