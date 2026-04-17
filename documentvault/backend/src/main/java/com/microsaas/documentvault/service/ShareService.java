package com.microsaas.documentvault.service;

import com.microsaas.documentvault.dto.ShareRequest;
import com.microsaas.documentvault.model.DocumentShare;
import com.microsaas.documentvault.repository.DocumentShareRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShareService {
    private final DocumentShareRepository shareRepository;
    private final AuditService auditService;

    public ShareService(DocumentShareRepository shareRepository, AuditService auditService) {
        this.shareRepository = shareRepository;
        this.auditService = auditService;
    }

    public DocumentShare shareDocument(UUID documentId, ShareRequest req, UUID tenantId, UUID userId) {
        DocumentShare share = new DocumentShare();
        share.setDocumentId(documentId);
        share.setTenantId(tenantId);
        share.setSharedWithEmail(req.getEmail());
        share.setAccessLevel(req.getAccessLevel());
        share.setExpiresAt(req.getExpiresAt());
        // mock password hashing
        if (req.getPassword() != null) {
            share.setPasswordHash("hashed_" + req.getPassword());
        }
        share.setCreatedBy(userId);
        share = shareRepository.save(share);
        
        auditService.logAction(tenantId, documentId, userId, "SHARE", "0.0.0.0", "System");
        return share;
    }

    public void revokeShare(UUID documentId, UUID shareId, UUID tenantId, UUID userId) {
        DocumentShare share = shareRepository.findByIdAndTenantId(shareId, tenantId).orElseThrow();
        shareRepository.delete(share);
        
        auditService.logAction(tenantId, documentId, userId, "REVOKE_SHARE", "0.0.0.0", "System");
    }
}
