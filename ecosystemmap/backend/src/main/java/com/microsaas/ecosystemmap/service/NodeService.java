package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.ecosystemmap.entity.Node;
import com.microsaas.ecosystemmap.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository nodeRepository;
    private final EcosystemService ecosystemService;
    private final WebhookService webhookService;

    public List<Node> getNodesByEcosystem(UUID ecosystemId) {
        return nodeRepository.findByTenantIdAndEcosystemId(TenantContext.require().toString(), ecosystemId);
    }

    public Node getNodeById(UUID id) {
        return nodeRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Node not found"));
    }

    @Transactional
    public Node createNode(UUID ecosystemId, Node node) {
        node.setTenantId(TenantContext.require().toString());
        node.setEcosystem(ecosystemService.getEcosystemById(ecosystemId));
        Node saved = nodeRepository.save(node);
        webhookService.dispatch(TenantContext.require(), "node.created", saved.getId().toString());
        return saved;
    }

    @Transactional
    public Node updateNode(UUID id, Node updateData) {
        Node existing = getNodeById(id);
        existing.setAppName(updateData.getAppName());
        existing.setNodeType(updateData.getNodeType());
        existing.setStatus(updateData.getStatus());
        Node updated = nodeRepository.save(existing);
        webhookService.dispatch(TenantContext.require(), "node.updated", updated.getId().toString());
        return updated;
    }

    @Transactional
    public void deleteNode(UUID id) {
        Node existing = getNodeById(id);
        nodeRepository.delete(existing);
        webhookService.dispatch(TenantContext.require(), "node.deleted", existing.getId().toString());
    }
}
