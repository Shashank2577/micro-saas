# Handoff Notes

## Assumptions
- Assuming LiteLLM responds in a format mirroring OpenAI's Chat Completions API. The logic extracts the content text from `choices[0].message.content`.
- Voice AI / Telephony functionality like real-time streaming audio integration with Twilio/WebRTC is outside scope of MVP, so we mock calls via HTTP simulation and test via the Simulator Panel text chat.
- Authentication/authorization is stubbed by passing a hardcoded `X-Tenant-ID` in the API client and using the `TenantContext` in the backend.

## Future Work
- Actual integration with a Voice-to-Text and Text-to-Voice provider for real-time WebRTC audio streaming.
- Proper authentication context for the frontend.
- Live event hooks for calls and agents to be consumed by external applications like billing.
