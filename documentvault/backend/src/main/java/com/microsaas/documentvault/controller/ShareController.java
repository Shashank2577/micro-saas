package com.microsaas.documentvault.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentvault.dto.ShareRequest;
import com.microsaas.documentvault.model.DocumentShare;
import com.microsaas.documentvault.service.ShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/documents/{documentId}/share")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }
    
    private UUID getUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @PostMapping
    public ResponseEntity<DocumentShare> shareDocument(
            @PathVariable UUID documentId,
            @RequestBody ShareRequest req) {
        return ResponseEntity.ok(shareService.shareDocument(documentId, req, getTenantId(), getUserId()));
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> revokeShare(
            @PathVariable UUID documentId,
            @PathVariable UUID shareId) {
        shareService.revokeShare(documentId, shareId, getTenantId(), getUserId());
        return ResponseEntity.ok().build();
    }
}
