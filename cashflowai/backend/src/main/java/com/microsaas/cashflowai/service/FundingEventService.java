package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.FundingEvent;
import com.microsaas.cashflowai.repository.FundingEventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundingEventService {
    private final FundingEventRepository repository;
    private final WebhookService webhookService;

    @Transactional(readOnly = true)
    public List<FundingEvent> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public FundingEvent getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("FundingEvent not found"));
    }

    @Transactional
    public FundingEvent create(FundingEvent entity) {
        entity.setTenantId(TenantContext.require());
        FundingEvent saved = repository.save(entity);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.fundingevent.updated", saved.toString());
        } catch (Exception e) {
            // Ignore
        }
        return saved;
    }

    @Transactional
    public FundingEvent update(UUID id, FundingEvent updateData) {
        FundingEvent existing = getById(id);
        existing.setName(updateData.getName());
        existing.setStatus(updateData.getStatus());
        existing.setMetadataJson(updateData.getMetadataJson());
        FundingEvent saved = repository.save(existing);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.fundingevent.updated", saved.toString());
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
        FundingEvent entity = getById(id);
        if (entity.getName() == null || entity.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    @Transactional
    public void simulate(UUID id) {
        getById(id);
    }
}
