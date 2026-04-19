package com.microsaas.auditready.service;

import com.microsaas.auditready.model.*;
import com.microsaas.auditready.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditReadyServiceTest {

    @Mock private ControlFrameworkRepository frameworkRepo;
    @Mock private ControlRepository controlRepo;
    @Mock private EvidenceRepository evidenceRepo;
    @Mock private ComplianceGapRepository gapRepo;
    @Mock private AuditReportRepository reportRepo;
    @Mock private RemediationWorkflowRepository remediationRepo;
    @Mock private AuditTrailRepository trailRepo;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private LiteLLMClient liteLLMClient;

    @InjectMocks
    private AuditReadyService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void createFramework_ShouldSaveAndLogTrail() {
        ControlFramework framework = new ControlFramework();
        framework.setName("SOC 2");

        when(frameworkRepo.save(any(ControlFramework.class))).thenAnswer(i -> i.getArguments()[0]);
        when(trailRepo.save(any(AuditTrail.class))).thenAnswer(i -> i.getArguments()[0]);

        ControlFramework saved = service.createFramework(framework);

        assertNotNull(saved.getId());
        assertEquals(tenantId, saved.getTenantId());
        assertEquals("SOC 2", saved.getName());
        assertNotNull(saved.getCreatedAt());

        verify(frameworkRepo).save(any(ControlFramework.class));
        verify(trailRepo).save(any(AuditTrail.class));
    }

    @Test
    void createReport_ShouldCalculateScore() {
        UUID frameworkId = UUID.randomUUID();
        AuditReport report = new AuditReport();
        report.setFrameworkId(frameworkId);

        Control c1 = new Control(); c1.setStatus("IMPLEMENTED");
        Control c2 = new Control(); c2.setStatus("NOT_IMPLEMENTED");
        
        when(controlRepo.findByTenantIdAndFrameworkId(tenantId, frameworkId)).thenReturn(List.of(c1, c2));
        when(liteLLMClient.askAi(anyString())).thenReturn("AI Summary");
        when(reportRepo.save(any(AuditReport.class))).thenAnswer(i -> i.getArguments()[0]);
        when(trailRepo.save(any(AuditTrail.class))).thenAnswer(i -> i.getArguments()[0]);

        AuditReport saved = service.createReport(report);

        assertEquals(50.0, saved.getReadinessScore());
        assertEquals("AI Summary", saved.getSummary());
        verify(reportRepo).save(any());
        verify(trailRepo).save(any());
    }
}
