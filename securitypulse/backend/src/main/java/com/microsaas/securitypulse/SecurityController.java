package com.microsaas.securitypulse;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SecurityController {

    private final ScanJobRepository scanJobRepository;
    private final FindingRepository findingRepository;
    private final PolicyRepository policyRepository;
    private final PolicyDecisionRepository policyDecisionRepository;
    private final TriagedReportRepository triagedReportRepository;
    private final ScanService scanService;
    private final PolicyEngine policyEngine;

    public SecurityController(ScanJobRepository scanJobRepository,
                              FindingRepository findingRepository,
                              PolicyRepository policyRepository,
                              PolicyDecisionRepository policyDecisionRepository,
                              TriagedReportRepository triagedReportRepository,
                              ScanService scanService,
                              PolicyEngine policyEngine) {
        this.scanJobRepository = scanJobRepository;
        this.findingRepository = findingRepository;
        this.policyRepository = policyRepository;
        this.policyDecisionRepository = policyDecisionRepository;
        this.triagedReportRepository = triagedReportRepository;
        this.scanService = scanService;
        this.policyEngine = policyEngine;
    }

    @PostMapping("/scans")
    public ScanJob triggerScan(@RequestBody Map<String, String> payload) {
        UUID tenantId = TenantContext.require();
        String prUrl = payload.get("prUrl");
        ScanJob job = scanService.createScan(prUrl, tenantId);
        // In a real app, this would be async. For the stub, we run it synchronously.
        scanService.runScan(job.getId(), tenantId);
        return scanJobRepository.findById(job.getId()).orElse(job);
    }

    @GetMapping("/scans/{id}")
    public ScanJob getScan(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return scanJobRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Scan job not found"));
    }

    @GetMapping("/scans/{id}/findings")
    public List<Finding> getScanFindings(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return findingRepository.findByScanJobIdAndTenantId(id, tenantId);
    }

    @PostMapping("/scans/{id}/triage")
    public TriagedReport triageScan(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        List<Finding> findings = findingRepository.findByScanJobIdAndTenantId(id, tenantId);

        TriagedReport report = TriagedReport.builder()
                .id(UUID.randomUUID())
                .scanJobId(id)
                .summary("Triaged " + findings.size() + " findings.")
                .priorityLevel(findings.stream().anyMatch(f -> "CRITICAL".equals(f.getSeverity())) ? "URGENT" : "NORMAL")
                .tenantId(tenantId)
                .build();

        return triagedReportRepository.save(report);
    }

    @PostMapping("/policies")
    public Policy createPolicy(@RequestBody Policy policy) {
        UUID tenantId = TenantContext.require();
        policy.setId(UUID.randomUUID());
        policy.setTenantId(tenantId);
        return policyRepository.save(policy);
    }

    @GetMapping("/policies")
    public List<Policy> listPolicies() {
        UUID tenantId = TenantContext.require();
        return policyRepository.findByTenantId(tenantId);
    }

    @PostMapping("/policies/enforce")
    public PolicyDecision enforcePolicy(@RequestBody Map<String, UUID> payload) {
        UUID tenantId = TenantContext.require();
        UUID scanJobId = payload.get("scanJobId");
        
        List<Finding> findings = findingRepository.findByScanJobIdAndTenantId(scanJobId, tenantId);
        List<Policy> policies = policyRepository.findByTenantId(tenantId);
        
        return policyEngine.evaluate(scanJobId, findings, policies, tenantId);
    }
}
