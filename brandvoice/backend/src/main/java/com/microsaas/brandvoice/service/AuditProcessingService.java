package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.dto.ContentAuditRequest;
import com.microsaas.brandvoice.entity.BrandProfile;
import com.microsaas.brandvoice.entity.ContentAudit;
import com.microsaas.brandvoice.repository.BrandProfileRepository;
import com.microsaas.brandvoice.repository.ContentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditProcessingService {

    private final ContentAuditRepository auditRepository;
    private final BrandProfileRepository profileRepository;

    public ContentAudit submitAudit(UUID tenantId, ContentAuditRequest request) {
        BrandProfile profile = profileRepository.findByIdAndTenantId(request.getBrandProfileId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ContentAudit audit = new ContentAudit();
        audit.setTenantId(tenantId);
        audit.setBrandProfile(profile);
        audit.setContentTitle(request.getContentTitle());
        audit.setContentBody(request.getContentBody());
        audit.setChannel(request.getChannel());
        audit.setStatus("PENDING");

        audit = auditRepository.save(audit);

        // Normally enqueue to pgmq here. For simplicity/testability we'll keep it here,
        // and a worker would pick it up, or we could call Analysis directly for synchronous testing.
        return audit;
    }

    public List<ContentAudit> getAudits(UUID tenantId) {
        return auditRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public ContentAudit getAudit(UUID tenantId, UUID id) {
        return auditRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Audit not found"));
    }
}
