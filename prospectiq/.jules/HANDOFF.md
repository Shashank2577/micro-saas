## Assumptions & Known Gaps
- CRM Sync integration is mocked and simply returns OK without making external calls. Needs webhook configs.
- AI Generation connects to a local/mocked LiteLLMClient. Will need actual API keys deployed for production.
- Prospect brief PDF export returns mock text content instead of a real PDF for now.
