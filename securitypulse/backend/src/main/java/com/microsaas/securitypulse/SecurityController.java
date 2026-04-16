package com.microsaas.securitypulse;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class SecurityController {

    private final FindingRepository findingRepository;
    private final PolicyRepository policyRepository;
    private final ScanService scanService;
    private final PolicyEngine policyEngine;

    public SecurityController(FindingRepository findingRepository, PolicyRepository policyRepository, ScanService scanService, PolicyEngine policyEngine) {
        this.findingRepository = findingRepository;
        this.policyRepository = policyRepository;
        this.scanService = scanService;
        this.policyEngine = policyEngine;
    }

    @PostMapping("/scan")
    public ScanResult scan(@RequestBody Map<String, String> payload, @RequestHeader(value = "X-Tenant-ID", required = false) UUID tenantId) {
        if (tenantId == null) {
            tenantId = UUID.randomUUID(); // Fallback for testing if not provided
        }
        
        String prUrl = payload.get("prUrl");
        List<Finding> findings = scanService.scan(prUrl, tenantId);
        
        // Save findings
        findingRepository.saveAll(findings);
        
        // Get policies for tenant
        List<Policy> policies = policyRepository.findByTenantId(tenantId);
        
        // Evaluate
        String decision = policyEngine.evaluate(findings, policies);
        
        return new ScanResult(findings, decision);
    }

    @GetMapping("/findings")
    public List<Finding> getFindings(@RequestHeader(value = "X-Tenant-ID", required = false) UUID tenantId) {
        if (tenantId == null) {
            return findingRepository.findAll();
        }
        return findingRepository.findByTenantId(tenantId);
    }

    @PostMapping("/policies")
    public Policy createPolicy(@RequestBody Policy policy, @RequestHeader(value = "X-Tenant-ID", required = false) UUID tenantId) {
        if (policy.getId() == null) {
            policy.setId(UUID.randomUUID());
        }
        if (policy.getTenantId() == null && tenantId != null) {
            policy.setTenantId(tenantId);
        } else if (policy.getTenantId() == null) {
            policy.setTenantId(UUID.randomUUID()); // Fallback
        }
        return policyRepository.save(policy);
    }
    
    public static class ScanResult {
        private List<Finding> findings;
        private String policyDecision;
        
        public ScanResult(List<Finding> findings, String policyDecision) {
            this.findings = findings;
            this.policyDecision = policyDecision;
        }
        
        public List<Finding> getFindings() { return findings; }
        public String getPolicyDecision() { return policyDecision; }
    }
}
