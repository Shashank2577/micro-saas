#!/usr/bin/env node

import fs from "node:fs";
import path from "node:path";

const cwd = process.cwd();

const reportPath = path.join(cwd, ".app-report-data.json");
if (!fs.existsSync(reportPath)) {
  console.error("Missing .app-report-data.json. Run audit generation first.");
  process.exit(1);
}

const report = JSON.parse(fs.readFileSync(reportPath, "utf8"));
const byApp = new Map(report.apps.map((a) => [a.app, a]));

const targetApps = [
  "budgetpilot",
  "ghostwriter",
  "invoiceprocessor",
  "jobcraftai",
  "seointelligence",
  "videonarrator",
  "securitypulse",
  "supportintelligence",
  "auditready",
  "constructioniq",
  "contentos",
  "dependencyradar",
  "licenseguard",
  "nexus-hub",
  "policyforge",
  "deploysignal",
  "cashflowanalyzer",
  "complianceradar",
  "copyoptimizer",
  "dataqualityai",
  "financenarrator",
  "legalresearch",
  "performancenarrative",
  "regulatoryfiling",
  "cashflowai",
  "compbenchmark",
  "contractportfolio",
  "debtnavigator",
  "equityintelligence",
  "goaltracker"
];

const wave1 = targetApps.slice(0, 15);
const wave2 = targetApps.slice(15);

