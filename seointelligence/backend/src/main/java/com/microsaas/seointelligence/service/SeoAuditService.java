package com.microsaas.seointelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.seointelligence.dto.SeoAuditRequest;
import com.microsaas.seointelligence.model.SeoAudit;
import com.microsaas.seointelligence.repository.SeoAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeoAuditService {

    private final SeoAuditRepository seoAuditRepository;
    private final WebhookService webhookService;

    public List<SeoAudit> listAudits() {
        String tenantId = TenantContext.require().toString();
        return seoAuditRepository.findByTenantId(tenantId);
    }

    public SeoAudit createAudit(SeoAuditRequest request) {
        UUID tenantId = TenantContext.require();
        SeoAudit audit = SeoAudit.builder()
                .tenantId(tenantId.toString())
                .url(request.getUrl())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        SeoAudit savedAudit = seoAuditRepository.save(audit);
        webhookService.dispatch(tenantId, "seointelligence.audit.created", "{\"auditId\": \"" + savedAudit.getId() + "\"}");
        return savedAudit;
    }
}
