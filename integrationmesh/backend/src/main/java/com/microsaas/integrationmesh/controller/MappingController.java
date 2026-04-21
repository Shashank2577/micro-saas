package com.microsaas.integrationmesh.controller;

import com.microsaas.integrationmesh.dto.FieldMappingDto;
import com.microsaas.integrationmesh.model.FieldMapping;
import com.microsaas.integrationmesh.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mappings")
@RequiredArgsConstructor
public class MappingController {

    private final MappingService mappingService;

    @GetMapping("/{integrationId}")
    public ResponseEntity<List<FieldMapping>> getMappings(@PathVariable UUID integrationId) {
        return ResponseEntity.ok(mappingService.getMappings(integrationId));
    }

    @PostMapping("/{integrationId}")
    public ResponseEntity<FieldMapping> saveMapping(@PathVariable UUID integrationId, @RequestBody FieldMappingDto dto) {
        return ResponseEntity.ok(mappingService.saveMapping(integrationId, dto));
    }

    @PostMapping("/{integrationId}/suggest")
    public ResponseEntity<List<FieldMapping>> suggestMappings(@PathVariable UUID integrationId) {
        return ResponseEntity.ok(mappingService.suggestMappings(integrationId));
    }
}
