package com.microsaas.complianceradar.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.JurisdictionRule;
import com.microsaas.complianceradar.repository.JurisdictionRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NormalizationService {

    private final JurisdictionRuleRepository repository;
    private final AiService aiService;

    @Transactional(readOnly = true)
    public List<JurisdictionRule> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public JurisdictionRule getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("JurisdictionRule not found"));
    }

    @Transactional
    public JurisdictionRule create(JurisdictionRule rule) {
        rule.setId(UUID.randomUUID());
        rule.setTenantId(TenantContext.require());
        rule.setCreatedAt(Instant.now());
        return repository.save(rule);
    }

    @Transactional
    public JurisdictionRule update(UUID id, JurisdictionRule updateDetails) {
        JurisdictionRule existing = getById(id);
        existing.setName(updateDetails.getName());
        existing.setStatus(updateDetails.getStatus());
        existing.setMetadataJson(updateDetails.getMetadataJson());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteByIdAndTenantId(id, TenantContext.require());
    }

    @Transactional
    public void validate(UUID id) {
        JurisdictionRule existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        JurisdictionRule existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    public String analyzeText(String text) {
        return aiService.chat(new com.crosscutting.starter.ai.ChatRequest("claude-sonnet-4-6", java.util.List.of(new com.crosscutting.starter.ai.ChatMessage("user", "Normalize the following regulatory text into rules: " + text)), 0.7, 1000)).content();
    }
}
