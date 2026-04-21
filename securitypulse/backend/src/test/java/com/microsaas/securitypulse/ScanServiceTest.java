package com.microsaas.securitypulse;

import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScanServiceTest {

    @Mock
    private ScanJobRepository scanJobRepository;
    @Mock
    private FindingRepository findingRepository;
    @Mock
    private PolicyDecisionRepository policyDecisionRepository;
    @Mock
    private WebhookService webhookService;

    private ScanService scanService;
    private PolicyEngine policyEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scanService = new ScanService(scanJobRepository, findingRepository, webhookService, objectMapper);
        policyEngine = new PolicyEngine(policyDecisionRepository, webhookService, objectMapper);
    }

    @Test
    void testCreateScan() {
        UUID tenantId = UUID.randomUUID();
        String prUrl = "https://github.com/org/repo/pull/1";
        
        when(scanJobRepository.save(any(ScanJob.class))).thenAnswer(i -> i.getArguments()[0]);

        ScanJob job = scanService.createScan(prUrl, tenantId);

        assertNotNull(job);
        assertEquals(prUrl, job.getPrUrl());
        assertEquals(tenantId, job.getTenantId());
        assertEquals("PENDING", job.getStatus());
        verify(scanJobRepository).save(any(ScanJob.class));
    }

    @Test
    void testRunScan() {
        UUID tenantId = UUID.randomUUID();
        UUID scanJobId = UUID.randomUUID();
        ScanJob job = ScanJob.builder()
                .id(scanJobId)
                .prUrl("url")
                .tenantId(tenantId)
                .build();

        when(scanJobRepository.findByIdAndTenantId(scanJobId, tenantId)).thenReturn(Optional.of(job));
        when(scanJobRepository.save(any(ScanJob.class))).thenAnswer(i -> i.getArguments()[0]);

        List<Finding> findings = scanService.runScan(scanJobId, tenantId);
        
        assertNotNull(findings);
        assertEquals(2, findings.size());
        assertEquals("COMPLETED", job.getStatus());
        
        verify(findingRepository).saveAll(anyList());
        verify(webhookService, atLeastOnce()).dispatch(eq(tenantId), anyString(), anyString());
    }
    
    @Test
    void testPolicyEngine() {
        UUID tenantId = UUID.randomUUID();
        UUID scanJobId = UUID.randomUUID();
        
        Finding finding = Finding.builder()
                .id(UUID.randomUUID())
                .severity("CRITICAL")
                .tool("SEMGREP")
                .tenantId(tenantId)
                .build();

        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .name("Block Critical")
                .rule("CRITICAL")
                .action("BLOCK")
                .tenantId(tenantId)
                .build();
        
        when(policyDecisionRepository.save(any(PolicyDecision.class))).thenAnswer(i -> i.getArguments()[0]);
        
        PolicyDecision decision = policyEngine.evaluate(scanJobId, List.of(finding), List.of(policy), tenantId);
        
        assertEquals("BLOCK", decision.getDecision());
        verify(webhookService).dispatch(eq(tenantId), eq("policy-violation"), anyString());
    }
}