const appCatalog = {
  budgetpilot: {
    title: "BudgetPilot",
    domain: "Finance Operations",
    route: "budgets",
    intent: "Budget planning with continuous variance intelligence and corrective actions",
    actors: ["Finance manager", "Department owner", "Controller"],
    nouns: ["BudgetPlan", "BudgetLine", "VarianceAlert", "SpendSnapshot", "ReforecastRun", "ApprovalPolicy"],
    modules: ["planning", "variance", "reforecasting", "approvals", "alerts", "reporting"],
    emits: ["budgetpilot.budget.created", "budgetpilot.variance.alerted", "budgetpilot.reforecast.completed"],
    consumes: ["invoiceprocessor.invoice.approved", "cashflowai.position.updated", "goaltracker.goal.updated"]
  },
  ghostwriter: {
    title: "Ghostwriter",
    domain: "Creator Intelligence",
    route: "content-drafts",
    intent: "Long-form writing co-pilot with voice preservation and revision workflows",
    actors: ["Founder", "Content lead", "Editor"],
    nouns: ["VoiceProfile", "DraftDocument", "RevisionRequest", "OutlineTemplate", "CitationBlock", "QualityScore"],
    modules: ["voice-profiles", "drafting", "revision", "citations", "quality", "publishing"],
    emits: ["ghostwriter.draft.created", "ghostwriter.revision.completed", "ghostwriter.publish.ready"],
    consumes: ["contentos.calendar.slot.created", "copyoptimizer.score.requested", "seointelligence.keyword.pack.generated"]
  },
  invoiceprocessor: {
    title: "InvoiceProcessor",
    domain: "Finance Automation",
    route: "invoices",
    intent: "Intelligent invoice ingestion, extraction, validation, and approval routing",
    actors: ["AP analyst", "Finance manager", "Procurement lead"],
    nouns: ["Invoice", "LineItem", "Vendor", "MatchingResult", "ApprovalFlow", "PaymentSchedule"],
    modules: ["ingestion", "extraction", "matching", "approval", "exceptions", "payments"],
    emits: ["invoiceprocessor.invoice.ingested", "invoiceprocessor.invoice.approved", "invoiceprocessor.exception.raised"],
    consumes: ["contractportfolio.contract.updated", "policyforge.policy.published", "taskqueue.job.retry.exhausted"]
  },
  jobcraftai: {
    title: "JobCraftAI",
    domain: "HR Talent",
    route: "job-postings",
    intent: "Generate inclusive, high-converting job descriptions with hiring analytics",
    actors: ["Recruiter", "Hiring manager", "People ops"],
    nouns: ["JobPosting", "RoleProfile", "BiasCheck", "ChannelPerformance", "CandidatePersona", "RevisionLog"],
    modules: ["role-profiles", "drafting", "bias-check", "publishing", "performance", "iterations"],
    emits: ["jobcraftai.posting.published", "jobcraftai.bias.issue.flagged", "jobcraftai.channel.performance.updated"],
    consumes: ["hiresignal.requisition.created", "peopleanalytics.role.benchmark.updated", "compbenchmark.band.changed"]
  },
  seointelligence: {
    title: "SEOIntelligence",
    domain: "Marketing Intelligence",
    route: "seo",
    intent: "Keyword opportunity, content gap analysis, and ranking movement intelligence",
    actors: ["SEO lead", "Content strategist", "Growth manager"],
    nouns: ["KeywordCluster", "ContentGap", "RankSnapshot", "SERPCompetitor", "OptimizationPlan", "CrawlIssue"],
    modules: ["keyword-clusters", "gap-analysis", "rank-tracking", "competitors", "optimization", "crawl-health"],
    emits: ["seointelligence.cluster.generated", "seointelligence.gap.found", "seointelligence.rank.alerted"],
    consumes: ["contentos.asset.published", "copyoptimizer.variant.promoted", "datastoryteller.metric.updated"]
  },
  videonarrator: {
    title: "VideoNarrator",
    domain: "Creator Intelligence",
    route: "videos",
    intent: "Repurpose long-form video into clips, summaries, chapters, and social derivatives",
    actors: ["Video editor", "Content manager", "Growth lead"],
    nouns: ["VideoAsset", "Transcript", "ClipSuggestion", "Chapter", "SocialVariant", "EngagementMetric"],
    modules: ["ingestion", "transcription", "chaptering", "clip-generation", "social-variants", "performance"],
    emits: ["videonarrator.transcript.ready", "videonarrator.clip.generated", "videonarrator.package.ready"],
    consumes: ["contentos.calendar.slot.created", "socialintelligence.trend.detected", "brandvoice.style.updated"]
  },
  securitypulse: {
    title: "SecurityPulse",
    domain: "Developer Security",
    route: "security",
    intent: "Code and dependency security posture tracking with policy-enforced remediation",
    actors: ["Security engineer", "Platform engineer", "Engineering manager"],
    nouns: ["SecurityFinding", "PolicyRule", "RepoScan", "RemediationTask", "RiskTrend", "SuppressionRecord"],
    modules: ["scans", "findings", "policies", "remediation", "risk-trends", "exceptions"],
    emits: ["securitypulse.finding.created", "securitypulse.policy.violation", "securitypulse.finding.resolved"],
    consumes: ["dependencyradar.vulnerability.detected", "deploysignal.deployment.started", "apigatekeeper.policy.changed"]
  },
  supportintelligence: {
    title: "SupportIntelligence",
    domain: "Customer Support Ops",
    route: "support",
    intent: "Ticket triage, auto-summarization, and root-cause clustering for support orgs",
    actors: ["Support agent", "Support manager", "Product ops"],
    nouns: ["SupportTicket", "ConversationSummary", "IntentCluster", "PlaybookAction", "Escalation", "ResolutionQuality"],
    modules: ["triage", "summaries", "clusters", "playbooks", "escalations", "quality"],
    emits: ["supportintelligence.ticket.triaged", "supportintelligence.escalation.created", "supportintelligence.cluster.updated"],
    consumes: ["usageintelligence.user.dropoff.detected", "incidentbrain.issue.correlated", "notificationhub.channel.registered"]
  },
  auditready: {
    title: "AuditReady",
    domain: "Compliance Automation",
    route: "audits",
    intent: "Evidence collection, control mapping, and readiness scoring for SOC2/ISO/GDPR",
    actors: ["Compliance lead", "Security lead", "Auditor liaison"],
    nouns: ["Control", "EvidenceItem", "AuditFramework", "ReadinessScore", "GapIssue", "RemediationPlan"],
    modules: ["frameworks", "controls", "evidence", "scoring", "gaps", "remediation"],
    emits: ["auditready.evidence.collected", "auditready.readiness.scored", "auditready.gap.opened"],
    consumes: ["policyforge.policy.published", "complianceradar.change.detected", "licenseguard.violation.detected"]
  },
  constructioniq: {
    title: "ConstructionIQ",
    domain: "Construction Operations",
    route: "projects",
    intent: "Construction project risk, budget, timeline, and resource intelligence",
    actors: ["Project manager", "Site supervisor", "Operations director"],
    nouns: ["ConstructionProject", "SiteReport", "Milestone", "SafetyIncident", "ChangeOrder", "ResourceAllocation"],
    modules: ["projects", "site-reports", "milestones", "safety", "change-orders", "resources"],
    emits: ["constructioniq.project.risk.updated", "constructioniq.milestone.delayed", "constructioniq.safety.incident.created"],
    consumes: ["budgetpilot.variance.alerted", "notificationhub.channel.registered", "documentvault.document.uploaded"]
  },
  contentos: {
    title: "ContentOS",
    domain: "Creator Operations",
    route: "content",
    intent: "Campaign planning, content lifecycle orchestration, and multi-channel publishing ops",
    actors: ["Content lead", "Editor", "Marketing ops"],
    nouns: ["ContentCampaign", "ContentAsset", "PublishingSlot", "ReviewTask", "DistributionPlan", "PerformanceSnapshot"],
    modules: ["campaigns", "assets", "calendar", "reviews", "distribution", "performance"],
    emits: ["contentos.asset.published", "contentos.review.requested", "contentos.campaign.updated"],
    consumes: ["ghostwriter.draft.created", "videonarrator.package.ready", "seointelligence.cluster.generated"]
  },
  dependencyradar: {
    title: "DependencyRadar",
    domain: "Developer Security",
    route: "dependencies",
    intent: "Dependency inventory, CVE detection, and remediation orchestration",
    actors: ["Security engineer", "Dev lead", "Platform owner"],
    nouns: ["DependencyAsset", "Vulnerability", "UpgradePlan", "ExposureScore", "PolicyException", "ScanRun"],
    modules: ["inventory", "scans", "vulnerabilities", "upgrades", "exceptions", "analytics"],
    emits: ["dependencyradar.vulnerability.detected", "dependencyradar.upgrade.recommended", "dependencyradar.scan.completed"],
    consumes: ["securitypulse.policy.violation", "apimanager.version.published", "deploysignal.deployment.risk.scored"]
  },
  licenseguard: {
    title: "LicenseGuard",
    domain: "Compliance Automation",
    route: "licenses",
    intent: "OSS license governance and usage policy enforcement across repositories",
    actors: ["Legal ops", "Security team", "Engineering lead"],
    nouns: ["LicenseAsset", "LicenseRule", "ComplianceIssue", "AttributionRecord", "ApprovalRequest", "SBOMSnapshot"],
    modules: ["inventory", "rules", "issues", "attribution", "approvals", "sbom"],
    emits: ["licenseguard.issue.detected", "licenseguard.policy.updated", "licenseguard.sbom.generated"],
    consumes: ["dependencyradar.vulnerability.detected", "auditready.framework.updated", "policyforge.policy.published"]
  },
  "nexus-hub": {
    title: "NexusHub",
    domain: "Platform Orchestration",
    route: "nexus",
    intent: "Central app registry, event bus governance, workflow orchestration, and integration contracts",
    actors: ["Platform admin", "Ops engineer", "Solution architect"],
    nouns: ["RegisteredApp", "EventContract", "WorkflowDefinition", "WorkflowRun", "Subscription", "IntegrationHealth"],
    modules: ["apps", "contracts", "subscriptions", "workflows", "runs", "health"],
    emits: ["nexushub.contract.updated", "nexushub.workflow.started", "nexushub.workflow.completed"],
    consumes: ["*.status.updated", "notificationhub.channel.registered", "observabilitystack.incident.created"]
  },
  policyforge: {
    title: "PolicyForge",
    domain: "Compliance Automation",
    route: "policies",
    intent: "Policy authoring, versioning, attestation, and enforcement workflows",
    actors: ["Compliance lead", "Security lead", "Department owner"],
    nouns: ["PolicyDocument", "PolicyVersion", "Attestation", "ExceptionRequest", "ControlMapping", "PolicyChange"],
    modules: ["authoring", "versions", "attestations", "exceptions", "mappings", "enforcement"],
    emits: ["policyforge.policy.published", "policyforge.attestation.missed", "policyforge.exception.approved"],
    consumes: ["complianceradar.change.detected", "auditready.gap.opened", "licenseguard.issue.detected"]
  },
  deploysignal: {
    title: "DeploySignal",
    domain: "Developer Intelligence",
    route: "deployments",
    intent: "DORA metrics, deployment risk scoring, and release guardrails",
    actors: ["Platform engineer", "Engineering manager", "Release manager"],
    nouns: ["Deployment", "RiskScore", "DoraMetricSnapshot", "IncidentLink", "RollbackEvent", "ReleaseWindow"],
    modules: ["deployments", "risk-scoring", "dora-metrics", "rollbacks", "windows", "reports"],
    emits: ["deploysignal.deployment.risk.scored", "deploysignal.rollback.detected", "deploysignal.dora.updated"],
    consumes: ["incidentbrain.incident.opened", "observabilitystack.alert.triggered", "securitypulse.finding.created"]
  },
  cashflowanalyzer: {
    title: "CashflowAnalyzer",
    domain: "Finance Operations",
    route: "cashflow",
    intent: "Historical cashflow diagnostics, forecasting, and anomaly explanations",
    actors: ["Finance analyst", "CFO", "Controller"],
    nouns: ["CashflowPeriod", "CashMovement", "TrendSignal", "ForecastRun", "AnomalyFlag", "NarrativeInsight"],
    modules: ["ingestion", "analysis", "forecasting", "anomalies", "insights", "reporting"],
    emits: ["cashflowanalyzer.forecast.generated", "cashflowanalyzer.anomaly.detected", "cashflowanalyzer.insight.published"],
    consumes: ["invoiceprocessor.invoice.approved", "budgetpilot.reforecast.completed", "financenarrator.summary.requested"]
  },
  complianceradar: {
    title: "ComplianceRadar",
    domain: "Compliance Automation",
    route: "regulations",
    intent: "Monitor regulatory changes and map deltas to internal controls and policies",
    actors: ["Compliance analyst", "Risk manager", "Security lead"],
    nouns: ["RegulationUpdate", "JurisdictionRule", "ImpactAssessment", "ControlGap", "TaskAssignment", "DeadlineAlert"],
    modules: ["feeds", "normalization", "impact-assessment", "gap-mapping", "tasks", "alerts"],
    emits: ["complianceradar.change.detected", "complianceradar.gap.opened", "complianceradar.deadline.alerted"],
    consumes: ["policyforge.policy.published", "auditready.readiness.scored", "regulatoryfiling.filing.submitted"]
  },
  copyoptimizer: {
    title: "CopyOptimizer",
    domain: "Marketing Intelligence",
    route: "copy",
    intent: "Generate and score conversion copy variants before publication",
    actors: ["Growth marketer", "Copywriter", "Performance manager"],
    nouns: ["CopyAsset", "Variant", "AudienceSegment", "PredictionScore", "ExperimentPlan", "WinningVariant"],
    modules: ["assets", "variants", "scoring", "experiments", "promotion", "analytics"],
    emits: ["copyoptimizer.variant.generated", "copyoptimizer.variant.promoted", "copyoptimizer.score.computed"],
    consumes: ["seointelligence.cluster.generated", "brandvoice.style.updated", "contentos.asset.created"]
  },
  dataqualityai: {
    title: "DataQualityAI",
    domain: "Data Reliability",
    route: "data-quality",
    intent: "Data quality rule authoring, drift detection, and automated remediation recommendations",
    actors: ["Data engineer", "Analytics engineer", "Platform owner"],
    nouns: ["QualityRule", "DatasetProfile", "DriftEvent", "ValidationRun", "IssueTicket", "RemediationSuggestion"],
    modules: ["rules", "profiling", "validation", "drift", "issues", "remediation"],
    emits: ["dataqualityai.rule.failed", "dataqualityai.drift.detected", "dataqualityai.remediation.suggested"],
    consumes: ["datacatalogai.asset.registered", "datalineagetracker.link.updated", "pipelineorchestrator.run.completed"]
  },
  financenarrator: {
    title: "FinanceNarrator",
    domain: "Finance Intelligence",
    route: "narratives",
    intent: "Generate executive-grade financial narratives from structured financial datasets",
    actors: ["CFO", "FP&A analyst", "Board prep owner"],
    nouns: ["NarrativeRequest", "NarrativeSection", "SupportingMetric", "ToneProfile", "ApprovalReview", "ExportArtifact"],
    modules: ["requests", "sections", "metrics", "tone-profiles", "reviews", "exports"],
    emits: ["financenarrator.narrative.generated", "financenarrator.review.requested", "financenarrator.export.completed"],
    consumes: ["cashflowanalyzer.insight.published", "budgetpilot.variance.alerted", "equityintelligence.round.updated"]
  },
  legalresearch: {
    title: "LegalResearch",
    domain: "Legal Intelligence",
    route: "research",
    intent: "Legal research workflow with citation tracking, precedent ranking, and memo generation",
    actors: ["Legal analyst", "Counsel", "Compliance manager"],
    nouns: ["ResearchQuery", "SourceCitation", "PrecedentNote", "MemoDraft", "ArgumentGraph", "ReviewComment"],
    modules: ["queries", "sources", "precedents", "memos", "arguments", "reviews"],
    emits: ["legalresearch.memo.generated", "legalresearch.citation.added", "legalresearch.review.requested"],
    consumes: ["complianceradar.change.detected", "policyforge.policy.published", "contractsense.risk.flagged"]
  },
  performancenarrative: {
    title: "PerformanceNarrative",
    domain: "HR Talent",
    route: "performance",
    intent: "Performance review narrative drafting with calibration support",
    actors: ["Manager", "HRBP", "People ops"],
    nouns: ["ReviewCycle", "EmployeeReview", "CalibrationNote", "GoalEvidence", "NarrativeDraft", "FeedbackItem"],
    modules: ["cycles", "reviews", "evidence", "narratives", "calibration", "feedback"],
    emits: ["performancenarrative.review.drafted", "performancenarrative.calibration.flagged", "performancenarrative.review.finalized"],
    consumes: ["goaltracker.goal.updated", "peopleanalytics.signal.updated", "retentionsignal.risk.detected"]
  },
  regulatoryfiling: {
    title: "RegulatoryFiling",
    domain: "Compliance Automation",
    route: "filings",
    intent: "Regulatory filing calendar, obligation tracking, and document assembly",
    actors: ["Compliance manager", "Legal ops", "Controller"],
    nouns: ["FilingObligation", "JurisdictionSchedule", "SubmissionPacket", "FilingDeadline", "ValidationCheck", "SubmissionReceipt"],
    modules: ["obligations", "calendar", "packets", "validations", "submissions", "receipts"],
    emits: ["regulatoryfiling.deadline.alerted", "regulatoryfiling.packet.ready", "regulatoryfiling.filing.submitted"],
    consumes: ["complianceradar.change.detected", "policyforge.policy.published", "auditready.gap.opened"]
  },
  cashflowai: {
    title: "CashflowAI",
    domain: "Finance Operations",
    route: "cash-position",
    intent: "Near-term cash position forecasting and shortfall mitigation automation",
    actors: ["Treasury analyst", "CFO", "Finance manager"],
    nouns: ["CashPosition", "LiquidityForecast", "ShortfallAlert", "MitigationOption", "ScenarioRun", "FundingEvent"],
    modules: ["positions", "forecasts", "shortfalls", "mitigations", "scenarios", "events"],
    emits: ["cashflowai.position.updated", "cashflowai.shortfall.detected", "cashflowai.mitigation.recommended"],
    consumes: ["invoiceprocessor.invoice.approved", "budgetpilot.variance.alerted", "debtnavigator.plan.approved"]
  },
  compbenchmark: {
    title: "CompBenchmark",
    domain: "HR Talent",
    route: "compensation-benchmark",
    intent: "Compensation benchmarking and pay-band intelligence",
    actors: ["HR lead", "People ops", "Finance partner"],
    nouns: ["CompBand", "MarketDataset", "BenchmarkRun", "GapFinding", "AdjustmentPlan", "ApprovalRecord"],
    modules: ["bands", "datasets", "benchmarking", "gaps", "plans", "approvals"],
    emits: ["compbenchmark.band.updated", "compbenchmark.gap.detected", "compbenchmark.plan.recommended"],
    consumes: ["jobcraftai.posting.published", "performancenarrative.review.finalized", "compensationos.cycle.created"]
  },
  contractportfolio: {
    title: "ContractPortfolio",
    domain: "Legal Intelligence",
    route: "contracts",
    intent: "Portfolio-wide contract extraction, risk indexing, and renewal intelligence",
    actors: ["Legal ops", "Procurement", "Finance controller"],
    nouns: ["ContractRecord", "ClauseExtraction", "RenewalAlert", "RiskScore", "ObligationItem", "CounterpartyProfile"],
    modules: ["contracts", "clause-extraction", "obligations", "risk-scoring", "renewals", "counterparties"],
    emits: ["contractportfolio.contract.indexed", "contractportfolio.renewal.alerted", "contractportfolio.risk.updated"],
    consumes: ["documentvault.document.uploaded", "contractsense.contract.analyzed", "regulatoryfiling.filing.submitted"]
  },
  debtnavigator: {
    title: "DebtNavigator",
    domain: "Personal Finance",
    route: "debt",
    intent: "Debt optimization plans with payoff simulation and consolidation analysis",
    actors: ["Financial advisor", "Individual user", "Credit counselor"],
    nouns: ["DebtAccount", "PaymentPlan", "OptimizationRun", "ConsolidationOffer", "RiskProjection", "MilestoneTrack"],
    modules: ["accounts", "plans", "optimization", "consolidation", "projections", "milestones"],
    emits: ["debtnavigator.plan.generated", "debtnavigator.plan.approved", "debtnavigator.milestone.completed"],
    consumes: ["cashflowai.shortfall.detected", "goaltracker.goal.updated", "wealthedge.portfolio.rebalanced"]
  },
  equityintelligence: {
    title: "EquityIntelligence",
    domain: "Finance Operations",
    route: "equity",
    intent: "Cap table, vesting, and round scenario intelligence for founders and finance teams",
    actors: ["Founder", "Finance lead", "Legal ops"],
    nouns: ["CapTable", "Shareholder", "VestingSchedule", "FundingRound", "DilutionScenario", "OptionPoolPlan"],
    modules: ["cap-table", "shareholders", "vesting", "rounds", "dilution", "option-pool"],
    emits: ["equityintelligence.round.updated", "equityintelligence.dilution.simulated", "equityintelligence.vesting.alerted"],
    consumes: ["financenarrator.narrative.generated", "budgetpilot.reforecast.completed", "contractportfolio.contract.indexed"]
  },
  goaltracker: {
    title: "GoalTracker",
    domain: "Personal Finance",
    route: "goals",
    intent: "Goal planning and progress tracking with automated coaching nudges",
    actors: ["Individual user", "Financial coach", "Advisor"],
    nouns: ["FinancialGoal", "GoalPlan", "ProgressSnapshot", "NudgeRecommendation", "Milestone", "ContributionEvent"],
    modules: ["goals", "plans", "progress", "nudges", "milestones", "contributions"],
    emits: ["goaltracker.goal.updated", "goaltracker.milestone.reached", "goaltracker.nudge.sent"],
    consumes: ["cashflowai.position.updated", "debtnavigator.plan.generated", "equityintelligence.round.updated"]
  }
};

