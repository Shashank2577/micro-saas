# Next 15 Apps — Jules Production Progression Plan

## Objective
Prepare the next 15 highest-priority post-wave apps for autonomous Jules execution with detailed specs and session payloads.

## App Order (priority)
1. localizationos (score: 55, tier: Alpha)
2. procurebot (score: 55, tier: Alpha)
3. vendoriq (score: 55, tier: Alpha)
4. runwaymodeler (score: 58, tier: Alpha)
5. analyticsbuilder (score: 60, tier: Beta)
6. apievolver (score: 60, tier: Beta)
7. billingai (score: 60, tier: Beta)
8. creatoranalytics (score: 60, tier: Beta)
9. datacatalogai (score: 60, tier: Beta)
10. hiresignal (score: 60, tier: Beta)
11. interviewos (score: 60, tier: Beta)
12. onboardflow (score: 60, tier: Beta)
13. peopleanalytics (score: 60, tier: Beta)
14. retentionsignal (score: 60, tier: Beta)
15. datagovernanceos (score: 63, tier: Beta)

## Deliverables
- Detailed per-app specs: `docs/jules/prod-wave-next-15/specs/*.md`
- Session payloads: `docs/jules/prod-wave-next-15/sessions/*.json`
- Dispatch script: `scripts/dispatch-prod-wave-next-15.sh`
- API-key dispatch script: `scripts/dispatch-prod-wave-next-15-api.sh`

## Readiness Gate Before Dispatch
1. Confirm current wave capacity has free slots
2. Ensure Jules auth/account context is correct for target key/account
3. Run dispatch script and monitor session ID creation
