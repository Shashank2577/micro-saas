package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.MitigationOption;
import com.microsaas.cashflowai.repository.MitigationOptionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MitigationOptionService {
    private final MitigationOptionRepository repository;
    private final WebhookService webhookService;

    @Transactional(readOnly = true)
    public List<MitigationOption> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public MitigationOption getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("MitigationOption not found"));
    }

    @Transactional
    public MitigationOption create(MitigationOption entity) {
        entity.setTenantId(TenantContext.require());
        MitigationOption saved = repository.save(entity);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.mitigation.recommended", saved.toString());
        } catch (Exception e) {
            // Ignore
        }
        return saved;
    }

    @Transactional
    public MitigationOption update(UUID id, MitigationOption updateData) {
        MitigationOption existing = getById(id);
        existing.setName(updateData.getName());
        existing.setStatus(updateData.getStatus());
        existing.setMetadataJson(updateData.getMetadataJson());
        MitigationOption saved = repository.save(existing);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.mitigation.recommended", saved.toString());
        } catch (Exception e) {
            // Ignore
        }
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getById(id));
    }

    @Transactional
    public void validate(UUID id) {
        MitigationOption entity = getById(id);
        if (entity.getName() == null || entity.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    @Transactional
    public void simulate(UUID id) {
        getById(id);
    }
}
