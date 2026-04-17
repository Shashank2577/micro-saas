package com.microsaas.documentvault.service;

import com.microsaas.documentvault.dto.DocumentCreateRequest;
import com.microsaas.documentvault.dto.DocumentUpdateRequest;
import com.microsaas.documentvault.exception.DocumentNotFoundException;
import com.microsaas.documentvault.exception.RetentionHoldException;
import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.model.DocumentVersion;
import com.microsaas.documentvault.repository.DocumentRepository;
import com.microsaas.documentvault.repository.DocumentVersionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final LocalStorageService storageService;
    private final VirusScannerService virusScannerService;
    private final AIService aiService;
    private final QuotaService quotaService;
    private final AuditService auditService;

    public DocumentService(DocumentRepository documentRepository, DocumentVersionRepository versionRepository,
                           LocalStorageService storageService, VirusScannerService virusScannerService,
                           AIService aiService, QuotaService quotaService, AuditService auditService) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.storageService = storageService;
        this.virusScannerService = virusScannerService;
        this.aiService = aiService;
        this.quotaService = quotaService;
        this.auditService = auditService;
    }

    @Transactional
    public Document uploadDocument(MultipartFile file, DocumentCreateRequest req, UUID userId, UUID tenantId) throws IOException {
        virusScannerService.scan(file);
        quotaService.checkQuota(tenantId, file.getSize());

        String s3Key = storageService.store(file, tenantId);
        String ocrText = aiService.performOCR(file.getBytes());

        Document doc = new Document();
        doc.setTenantId(tenantId);
        doc.setTitle(req.getTitle());
        doc.setDescription(req.getDescription());
        doc.setFolderId(req.getFolderId());
        doc.setStatus("ACTIVE");
        doc.setOwnerId(userId);
        doc.setSizeBytes(file.getSize());
        doc.setMimeType(file.getContentType());
        doc.setCreatedBy(userId);
        doc = documentRepository.save(doc);

        DocumentVersion version = new DocumentVersion();
        version.setDocumentId(doc.getId());
        version.setTenantId(tenantId);
        version.setVersionNumber(1);
        version.setS3Key(s3Key);
        version.setSizeBytes(file.getSize());
        version.setChecksum("mock-checksum");
        version.setOcrText(ocrText);
        version.setCreatedBy(userId);
        version = versionRepository.save(version);

        doc.setCurrentVersionId(version.getId());
        documentRepository.save(doc);

        quotaService.updateUsage(tenantId, file.getSize());
        auditService.logAction(tenantId, doc.getId(), userId, "UPLOAD", "0.0.0.0", "System");

        return doc;
    }

    public Page<Document> getDocuments(UUID tenantId, String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return documentRepository.findByTenantIdAndTitleContainingIgnoreCase(tenantId, search, pageable);
        }
        return documentRepository.findByTenantId(tenantId, pageable);
    }

    public Document getDocument(UUID id, UUID tenantId) {
        return documentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));
    }

    @Transactional
    public Document updateDocument(UUID id, DocumentUpdateRequest req, UUID tenantId, UUID userId) {
        Document doc = getDocument(id, tenantId);
        if (req.getTitle() != null) doc.setTitle(req.getTitle());
        if (req.getDescription() != null) doc.setDescription(req.getDescription());
        if (req.getStatus() != null) doc.setStatus(req.getStatus());
        if (req.getRetentionHold() != null) doc.setRetentionHold(req.getRetentionHold());
        doc.setUpdatedBy(userId);
        
        auditService.logAction(tenantId, doc.getId(), userId, "UPDATE", "0.0.0.0", "System");
        return documentRepository.save(doc);
    }

    @Transactional
    public void deleteDocument(UUID id, UUID tenantId, UUID userId) {
        Document doc = getDocument(id, tenantId);
        if (Boolean.TRUE.equals(doc.getRetentionHold())) {
            throw new RetentionHoldException("Cannot delete document under retention hold");
        }
        doc.setStatus("DELETED");
        documentRepository.save(doc);
        
        auditService.logAction(tenantId, doc.getId(), userId, "DELETE", "0.0.0.0", "System");
    }
}
