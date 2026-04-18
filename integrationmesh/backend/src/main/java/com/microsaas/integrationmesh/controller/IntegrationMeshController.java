package com.microsaas.integrationmesh.controller;

import com.microsaas.integrationmesh.dto.ConnectorDto;
import com.microsaas.integrationmesh.dto.IntegrationDto;
import com.microsaas.integrationmesh.dto.FieldMappingDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.model.FieldMapping;
import com.microsaas.integrationmesh.model.Integration;
import com.microsaas.integrationmesh.model.SyncHistory;
import com.microsaas.integrationmesh.service.ConnectorService;
import com.microsaas.integrationmesh.service.IntegrationService;
import com.microsaas.integrationmesh.service.MappingService;
import com.microsaas.integrationmesh.service.SyncHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class IntegrationMeshController {

    private final ConnectorService connectorService;
    private final IntegrationService integrationService;
    private final MappingService mappingService;
    private final SyncHistoryService syncHistoryService;

    public IntegrationMeshController(ConnectorService connectorService, IntegrationService integrationService, MappingService mappingService, SyncHistoryService syncHistoryService) {
        this.connectorService = connectorService;
        this.integrationService = integrationService;
        this.mappingService = mappingService;
        this.syncHistoryService = syncHistoryService;
    }

    @GetMapping("/connectors")
    public ResponseEntity<List<Connector>> listConnectors() {
        return ResponseEntity.ok(connectorService.listConnectors());
    }

    @PostMapping("/connectors")
    public ResponseEntity<Connector> createConnector(@RequestBody ConnectorDto dto) {
        return ResponseEntity.ok(connectorService.createConnector(dto));
    }

    @GetMapping("/connectors/{id}")
    public ResponseEntity<Connector> getConnector(@PathVariable UUID id) {
        return ResponseEntity.ok(connectorService.getConnector(id));
    }

    @GetMapping("/integrations")
    public ResponseEntity<List<Integration>> listIntegrations() {
        return ResponseEntity.ok(integrationService.listIntegrations());
    }

    @PostMapping("/integrations")
    public ResponseEntity<Integration> createIntegration(@RequestBody IntegrationDto dto) {
        return ResponseEntity.ok(integrationService.createIntegration(dto));
    }

    @GetMapping("/integrations/{id}")
    public ResponseEntity<Integration> getIntegration(@PathVariable UUID id) {
        return ResponseEntity.ok(integrationService.getIntegration(id));
    }

    @PutMapping("/integrations/{id}/status")
    public ResponseEntity<Integration> updateIntegrationStatus(@PathVariable UUID id, @RequestBody Map<String, String> statusMap) {
        return ResponseEntity.ok(integrationService.updateStatus(id, statusMap.get("status")));
    }

    @GetMapping("/integrations/{id}/mappings")
    public ResponseEntity<List<FieldMapping>> getMappings(@PathVariable UUID id) {
        return ResponseEntity.ok(mappingService.getMappings(id));
    }

    @PostMapping("/integrations/{id}/mappings")
    public ResponseEntity<FieldMapping> saveMapping(@PathVariable UUID id, @RequestBody FieldMappingDto dto) {
        return ResponseEntity.ok(mappingService.saveMapping(id, dto));
    }

    @PostMapping("/integrations/{id}/mappings/suggest")
    public ResponseEntity<List<FieldMapping>> suggestMappings(@PathVariable UUID id) {
        return ResponseEntity.ok(mappingService.suggestMappings(id));
    }

    @GetMapping("/integrations/{id}/history")
    public ResponseEntity<List<SyncHistory>> getSyncHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(syncHistoryService.getHistory(id));
    }
}