function ensureDir(dirPath) {
  fs.mkdirSync(dirPath, { recursive: true });
}

function routeFromNoun(noun) {
  return noun
    .replace(/([a-z])([A-Z])/g, "$1-$2")
    .toLowerCase()
    .replace(/[^a-z0-9-]/g, "");
}

function entityFields(noun, app) {
  const base = [
    ["id", "UUID", "Primary key"],
    ["tenant_id", "UUID", "Tenant isolation key"],
    ["name", "varchar(180)", `${noun} display name`],
    ["status", "varchar(40)", "Lifecycle status enum"],
    ["metadata_json", "jsonb", "Extensible structured attributes"],
    ["created_at", "timestamptz", "Creation timestamp"],
    ["updated_at", "timestamptz", "Last updated timestamp"]
  ];
  const appSpecific = {
    BudgetPlan: [["period_start", "date", "Budget period start"], ["period_end", "date", "Budget period end"], ["amount", "numeric(14,2)", "Planned amount"]],
    Invoice: [["invoice_number", "varchar(80)", "Vendor invoice id"], ["due_date", "date", "Payment due date"], ["gross_amount", "numeric(14,2)", "Invoice gross amount"]],
    SecurityFinding: [["severity", "varchar(20)", "CRITICAL/HIGH/MEDIUM/LOW"], ["source", "varchar(60)", "Scanner source"], ["rule_key", "varchar(120)", "Detection rule identifier"]],
    RegulatoryUpdate: [["effective_date", "date", "Legal effective date"], ["jurisdiction", "varchar(80)", "Country/state"], ["source_url", "text", "Canonical publication URL"]],
    CashPosition: [["as_of", "date", "Snapshot date"], ["available_cash", "numeric(14,2)", "Available cash"], ["restricted_cash", "numeric(14,2)", "Restricted cash"]],
    FinancialGoal: [["target_amount", "numeric(14,2)", "Goal target amount"], ["target_date", "date", "Goal due date"], ["current_amount", "numeric(14,2)", "Current progress amount"]]
  };
  return [...base, ...(appSpecific[noun] || [])];
}

