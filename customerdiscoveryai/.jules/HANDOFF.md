## Questions Resolved During Build
- Q: How does the AI interviewer actually conduct the interview?
  A: For the scope of this backend/frontend build, the AI interview is simulated by an endpoint `POST /api/interviews/{id}/submit-transcript` where a completed conversation is uploaded. The core value of this platform is the *synthesis* of 100+ interviews, so we focus on the transcript processing.

## Assumptions
- LiteLLM is configured locally via `infra/compose.apps.yml` to route to a suitable model.

## Future Work
- Implement actual real-time voice/video interviewer via WebRTC and streaming LLM APIs.
