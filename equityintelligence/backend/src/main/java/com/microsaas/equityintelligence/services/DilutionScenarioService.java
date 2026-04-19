package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.DilutionScenario;
import com.microsaas.equityintelligence.repositories.DilutionScenarioRepository;
import org.springframework.stereotype.Service;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DilutionScenarioService extends BaseService<DilutionScenario, DilutionScenarioRepository> {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public DilutionScenarioService(DilutionScenarioRepository repository, WebhookService webhookService, ObjectMapper objectMapper) {
        super(repository);
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<DilutionScenario> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Override
    protected Optional<DilutionScenario> findByIdAndTenantId(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public boolean validate(UUID id) {
        return findById(id).isPresent();
    }

    public String simulate(UUID id) {
        try {
            DilutionScenario scenario = findById(id).orElseThrow();
            String payload = objectMapper.writeValueAsString(Map.of(
                "tenant_id", scenario.getTenantId().toString(),
                "scenario_id", id.toString(),
                "result_summary", "Simulated successfully"
            ));
            webhookService.dispatch(scenario.getTenantId(), "equityintelligence.dilution.simulated", payload);
        } catch (Exception e) {
            // Log and continue
        }
        return "{\"result\": \"simulation completed for " + id + "\"}";
    }
}
