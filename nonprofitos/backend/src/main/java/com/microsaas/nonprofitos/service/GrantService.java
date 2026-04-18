package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Grant;
import com.microsaas.nonprofitos.dto.GrantDto;
import com.microsaas.nonprofitos.repository.GrantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GrantService {

    private final GrantRepository grantRepository;
    private final AiService aiService;

    public GrantService(GrantRepository grantRepository, AiService aiService) {
        this.grantRepository = grantRepository;
        this.aiService = aiService;
    }

    public List<Grant> getAllGrants() {
        UUID tenantId = TenantContext.require();
        return grantRepository.findByTenantId(tenantId);
    }

    public Grant createGrant(GrantDto dto) {
        UUID tenantId = TenantContext.require();
        Grant grant = new Grant();
        grant.setTenantId(tenantId);
        grant.setTitle(dto.getTitle());
        grant.setFunder(dto.getFunder());
        grant.setAmount(dto.getAmount());
        grant.setDeadline(dto.getDeadline());
        grant.setStatus(dto.getStatus() == null ? "DRAFT" : dto.getStatus());
        return grantRepository.save(grant);
    }

    public String generateDraft(UUID id) {
        UUID tenantId = TenantContext.require();
        Grant grant = grantRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Grant not found"));

        String draft = aiService.generateGrantDraft(grant.getTitle(), grant.getFunder());
        
        grant.setDraftContent(draft);
        grantRepository.save(grant);
        
        return draft;
    }
}
