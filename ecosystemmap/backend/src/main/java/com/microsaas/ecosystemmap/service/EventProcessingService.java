package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.ecosystemmap.entity.Connection;
import com.microsaas.ecosystemmap.entity.DataFlowEvent;
import com.microsaas.ecosystemmap.entity.DeploymentStatus;
import com.microsaas.ecosystemmap.entity.Node;
import com.microsaas.ecosystemmap.repository.DataFlowEventRepository;
import com.microsaas.ecosystemmap.repository.DeploymentStatusRepository;
import com.microsaas.ecosystemmap.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProcessingService {

    private final NodeRepository nodeRepository;
    private final DeploymentStatusRepository deploymentStatusRepository;
    private final DataFlowEventRepository dataFlowEventRepository;
    private final ConnectionService connectionService;

    @Transactional
    public void processAppDeployedEvent(String appName, String tenantId) {
        log.info("Processing app.deployed for app: {}, tenant: {}", appName, tenantId);

        try {
            TenantContext.set(UUID.fromString(tenantId));
            Optional<Node> nodeOpt = nodeRepository.findByAppNameAndTenantId(appName, tenantId);
            if (nodeOpt.isPresent()) {
                Node node = nodeOpt.get();
                node.setStatus("DEPLOYED");
                nodeRepository.save(node);

                DeploymentStatus status = DeploymentStatus.builder()
                        .tenantId(tenantId)
                        .node(node)
                        .status("DEPLOYED")
                        .statusReason("Received app.deployed webhook")
                        .build();
                deploymentStatusRepository.save(status);
            } else {
                log.warn("Node not found for app: {} in tenant: {}", appName, tenantId);
            }
        } catch (Exception e) {
            log.error("Error processing app.deployed event", e);
        } finally {
            TenantContext.clear();
        }
    }

    @Transactional
    public void processAppUndeployedEvent(String appName, String tenantId) {
        log.info("Processing app.undeployed for app: {}, tenant: {}", appName, tenantId);
        try {
            TenantContext.set(UUID.fromString(tenantId));
            Optional<Node> nodeOpt = nodeRepository.findByAppNameAndTenantId(appName, tenantId);
            if (nodeOpt.isPresent()) {
                Node node = nodeOpt.get();
                node.setStatus("UNDEPLOYED");
                nodeRepository.save(node);

                DeploymentStatus status = DeploymentStatus.builder()
                        .tenantId(tenantId)
                        .node(node)
                        .status("UNDEPLOYED")
                        .statusReason("Received app.undeployed webhook")
                        .build();
                deploymentStatusRepository.save(status);
            }
        } catch (Exception e) {
            log.error("Error processing app.undeployed event", e);
        } finally {
            TenantContext.clear();
        }
    }

    @Transactional
    public void processDataFlowEvent(UUID connectionId, String eventType, Long payloadSize, String tenantId) {
        log.info("Processing data.flow.event for connection: {}, tenant: {}", connectionId, tenantId);
        try {
            TenantContext.set(UUID.fromString(tenantId));
            Connection connection = connectionService.getConnectionById(connectionId);
            DataFlowEvent event = DataFlowEvent.builder()
                    .tenantId(tenantId)
                    .connection(connection)
                    .eventType(eventType)
                    .payloadSizeBytes(payloadSize)
                    .build();
            dataFlowEventRepository.save(event);
        } catch (Exception e) {
            log.error("Error processing data.flow.event", e);
        } finally {
            TenantContext.clear();
        }
    }
}
