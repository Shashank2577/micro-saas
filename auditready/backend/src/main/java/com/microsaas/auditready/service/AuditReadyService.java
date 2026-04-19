package com.microsaas.auditready.service;

import com.microsaas.auditready.model.*;
import com.microsaas.auditready.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditReadyService {
    private final ControlFrameworkRepository frameworkRepo;
    private final ControlRepository controlRepo;
    private final EvidenceRepository evidenceRepo;
    private final ComplianceGapRepository gapRepo;
    private final AuditReportRepository reportRepo;
    private final RemediationWorkflowRepository remediationRepo;
    private final AuditTrailRepository trailRepo;
    private final ApplicationEventPublisher eventPublisher;

    private void logAuditTrail(String entityType, UUID entityId, String action) {
        AuditTrail trail = AuditTrail.builder()
            .id(UUID.randomUUID())
            .tenantId(TenantContext.require())
            .entityType(entityType)
            .entityId(entityId)
            .action(action)
            .performedBy("system")
            .timestamp(ZonedDateTime.now())
            .build();
        trailRepo.save(trail);
    }

    // Frameworks
    public List<ControlFramework> getFrameworks() {
        return frameworkRepo.findByTenantId(TenantContext.require());
    }
    public ControlFramework getFramework(UUID id) {
        return frameworkRepo.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
    }
    @Transactional
    public ControlFramework createFramework(ControlFramework framework) {
        framework.setId(UUID.randomUUID());
        framework.setTenantId(TenantContext.require());
        framework.setCreatedAt(ZonedDateTime.now());
        framework.setUpdatedAt(ZonedDateTime.now());
        ControlFramework saved = frameworkRepo.save(framework);
        logAuditTrail("ControlFramework", saved.getId(), "CREATE");
        return saved;
    }

    // Controls
    public List<Control> getControls() {
        return controlRepo.findByTenantId(TenantContext.require());
    }
    @Transactional
    public Control createControl(Control control) {
        control.setId(UUID.randomUUID());
        control.setTenantId(TenantContext.require());
        control.setCreatedAt(ZonedDateTime.now());
        control.setUpdatedAt(ZonedDateTime.now());
        Control saved = controlRepo.save(control);
        logAuditTrail("Control", saved.getId(), "CREATE");
        return saved;
    }
    @Transactional
    public Control updateControl(UUID id, Control controlDetails) {
        Control control = controlRepo.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
        control.setName(controlDetails.getName());
        control.setDescription(controlDetails.getDescription());
        control.setStatus(controlDetails.getStatus());
        control.setUpdatedAt(ZonedDateTime.now());
        Control saved = controlRepo.save(control);
        logAuditTrail("Control", saved.getId(), "UPDATE");
        return saved;
    }

    // Evidence
    public List<Evidence> getEvidences() {
        return evidenceRepo.findByTenantId(TenantContext.require());
    }
    public Evidence getEvidence(UUID id) {
        return evidenceRepo.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
    }
    @Transactional
    public Evidence createEvidence(Evidence evidence) {
        evidence.setId(UUID.randomUUID());
        evidence.setTenantId(TenantContext.require());
        evidence.setCollectedAt(ZonedDateTime.now());
        Evidence saved = evidenceRepo.save(evidence);
        logAuditTrail("Evidence", saved.getId(), "CREATE");
        
        // Emit domain event manually since the event class is removed
        // We will just skip event emission or emit a simple object for now if it doesn't exist
        
        return saved;
    }

    // Gaps
    public List<ComplianceGap> getGaps() {
        return gapRepo.findByTenantId(TenantContext.require());
    }
    @Transactional
    public ComplianceGap createGap(ComplianceGap gap) {
        gap.setId(UUID.randomUUID());
        gap.setTenantId(TenantContext.require());
        gap.setDetectedAt(ZonedDateTime.now());
        ComplianceGap saved = gapRepo.save(gap);
        logAuditTrail("ComplianceGap", saved.getId(), "CREATE");
        return saved;
    }
    @Transactional
    public ComplianceGap updateGap(UUID id, ComplianceGap gapDetails) {
        ComplianceGap gap = gapRepo.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
        gap.setDescription(gapDetails.getDescription());
        gap.setSeverity(gapDetails.getSeverity());
        gap.setStatus(gapDetails.getStatus());
        ComplianceGap saved = gapRepo.save(gap);
        logAuditTrail("ComplianceGap", saved.getId(), "UPDATE");
        return saved;
    }

    private final LiteLLMClient liteLLMClient;

    // Reports
    public List<AuditReport> getReports() {
        return reportRepo.findByTenantId(TenantContext.require());
    }
    @Transactional
    public AuditReport createReport(AuditReport report) {
        report.setId(UUID.randomUUID());
        report.setTenantId(TenantContext.require());
        report.setGeneratedAt(ZonedDateTime.now());
        
        // Calculate score
        List<Control> controls = controlRepo.findByTenantIdAndFrameworkId(report.getTenantId(), report.getFrameworkId());
        long implemented = controls.stream().filter(c -> "IMPLEMENTED".equals(c.getStatus())).count();
        double score = controls.isEmpty() ? 0.0 : (double) implemented / controls.size() * 100;
        report.setReadinessScore(score);
        
        // Generate AI Summary
        String prompt = String.format("Generate a brief audit summary for framework ID %s with readiness score %.2f%%. There are %d implemented controls.",
            report.getFrameworkId(), score, implemented);
        String summary = liteLLMClient.askAi(prompt);
        report.setSummary(summary);
        
        AuditReport saved = reportRepo.save(report);
        logAuditTrail("AuditReport", saved.getId(), "CREATE");
        
        return saved;
    }

    // Remediations
    public List<RemediationWorkflow> getRemediations() {
        return remediationRepo.findByTenantId(TenantContext.require());
    }
    @Transactional
    public RemediationWorkflow createRemediation(RemediationWorkflow remediation) {
        remediation.setId(UUID.randomUUID());
        remediation.setTenantId(TenantContext.require());
        RemediationWorkflow saved = remediationRepo.save(remediation);
        logAuditTrail("RemediationWorkflow", saved.getId(), "CREATE");
        return saved;
    }
    @Transactional
    public RemediationWorkflow updateRemediation(UUID id, RemediationWorkflow remediationDetails) {
        RemediationWorkflow remediation = remediationRepo.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
        remediation.setTitle(remediationDetails.getTitle());
        remediation.setDescription(remediationDetails.getDescription());
        remediation.setAssignedTo(remediationDetails.getAssignedTo());
        remediation.setStatus(remediationDetails.getStatus());
        remediation.setDueDate(remediationDetails.getDueDate());
        RemediationWorkflow saved = remediationRepo.save(remediation);
        logAuditTrail("RemediationWorkflow", saved.getId(), "UPDATE");
        return saved;
    }

    // Audit Trails
    public List<AuditTrail> getAuditTrails() {
        return trailRepo.findByTenantId(TenantContext.require());
    }
}
