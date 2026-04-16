package com.microsaas.securitypulse;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScanService {

    public List<Finding> scan(String prUrl, UUID tenantId) {
        List<Finding> findings = new ArrayList<>();
        
        // Mock SAST scan
        findings.add(new Finding(UUID.randomUUID(), prUrl, "SEMGREP", "HIGH", "SQL Injection vulnerability found in UserService.java", "OPEN", tenantId));
        
        // Mock secrets scan
        findings.add(new Finding(UUID.randomUUID(), prUrl, "TRUFFLEHOG", "CRITICAL", "Hardcoded AWS Access Key found in application.yml", "OPEN", tenantId));
        
        return findings;
    }
}
