package com.microsaas.securitypulse;

import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScanService {

    private final ScanJobRepository scanJobRepository;
    private final FindingRepository findingRepository;
    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public ScanService(ScanJobRepository scanJobRepository,
                       FindingRepository findingRepository,
                       WebhookService webhookService,
                       ObjectMapper objectMapper) {
        this.scanJobRepository = scanJobRepository;
        this.findingRepository = findingRepository;
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ScanJob createScan(String prUrl, UUID tenantId) {
        ScanJob job = ScanJob.builder()
                .id(UUID.randomUUID())
                .prUrl(prUrl)
                .status("PENDING")
                .createdAt(Instant.now())
                .tenantId(tenantId)
                .build();
        return scanJobRepository.save(job);
    }

    @Transactional
    public List<Finding> runScan(UUID scanJobId, UUID tenantId) {
        ScanJob job = scanJobRepository.findByIdAndTenantId(scanJobId, tenantId)
                .orElseThrow(() -> new RuntimeException("Scan job not found"));

        job.setStatus("RUNNING");
        scanJobRepository.save(job);

        List<Finding> findings = new ArrayList<>();
        
        // Mock SAST scan
        findings.add(Finding.builder()
                .id(UUID.randomUUID())
                .scanJobId(scanJobId)
                .prUrl(job.getPrUrl())
                .tool("SEMGREP")
                .severity("HIGH")
                .message("SQL Injection vulnerability found in UserService.java")
                .status("OPEN")
                .tenantId(tenantId)
                .build());
        
        // Mock secrets scan
        findings.add(Finding.builder()
                .id(UUID.randomUUID())
                .scanJobId(scanJobId)
                .prUrl(job.getPrUrl())
                .tool("TRUFFLEHOG")
                .severity("CRITICAL")
                .message("Hardcoded AWS Access Key found in application.yml")
                .status("OPEN")
                .tenantId(tenantId)
                .build());
        
        findingRepository.saveAll(findings);

        job.setStatus("COMPLETED");
        job.setCompletedAt(Instant.now());
        scanJobRepository.save(job);

        // Emit events
        emitScanCompleted(job);
        findings.forEach(this::emitVulnerabilityFound);

        return findings;
    }

    private void emitScanCompleted(ScanJob job) {
        try {
            webhookService.dispatch(job.getTenantId(), "security-scan-completed",
                    objectMapper.writeValueAsString(job));
        } catch (JsonProcessingException e) {
            // Log error
        }
    }

    private void emitVulnerabilityFound(Finding finding) {
        try {
            webhookService.dispatch(finding.getTenantId(), "vulnerability-found",
                    objectMapper.writeValueAsString(finding));
        } catch (JsonProcessingException e) {
            // Log error
        }
    }
}
