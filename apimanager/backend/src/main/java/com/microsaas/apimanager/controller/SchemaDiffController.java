package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.service.SchemaDiffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/versions/compare")
@RequiredArgsConstructor
public class SchemaDiffController {

    private final SchemaDiffService schemaDiffService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> compareVersions(@PathVariable UUID projectId, @RequestBody Map<String, String> body) {
        String oldSchema = body.get("oldSchema");
        String newSchema = body.get("newSchema");
        return ResponseEntity.ok(schemaDiffService.compareSchemas(oldSchema, newSchema));
    }
}
