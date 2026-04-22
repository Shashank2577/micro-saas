# PolicyForge

## Overview
AI policy management platform. Write policies with AI co-pilot. Track versions with diff history. Surface policy gaps from incident and audit history. Auto-generate policy update summaries for employees. Send policies for acknowledgment and track completion.

## Entities (8+)
1. **Policy**: Core policy document.
2. **PolicyVersion**: Historical versions and diffs.
3. **PolicyAcknowledgment**: Tracking employee acknowledgment.
4. **PolicyDraft**: AI-assisted policy writing sessions.
5. **PolicyGap**: AI-surfaced gaps from incident and audit history.
6. **UpdateSummary**: Auto-generated policy update summaries for employees.
7. **PolicyCategory**: Organization tags/categories for policies.
8. **IncidentReference**: Links to external incidents/audits that triggered gaps.

## AI Pattern
Co-pilot (writing) + RAG (cross-referencing incidents and audits).
- **Drafting**: AI assists in drafting policies.
- **Summarization**: Auto-generates summaries of changes between PolicyVersions.
- **Gap Analysis**: AI evaluates policies against incidents to surface PolicyGaps.

## Endpoints
- CRUD for Policies, Versions, Drafts, Categories.
- Generate AI draft for PolicyDraft.
- Generate update summary for PolicyVersion diff.
- Acknowledge policies.
- List PolicyGaps.
