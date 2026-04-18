package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.model.ApiVersion;
import com.microsaas.apimanager.repository.ApiVersionRepository;
import com.microsaas.apimanager.service.SchemaService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/versions/{versionId}/schema")
@RequiredArgsConstructor
public class SchemaController {

    private final ApiVersionRepository versionRepository;
    private final SchemaService schemaService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSchema(@PathVariable UUID projectId, @PathVariable UUID versionId) {
        Optional<ApiVersion> versionOpt = versionRepository.findByIdAndTenantId(versionId, TenantContext.require().toString());
        if (versionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ApiVersion version = versionOpt.get();
        if (version.getOpenapiSchema() == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(schemaService.parseSchema(version.getOpenapiSchema()));
    }
}
