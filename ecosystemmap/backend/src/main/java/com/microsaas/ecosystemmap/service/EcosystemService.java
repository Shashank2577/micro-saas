package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.ecosystemmap.entity.Ecosystem;
import com.microsaas.ecosystemmap.repository.EcosystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EcosystemService {

    private final EcosystemRepository ecosystemRepository;
    private final WebhookService webhookService;

    public List<Ecosystem> getAllEcosystems() {
        return ecosystemRepository.findByTenantId(TenantContext.require().toString());
    }

    public Ecosystem getEcosystemById(UUID id) {
        return ecosystemRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Ecosystem not found"));
    }

    @Transactional
    public Ecosystem createEcosystem(Ecosystem ecosystem) {
        ecosystem.setTenantId(TenantContext.require().toString());
        Ecosystem saved = ecosystemRepository.save(ecosystem);
        webhookService.dispatch(TenantContext.require(), "ecosystem.created", saved.getId().toString());
        return saved;
    }

    @Transactional
    public Ecosystem updateEcosystem(UUID id, Ecosystem updateData) {
        Ecosystem existing = getEcosystemById(id);
        existing.setName(updateData.getName());
        existing.setDescription(updateData.getDescription());
        Ecosystem updated = ecosystemRepository.save(existing);
        webhookService.dispatch(TenantContext.require(), "ecosystem.updated", updated.getId().toString());
        return updated;
    }

    @Transactional
    public void deleteEcosystem(UUID id) {
        Ecosystem existing = getEcosystemById(id);
        ecosystemRepository.delete(existing);
        webhookService.dispatch(TenantContext.require(), "ecosystem.deleted", existing.getId().toString());
    }
}
