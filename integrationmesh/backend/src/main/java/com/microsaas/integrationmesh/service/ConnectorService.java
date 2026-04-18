package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.dto.ConnectorDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.repository.ConnectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConnectorService {
    private final ConnectorRepository connectorRepository;

    public ConnectorService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    public List<Connector> listConnectors() {
        UUID tenantId = TenantContext.require();
        return connectorRepository.findByTenantId(tenantId);
    }

    public Connector createConnector(ConnectorDto dto) {
        UUID tenantId = TenantContext.require();
        Connector connector = new Connector();
        connector.setTenantId(tenantId);
        connector.setName(dto.getName());
        connector.setType(dto.getType());
        connector.setConfig(dto.getConfig());
        connector.setStatus("ACTIVE");
        return connectorRepository.save(connector);
    }

    public Connector getConnector(UUID id) {
        UUID tenantId = TenantContext.require();
        return connectorRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Connector not found"));
    }
}
