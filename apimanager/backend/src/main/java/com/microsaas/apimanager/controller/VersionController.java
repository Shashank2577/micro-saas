package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.model.ApiVersion;
import com.microsaas.apimanager.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/versions")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @GetMapping
    public ResponseEntity<List<ApiVersion>> listVersions(@PathVariable UUID projectId) {
        return ResponseEntity.ok(versionService.getVersions(projectId));
    }

    @PostMapping
    public ResponseEntity<ApiVersion> createVersion(@PathVariable UUID projectId, @RequestBody ApiVersion version) {
        return ResponseEntity.ok(versionService.createVersion(projectId, version));
    }

    @PutMapping("/{versionId}/status")
    public ResponseEntity<ApiVersion> updateStatus(@PathVariable UUID projectId, @PathVariable UUID versionId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return versionService.updateStatus(versionId, status)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
