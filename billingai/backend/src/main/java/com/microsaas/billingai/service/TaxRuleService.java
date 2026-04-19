package com.microsaas.billingai.service;

import com.microsaas.billingai.model.TaxRule;
import com.microsaas.billingai.repository.TaxRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxRuleService {
    private final TaxRuleRepository repository;

    public List<TaxRule> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public TaxRule findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(rule -> rule.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Tax Rule not found"));
    }

    public TaxRule create(UUID tenantId, TaxRule rule) {
        rule.setId(UUID.randomUUID());
        rule.setTenantId(tenantId);
        rule.setCreatedAt(OffsetDateTime.now());
        rule.setUpdatedAt(OffsetDateTime.now());
        return repository.save(rule);
    }

    public TaxRule update(UUID tenantId, UUID id, TaxRule ruleUpdate) {
        TaxRule existing = findById(tenantId, id);
        existing.setName(ruleUpdate.getName());
        existing.setStatus(ruleUpdate.getStatus());
        existing.setMetadataJson(ruleUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }
}
