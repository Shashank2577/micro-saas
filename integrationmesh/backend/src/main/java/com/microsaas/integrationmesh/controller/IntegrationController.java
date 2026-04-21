package com.microsaas.integrationmesh.controller;

import com.microsaas.integrationmesh.dto.IntegrationDto;
import com.microsaas.integrationmesh.model.Integration;
import com.microsaas.integrationmesh.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/integrations")
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationService integrationService;

    @GetMapping
    public ResponseEntity<List<Integration>> listIntegrations() {
        return ResponseEntity.ok(integrationService.listIntegrations());
    }

    @PostMapping
    public ResponseEntity<Integration> createIntegration(@RequestBody IntegrationDto dto) {
        return ResponseEntity.ok(integrationService.createIntegration(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Integration> getIntegration(@PathVariable UUID id) {
        return ResponseEntity.ok(integrationService.getIntegration(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Integration> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> statusMap) {
        return ResponseEntity.ok(integrationService.updateStatus(id, statusMap.get("status")));
    }
}