function generatedEndpoints(app, cfg) {
  const endpoints = [];
  const nouns = cfg.nouns.slice(0, 5);
  for (const noun of nouns) {
    const resource = routeFromNoun(noun) + "s";
    endpoints.push(`GET /api/v1/${cfg.route}/${resource}`);
    endpoints.push(`POST /api/v1/${cfg.route}/${resource}`);
    endpoints.push(`GET /api/v1/${cfg.route}/${resource}/{id}`);
    endpoints.push(`PATCH /api/v1/${cfg.route}/${resource}/{id}`);
    endpoints.push(`POST /api/v1/${cfg.route}/${resource}/{id}/validate`);
  }
  endpoints.push(`POST /api/v1/${cfg.route}/ai/analyze`);
  endpoints.push(`POST /api/v1/${cfg.route}/ai/recommendations`);
  endpoints.push(`POST /api/v1/${cfg.route}/workflows/execute`);
  endpoints.push(`GET /api/v1/${cfg.route}/health/contracts`);
  endpoints.push(`GET /api/v1/${cfg.route}/metrics/summary`);
  return endpoints;
}

function buildSpecMarkdown(app, cfg, maturity) {
  const risks = maturity?.risks || [];
  const lowHanging = [];
  if (risks.includes("no-backend-tests")) lowHanging.push("Add backend unit + integration test suite for service and controller paths.");
  if (risks.includes("no-frontend-tests")) lowHanging.push("Add frontend component and page interaction tests with Vitest + Testing Library.");
  if (risks.includes("no-db-migrations")) lowHanging.push("Introduce Flyway V1 baseline and follow-up incremental migrations.");
  if (risks.includes("no-backend-dockerfile")) lowHanging.push("Add backend Dockerfile aligned with Java 21 and healthcheck.");
  if (risks.includes("no-readme")) lowHanging.push("Publish operational README (runbook, env vars, known failure modes).");
  if (risks.includes("no-integration-manifest")) lowHanging.push("Create normalized integration-manifest contract and register with Nexus Hub.");
  if (!lowHanging.length) lowHanging.push("Harden integration contracts and increase non-functional test depth.");

  const integrationHints = (maturity?.shouldDo || []).slice(0, 4);
  const endpoints = generatedEndpoints(app, cfg);
  const score = maturity?.score ?? "n/a";
  const tier = maturity?.tier ?? "Unknown";
  const depth = maturity?.depth ?? "Unknown";

  const entityTables = cfg.nouns
    .map((noun) => {
      const rows = entityFields(noun, app)
        .map((field) => `| ${field[0]} | ${field[1]} | ${field[2]} |`)
        .join("\n");
      return `### ${noun}\n| Field | Type | Notes |\n|---|---|---|\n${rows}\n`;
    })
    .join("\n");

  const moduleChecklist = cfg.modules
    .map((module, i) => `- [ ] ${i + 1}. ${module}: backend service + controller + frontend page + tests`)
    .join("\n");

  const acceptance = [
    `All ${cfg.modules.length} module slices are implemented and wired from UI to backend.`,
    "All endpoints return tenant-scoped data and reject cross-tenant access.",
    "All write endpoints publish documented domain events with payload schema validation.",
    "OpenAPI docs render and include every endpoint in this spec.",
    "Backend tests cover happy path, validation failures, and permission failures.",
    "Frontend tests cover critical user flows and error states.",
    "Async workflows are queued via pgmq and are idempotent on retries.",
    "LiteLLM integration is guarded with timeout/retry/circuit breaker policy.",
    "All env var contracts are documented and validated on startup.",
    "No TODO/FIXME placeholders in production code paths.",
    "Integration manifest contracts match implemented emits/consumes events.",
    "PR includes .jules/DETAILED_SPEC.md, IMPLEMENTATION_LOG.md, and HANDOFF.md."
  ];

  return `# ${cfg.title} — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** \`${app}\`
- **Domain:** ${cfg.domain}
- **Outcome:** ${cfg.intent}
- **Primary actors:** ${cfg.actors.join(", ")}

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** ${score}
- **Tier:** ${tier}
- **Depth:** ${depth}
- **Known structural risks:** ${risks.length ? risks.join(", ") : "none flagged"}
- **Low-hanging fruits before/while implementation:**
${lowHanging.map((x) => `  - ${x}`).join("\n")}

## 3) Scope for This Jules Session (must be fully implemented)
${moduleChecklist}

## 4) Domain Model (authoritative object design)
${entityTables}

## 5) API Contract (minimum required endpoints)
${endpoints.map((ep) => `- ${ep}`).join("\n")}

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
${cfg.modules.map((m) => `- ${m}Service: create/update/list/getById/delete/validate/simulate`).join("\n")}

## 7) Event & Integration Contract
- **Emits:** ${cfg.emits.join(", ")}
- **Consumes:** ${cfg.consumes.join(", ")}
- Every emitted event must include: \`event_id\`, \`tenant_id\`, \`occurred_at\`, \`source_app\`, \`payload\`.
- Consumed events must be idempotent and deduplicated by \`event_id\`.
- Register/refresh contracts in \`integration-manifest.json\` and ensure Nexus Hub compatibility.

## 8) Frontend Requirements (Next.js App Router)
- Build operator-facing pages for all modules in section 3.
- Include:
  - Table/list view with filters and pagination
  - Detail page with timeline/activity
  - Create/edit forms with schema validation
  - Error/empty/loading states
  - Toast notifications and optimistic updates where safe
- Add component and page tests for critical paths.

## 9) AI/LLM Requirements
- Use LiteLLM through a dedicated client wrapper.
- Include:
  - Request schema validation
  - Timeout, retry (exponential backoff), and fallback behavior
  - Prompt templates versioned in code
  - Structured JSON output parsing with guardrails
  - Audit log of AI decisions (traceability)

## 10) Non-Functional & Security Requirements
- Multi-tenant hard isolation by \`tenant_id\` on every query and mutation.
- RBAC on every endpoint (minimum roles: admin/operator/viewer).
- PII fields encrypted at rest where applicable.
- Metrics:
  - P95 API latency < 300ms for reads, < 600ms for writes
  - Background jobs visible in metrics/health endpoints
- Observability:
  - Structured logs with correlation id
  - Error budget and alert thresholds
- Resilience:
  - Idempotency keys for external side-effects
  - Transaction boundaries for multi-write operations

## 11) Detailed Acceptance Criteria (must all pass)
${acceptance.map((a, i) => `${i + 1}. ${a}`).join("\n")}

## 12) Integration Opportunities (what this app should connect to)
${integrationHints.length ? integrationHints.map((x) => `- ${x}`).join("\n") : "- Integrate with adjacent apps in same domain and with Nexus Hub event bus."}

## 13) Explicit Build/Verification Commands
- Backend:
  - \`mvn -pl ${app}/backend clean verify\`
- Frontend:
  - \`npm --prefix ${app}/frontend test\`
  - \`npm --prefix ${app}/frontend run build\`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
`;
}

