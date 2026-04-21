package com.microsaas.integrationmesh.controller;

import com.microsaas.integrationmesh.dto.ConnectorDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.service.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/connectors")
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService connectorService;

    @GetMapping
    public ResponseEntity<List<Connector>> listConnectors() {
        return ResponseEntity.ok(connectorService.listConnectors());
    }

    @PostMapping
    public ResponseEntity<Connector> createConnector(@RequestBody ConnectorDto dto) {
        return ResponseEntity.ok(connectorService.createConnector(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Connector> getConnector(@PathVariable UUID id) {
        return ResponseEntity.ok(connectorService.getConnector(id));
    }
}
