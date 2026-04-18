# Handoff Notes

## Assumptions
- Multi-tenancy is enforced via `X-Tenant-ID` as per the `cc-starter` standard.
- The LiteLLM proxy at `http://localhost:4000` is expected to handle transcription and TTS if configured; otherwise, we might need direct integration or mock responses if API keys are missing in the environment.
- FFMPEG video merging (burning subtitles/audio into a final MP4) is computationally heavy. I will outline the structure for it but might simulate the final export step if the environment lacks full FFMPEG support.

## Future Work
- Implement actual user pricing/billing tracking via Stripe.
- Voice cloning implementation requires specific provider API access (e.g., ElevenLabs) which will be stubbed if keys are unavailable.
