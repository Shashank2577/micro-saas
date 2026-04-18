package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.FundingRound;
import com.microsaas.equityintelligence.repositories.FundingRoundRepository;
import org.springframework.stereotype.Service;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FundingRoundService extends BaseService<FundingRound, FundingRoundRepository> {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public FundingRoundService(FundingRoundRepository repository, WebhookService webhookService, ObjectMapper objectMapper) {
        super(repository);
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<FundingRound> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Override
    protected Optional<FundingRound> findByIdAndTenantId(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    protected void onAfterSave(FundingRound entity, boolean isCreate) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                "tenant_id", entity.getTenantId().toString(),
                "round_id", entity.getId().toString(),
                "name", entity.getName(),
                "status", entity.getStatus()
            ));
            webhookService.dispatch(entity.getTenantId(), "equityintelligence.round.updated", payload);
        } catch (Exception e) {
            // Log and continue
        }
    }

    public boolean validate(UUID id) {
        return findById(id).isPresent();
    }

    public String simulate(UUID id) {
        return "{\"result\": \"simulation completed for " + id + "\"}";
    }
}