function buildSessionJson(app, cfg, maturity) {
  return {
    app_id: app,
    title: cfg.title,
    domain: cfg.domain,
    intent: cfg.intent,
    maturity: {
      score: maturity?.score ?? null,
      tier: maturity?.tier ?? null,
      depth: maturity?.depth ?? null,
      risks: maturity?.risks ?? []
    },
    requirements: cfg.modules.map((m) => `${m} module fully implemented with backend, frontend, tests`),
    constraints: [
      "No user feedback loops; autonomous operation only",
      "No TODO/stub placeholders in production paths",
      "All queries are tenant-scoped",
      "OpenAPI must include every endpoint",
      "integration-manifest emits/consumes must be implemented",
      "Unit + integration + UI tests required"
    ],
    acceptance_criteria: [
      "All listed modules available in UI + API",
      "All required endpoints implemented",
      "Events emitted/consumed according to manifest",
      "Backend and frontend tests pass",
      "PR submitted with .jules documentation bundle"
    ],
    architecture: {
      entities: cfg.nouns,
      services: cfg.modules.map((m) => `${m}Service`),
      route_prefix: `/api/v1/${cfg.route}`
    },
    events: {
      emits: cfg.emits,
      consumes: cfg.consumes
    }
  };
}

function writeFile(filePath, content) {
  ensureDir(path.dirname(filePath));
  fs.writeFileSync(filePath, content);
}

