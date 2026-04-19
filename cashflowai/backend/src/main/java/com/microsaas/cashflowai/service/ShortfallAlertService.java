package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.ShortfallAlert;
import com.microsaas.cashflowai.repository.ShortfallAlertRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortfallAlertService {
    private final ShortfallAlertRepository repository;
    private final WebhookService webhookService;

    @Transactional(readOnly = true)
    public List<ShortfallAlert> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public ShortfallAlert getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ShortfallAlert not found"));
    }

    @Transactional
    public ShortfallAlert create(ShortfallAlert entity) {
        entity.setTenantId(TenantContext.require());
        ShortfallAlert saved = repository.save(entity);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.shortfall.detected", saved.toString());
        } catch (Exception e) {
            // Ignore
        }
        return saved;
    }

    @Transactional
    public ShortfallAlert update(UUID id, ShortfallAlert updateData) {
        ShortfallAlert existing = getById(id);
        existing.setName(updateData.getName());
        existing.setStatus(updateData.getStatus());
        existing.setMetadataJson(updateData.getMetadataJson());
        ShortfallAlert saved = repository.save(existing);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.shortfall.detected", saved.toString());
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
        ShortfallAlert entity = getById(id);
        if (entity.getName() == null || entity.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    @Transactional
    public void simulate(UUID id) {
        getById(id);
    }
}
