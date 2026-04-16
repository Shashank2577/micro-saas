package com.microsaas.securitypulse;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ScanServiceTest {

    @Test
    void testScanReturnsFindings() {
        ScanService scanService = new ScanService();
        UUID tenantId = UUID.randomUUID();
        String prUrl = "https://github.com/org/repo/pull/1";
        
        List<Finding> findings = scanService.scan(prUrl, tenantId);
        
        assertNotNull(findings);
        assertEquals(2, findings.size());
        
        // Verify mock contents
        assertTrue(findings.stream().anyMatch(f -> f.getTool().equals("SEMGREP")));
        assertTrue(findings.stream().anyMatch(f -> f.getTool().equals("TRUFFLEHOG")));
    }
    
    @Test
    void testPolicyEngine() {
        PolicyEngine engine = new PolicyEngine();
        UUID tenantId = UUID.randomUUID();
        
        Finding finding = new Finding(UUID.randomUUID(), "url", "SEMGREP", "CRITICAL", "msg", "OPEN", tenantId);
        Policy policy = new Policy(UUID.randomUUID(), "Block Critical", "CRITICAL", "BLOCK", tenantId);
        
        String decision = engine.evaluate(List.of(finding), List.of(policy));
        
        assertEquals("BLOCK", decision);
        
        Finding lowFinding = new Finding(UUID.randomUUID(), "url", "SEMGREP", "LOW", "msg", "OPEN", tenantId);
        String allowDecision = engine.evaluate(List.of(lowFinding), List.of(policy));
        
        assertEquals("ALLOW", allowDecision);
    }
}