const baseDir = path.join(cwd, "docs/jules/prod-wave-30");
const specsDir = path.join(baseDir, "specs");
const sessionsDir = path.join(baseDir, "sessions");

ensureDir(specsDir);
ensureDir(sessionsDir);

for (const app of targetApps) {
  const cfg = appCatalog[app];
  if (!cfg) {
    throw new Error(`Missing app catalog config for ${app}`);
  }
  const maturity = byApp.get(app);
  const specMd = buildSpecMarkdown(app, cfg, maturity);
  const sessionJson = buildSessionJson(app, cfg, maturity);

  writeFile(path.join(specsDir, `${app}.md`), specMd);
  writeFile(path.join(sessionsDir, `${app}.json`), JSON.stringify(sessionJson, null, 2));
}

const waveManifest = {
  created_at: new Date().toISOString(),
  objective: "Progress 30 lowest-maturity apps toward production readiness via two autonomous Jules waves",
  total_apps: targetApps.length,
  wave_1: wave1,
  wave_2: wave2
};

writeFile(path.join(baseDir, "wave-manifest.json"), JSON.stringify(waveManifest, null, 2));

const waveMd = `# Production Progression Wave Plan (30 Apps)

## Goal
Move the lowest-maturity apps toward production readiness using two autonomous Jules waves.

## Wave 1 (15 apps)
${wave1.map((a, i) => `${i + 1}. ${a}`).join("\n")}

## Wave 2 (15 apps)
${wave2.map((a, i) => `${i + 1}. ${a}`).join("\n")}

## Asset Layout
- Specs: \`docs/jules/prod-wave-30/specs/*.md\`
- Session payloads: \`docs/jules/prod-wave-30/sessions/*.json\`
- Dispatch scripts: \`scripts/dispatch-prod-wave-30-wave1.sh\`, \`scripts/dispatch-prod-wave-30-wave2.sh\`
`;

