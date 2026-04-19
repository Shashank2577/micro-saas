package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.VestingSchedule;
import com.microsaas.equityintelligence.repositories.VestingScheduleRepository;
import org.springframework.stereotype.Service;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VestingScheduleService extends BaseService<VestingSchedule, VestingScheduleRepository> {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public VestingScheduleService(VestingScheduleRepository repository, WebhookService webhookService, ObjectMapper objectMapper) {
        super(repository);
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<VestingSchedule> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Override
    protected Optional<VestingSchedule> findByIdAndTenantId(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    protected void onAfterSave(VestingSchedule entity, boolean isCreate) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                "tenant_id", entity.getTenantId().toString(),
                "schedule_id", entity.getId().toString(),
                "alert_type", isCreate ? "CREATED" : "UPDATED"
            ));
            webhookService.dispatch(entity.getTenantId(), "equityintelligence.vesting.alerted", payload);
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
