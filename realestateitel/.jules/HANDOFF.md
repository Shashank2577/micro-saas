## Questions Resolved During Build
- Q: How should we handle the actual PDF text extraction for leases?
  A: Assumed text extraction happens before hitting the backend or is handled by a standard library like Apache PDFBox. For this implementation, the endpoint will accept text directly for AI processing to abstract it.
- Q: Where does the market trend data come from?
  A: Assumed it's manually entered or ingested from an external system. We'll provide CRUD for it.

## Assumptions
- Real estate API (like MLS or Zillow) is not directly integrated. Comps are generated from internal data or mocked via AI.
- Lease PDF processing is mocked. We'll accept raw text for now.

## Future Work
- [ ] Integrate actual PDF parsing (e.g., Apache PDFBox).
- [ ] Connect to real MLS data feed for comparables.
- [ ] Implement map view for properties and comparables.