writeFile(path.join(baseDir, "WAVE_PLAN.md"), waveMd);

const promptTemplate = String.raw`You are Jules, an autonomous software engineer working in ${"${REPO}"}.

CRITICAL EXECUTION MODE
- Work autonomously and tirelessly with no feedback loop.
- No clarifying questions; make reasoned assumptions and document them.
- Push commits after each phase to avoid timeout loss.
- Submit PR when complete.

CONTEXT
- App: ${"${APP_ID}"}
- Detailed spec:
${"${SPEC_CONTENT}"}

PROTOCOL
${"${PROTOCOL_CONTENT}"}

PHASES
- Phase 1: Expand/validate detailed spec into ${"${APP_ID}"}/.jules/DETAILED_SPEC.md, commit+push
- Phase 2: Implement backend+frontend+tests+integration contracts, commit+push
- Phase 3: Run tests, fix failures, update .jules/IMPLEMENTATION_LOG.md and HANDOFF.md, commit+push
- Phase 4: Open PR to main with references to .jules docs and acceptance criteria
`;

function buildDispatchScript(fileName, apps) {
  const lines = [];
  lines.push("#!/usr/bin/env bash");
  lines.push("set -euo pipefail");
  lines.push("");
  lines.push('REPO="Shashank2577/micro-saas"');
  lines.push('REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"');
  lines.push('WAVE_DIR="${REPO_ROOT}/docs/jules/prod-wave-30"');
  lines.push('PROTOCOL_FILE="${REPO_ROOT}/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"');
  lines.push(`TRACKER="${'${REPO_ROOT}'}/.jules-${fileName.replace(".sh", "")}.json"`);
  lines.push(`LOG_FILE="${'${REPO_ROOT}'}/.jules-${fileName.replace(".sh", "")}.log"`);
  lines.push("");
  lines.push('if [[ ! -f "$PROTOCOL_FILE" ]]; then echo "Missing protocol file"; exit 1; fi');
  lines.push('if ! jules remote list --repo >/dev/null 2>&1; then echo "Jules auth missing. Run: jules login"; exit 1; fi');
  lines.push(": > \"$LOG_FILE\"");
  lines.push('echo "{\\"wave\\":\\"' + fileName.replace(".sh", "") + '\\",\\"apps\\":{}}" > "$TRACKER"');
  lines.push("");
  lines.push("dispatch_one() {");
  lines.push("  local app=\"$1\"");
  lines.push('  local spec_file="${WAVE_DIR}/specs/${app}.md"');
  lines.push('  [[ -f "$spec_file" ]] || { echo "Missing spec for ${app}" | tee -a "$LOG_FILE"; return 1; }');
  lines.push("  local spec_content");
  lines.push("  spec_content=$(cat \"$spec_file\")");
  lines.push("  local protocol_content");
  lines.push("  protocol_content=$(cat \"$PROTOCOL_FILE\")");
  lines.push("  local prompt");
  lines.push("  prompt=$(cat <<'PROMPT_EOF'");
  lines.push(promptTemplate);
  lines.push("PROMPT_EOF");
  lines.push(")");
  lines.push("  prompt=\"${prompt//\\$\\{APP_ID\\}/${app}}\"");
  lines.push("  prompt=\"${prompt//\\$\\{SPEC_CONTENT\\}/${spec_content}}\"");
  lines.push("  prompt=\"${prompt//\\$\\{PROTOCOL_CONTENT\\}/${protocol_content}}\"");
  lines.push("  prompt=\"${prompt//\\$\\{REPO\\}/${REPO}}\"");
  lines.push("  local out sid");
  lines.push("  out=$(jules remote new --repo \"$REPO\" --session \"$prompt\" 2>&1) || {");
  lines.push("    echo \"${app}=FAILED\" | tee -a \"$LOG_FILE\";");
  lines.push("    echo \"$out\" | tail -n 10;");
  lines.push("    return 1;");
  lines.push("  }");
  lines.push("  sid=$(echo \"$out\" | grep -Eo '[0-9]{15,}' | head -n1 || true)");
  lines.push("  [[ -n \"$sid\" ]] || sid=\"UNPARSED\"");
  lines.push("  echo \"${app}=${sid}\" | tee -a \"$LOG_FILE\"");
  lines.push("  sleep 15");
  lines.push("}");
  lines.push("");
  for (const app of apps) {
    lines.push(`dispatch_one "${app}"`);
  }
  lines.push("");
  lines.push('echo "Dispatch completed for ' + fileName.replace(".sh", "") + '"');
  lines.push('echo "Log: $LOG_FILE"');
  return lines.join("\n");
}

writeFile(
  path.join(cwd, "scripts/dispatch-prod-wave-30-wave1.sh"),
  buildDispatchScript("dispatch-prod-wave-30-wave1.sh", wave1)
);
writeFile(
  path.join(cwd, "scripts/dispatch-prod-wave-30-wave2.sh"),
  buildDispatchScript("dispatch-prod-wave-30-wave2.sh", wave2)
);

fs.chmodSync(path.join(cwd, "scripts/dispatch-prod-wave-30-wave1.sh"), 0o755);
fs.chmodSync(path.join(cwd, "scripts/dispatch-prod-wave-30-wave2.sh"), 0o755);

console.log(`Generated specs and dispatch scripts for ${targetApps.length} apps.`);
