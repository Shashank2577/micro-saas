package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.ecosystemmap.entity.Connection;
import com.microsaas.ecosystemmap.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final EcosystemService ecosystemService;
    private final NodeService nodeService;
    private final WebhookService webhookService;

    public List<Connection> getConnectionsByEcosystem(UUID ecosystemId) {
        return connectionRepository.findByTenantIdAndEcosystemId(TenantContext.require().toString(), ecosystemId);
    }

    public Connection getConnectionById(UUID id) {
        return connectionRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Connection not found"));
    }

    @Transactional
    public Connection createConnection(UUID ecosystemId, UUID sourceNodeId, UUID targetNodeId, Connection connection) {
        connection.setTenantId(TenantContext.require().toString());
        connection.setEcosystem(ecosystemService.getEcosystemById(ecosystemId));
        connection.setSourceNode(nodeService.getNodeById(sourceNodeId));
        connection.setTargetNode(nodeService.getNodeById(targetNodeId));
        Connection saved = connectionRepository.save(connection);
        webhookService.dispatch(TenantContext.require(), "connection.created", saved.getId().toString());
        return saved;
    }

    @Transactional
    public Connection updateConnection(UUID id, Connection updateData) {
        Connection existing = getConnectionById(id);
        existing.setConnectionType(updateData.getConnectionType());
        existing.setStatus(updateData.getStatus());
        Connection updated = connectionRepository.save(existing);
        webhookService.dispatch(TenantContext.require(), "connection.updated", updated.getId().toString());
        return updated;
    }

    @Transactional
    public void deleteConnection(UUID id) {
        Connection existing = getConnectionById(id);
        connectionRepository.delete(existing);
        webhookService.dispatch(TenantContext.require(), "connection.deleted", existing.getId().toString());
    }
}
