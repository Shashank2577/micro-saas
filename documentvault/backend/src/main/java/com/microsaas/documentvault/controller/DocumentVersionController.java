package com.microsaas.documentvault.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.model.DocumentVersion;
import com.microsaas.documentvault.service.DocumentVersionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents/{documentId}/versions")
public class DocumentVersionController {

    private final DocumentVersionService versionService;

    public DocumentVersionController(DocumentVersionService versionService) {
        this.versionService = versionService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }
    
    private UUID getUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @GetMapping
    public ResponseEntity<List<DocumentVersion>> getVersions(@PathVariable UUID documentId) {
        return ResponseEntity.ok(versionService.getVersions(documentId, getTenantId()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentVersion> addVersion(
            @PathVariable UUID documentId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(versionService.addVersion(documentId, file, getUserId(), getTenantId()));
    }

    @PostMapping("/{versionId}/rollback")
    public ResponseEntity<Document> rollbackToVersion(
            @PathVariable UUID documentId,
            @PathVariable UUID versionId) {
        return ResponseEntity.ok(versionService.rollbackToVersion(documentId, versionId, getTenantId(), getUserId()));
    }
}
