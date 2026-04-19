package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.SpendControlRuleRequest;
import com.microsaas.procurebot.model.SpendControlRule;
import com.microsaas.procurebot.repository.SpendControlRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpendControlRuleService {

    private final SpendControlRuleRepository repository;

    @Transactional(readOnly = true)
    public List<SpendControlRule> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public SpendControlRule findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("SpendControlRule not found"));
    }

    @Transactional
    public SpendControlRule create(UUID tenantId, SpendControlRuleRequest request) {
        SpendControlRule entity = SpendControlRule.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public SpendControlRule update(UUID id, UUID tenantId, SpendControlRuleRequest request) {
        SpendControlRule entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        SpendControlRule entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
