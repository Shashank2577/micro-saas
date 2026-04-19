package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.LiquidityForecast;
import com.microsaas.cashflowai.repository.LiquidityForecastRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiquidityForecastService {
    private final LiquidityForecastRepository repository;
    private final WebhookService webhookService;

    @Transactional(readOnly = true)
    public List<LiquidityForecast> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public LiquidityForecast getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("LiquidityForecast not found"));
    }

    @Transactional
    public LiquidityForecast create(LiquidityForecast entity) {
        entity.setTenantId(TenantContext.require());
        LiquidityForecast saved = repository.save(entity);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.liquidityforecast.updated", saved.toString());
        } catch (Exception e) {
            // Ignore
        }
        return saved;
    }

    @Transactional
    public LiquidityForecast update(UUID id, LiquidityForecast updateData) {
        LiquidityForecast existing = getById(id);
        existing.setName(updateData.getName());
        existing.setStatus(updateData.getStatus());
        existing.setMetadataJson(updateData.getMetadataJson());
        LiquidityForecast saved = repository.save(existing);
        try {
            webhookService.dispatch(TenantContext.require(), "cashflowai.liquidityforecast.updated", saved.toString());
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
        LiquidityForecast entity = getById(id);
        if (entity.getName() == null || entity.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    @Transactional
    public void simulate(UUID id) {
        getById(id);
    }
}
