package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.dto.IntegrationDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.model.Integration;
import com.microsaas.integrationmesh.repository.ConnectorRepository;
import com.microsaas.integrationmesh.repository.IntegrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IntegrationService {
    private final IntegrationRepository integrationRepository;
    private final ConnectorRepository connectorRepository;

    public IntegrationService(IntegrationRepository integrationRepository, ConnectorRepository connectorRepository) {
        this.integrationRepository = integrationRepository;
        this.connectorRepository = connectorRepository;
    }

    public List<Integration> listIntegrations() {
        UUID tenantId = TenantContext.require();
        return integrationRepository.findByTenantId(tenantId);
    }

    public Integration getIntegration(UUID id) {
        UUID tenantId = TenantContext.require();
        return integrationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Integration not found"));
    }

    public Integration createIntegration(IntegrationDto dto) {
        UUID tenantId = TenantContext.require();
        Connector source = connectorRepository.findByIdAndTenantId(dto.getSourceConnectorId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Source connector not found"));
        Connector target = connectorRepository.findByIdAndTenantId(dto.getTargetConnectorId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Target connector not found"));

        Integration integration = new Integration();
        integration.setTenantId(tenantId);
        integration.setName(dto.getName());
        integration.setSourceConnector(source);
        integration.setTargetConnector(target);
        integration.setStatus("PAUSED");

        return integrationRepository.save(integration);
    }

    public Integration updateStatus(UUID id, String status) {
        Integration integration = getIntegration(id);
        integration.setStatus(status);
        return integrationRepository.save(integration);
    }
}
