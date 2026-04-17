package com.microsaas.documentvault.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentvault.dto.DocumentCreateRequest;
import com.microsaas.documentvault.dto.DocumentUpdateRequest;
import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }
    
    // Mock user id
    private UUID getUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute DocumentCreateRequest req) throws IOException {
        Document doc = documentService.uploadDocument(file, req, getUserId(), getTenantId());
        return ResponseEntity.ok(doc);
    }

    @GetMapping
    public ResponseEntity<Page<Document>> getDocuments(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(documentService.getDocuments(getTenantId(), search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocument(id, getTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable UUID id,
            @RequestBody DocumentUpdateRequest req) {
        return ResponseEntity.ok(documentService.updateDocument(id, req, getTenantId(), getUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id, getTenantId(), getUserId());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> previewDocument(@PathVariable UUID id) {
        // mock preview
        return ResponseEntity.ok("mock preview content".getBytes());
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID id) {
        // mock download
        return ResponseEntity.ok("mock file content".getBytes());
    }
}
