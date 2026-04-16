package com.microsaas.auditready.service;

import com.microsaas.auditready.domain.AuditReadinessScore;
import com.microsaas.auditready.domain.Control;
import com.microsaas.auditready.domain.EvidenceItem;
import com.microsaas.auditready.repository.AuditReadinessScoreRepository;
import com.microsaas.auditready.repository.ControlRepository;
import com.microsaas.auditready.repository.EvidenceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadinessScoreService {

    private final AuditReadinessScoreRepository scoreRepository;
    private final ControlRepository controlRepository;
    private final EvidenceItemRepository evidenceItemRepository;

    public AuditReadinessScore calculateScore(UUID frameworkId, UUID tenantId) {
        List<Control> controls = controlRepository.findByFrameworkIdAndTenantId(frameworkId, tenantId);
        
        int controlsMet = 0;
        
        for (Control control : controls) {
            List<EvidenceItem> evidenceItems = evidenceItemRepository.findByControlIdAndTenantId(control.getId(), tenantId);
            boolean hasAcceptedEvidence = evidenceItems.stream()
                    .anyMatch(e -> e.getStatus() == EvidenceItem.Status.ACCEPTED);
            
            if (hasAcceptedEvidence) {
                controlsMet++;
            }
        }
        
        int totalControls = controls.size();
        int score = totalControls == 0 ? 0 : (int) Math.round(((double) controlsMet / totalControls) * 100);
        int controlsMissing = totalControls - controlsMet;
        
        AuditReadinessScore readinessScore = AuditReadinessScore.builder()
                .id(UUID.randomUUID())
                .frameworkId(frameworkId)
                .score(score)
                .controlsMet(controlsMet)
                .controlsMissing(controlsMissing)
                .calculatedAt(Instant.now())
                .tenantId(tenantId)
                .build();
                
        return scoreRepository.save(readinessScore);
    }
    
    public Optional<AuditReadinessScore> getLatestScore(UUID frameworkId, UUID tenantId) {
        return scoreRepository.findFirstByFrameworkIdAndTenantIdOrderByCalculatedAtDesc(frameworkId, tenantId);
    }
}
