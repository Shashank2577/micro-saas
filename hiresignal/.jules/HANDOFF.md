## Questions Resolved During Build
- Old entities (`Candidate`, `JobRequisition`, `Evaluation`, `HireOutcome`) were present in the codebase but did not align exactly with the detailed specification domains. I chose to delete them and recreate new entities corresponding strictly to the spec (`CandidateProfile`, `FitSignal`, `InterviewStage`, `HiringDecision`, `PipelineMetric`, `Requisition`).

## Assumptions
- Frontend index page (`/`) fetches dynamic API metrics at build time. To avoid build failures without a running backend server, I added `export const dynamic = 'force-dynamic'` to `src/app/page.tsx` so that Next.js server-renders the page on demand rather than trying to pre-render it.
- LiteLLM AI operations are currently mocked in the `AiWorkflowService`. Real integration is required before full production deployment.

## Future Work
- Add E2E tests integrating real backend database via Testcontainers.
- Connect the AI endpoints properly using Spring AI or direct HTTP client logic targeting LiteLLM.
- Polish frontend UI using Tailwind components to look more like a Dashboard rather than raw lists.
