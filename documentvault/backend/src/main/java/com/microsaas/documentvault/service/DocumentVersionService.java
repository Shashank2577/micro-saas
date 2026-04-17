package com.microsaas.documentvault.service;

import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.model.DocumentVersion;
import com.microsaas.documentvault.repository.DocumentRepository;
import com.microsaas.documentvault.repository.DocumentVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentVersionService {
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final LocalStorageService storageService;
    private final VirusScannerService virusScannerService;
    private final AIService aiService;
    private final QuotaService quotaService;
    private final AuditService auditService;

    public DocumentVersionService(DocumentRepository documentRepository, DocumentVersionRepository versionRepository,
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

    public List<DocumentVersion> getVersions(UUID documentId, UUID tenantId) {
        return versionRepository.findByDocumentIdAndTenantIdOrderByVersionNumberDesc(documentId, tenantId);
    }

    @Transactional
    public DocumentVersion addVersion(UUID documentId, MultipartFile file, UUID userId, UUID tenantId) throws IOException {
        Document doc = documentRepository.findByIdAndTenantId(documentId, tenantId).orElseThrow();
        virusScannerService.scan(file);
        quotaService.checkQuota(tenantId, file.getSize());

        String s3Key = storageService.store(file, tenantId);
        String ocrText = aiService.performOCR(file.getBytes());

        List<DocumentVersion> versions = versionRepository.findByDocumentIdAndTenantIdOrderByVersionNumberDesc(documentId, tenantId);
        int nextVersion = versions.isEmpty() ? 1 : versions.get(0).getVersionNumber() + 1;

        DocumentVersion version = new DocumentVersion();
        version.setDocumentId(doc.getId());
        version.setTenantId(tenantId);
        version.setVersionNumber(nextVersion);
        version.setS3Key(s3Key);
        version.setSizeBytes(file.getSize());
        version.setChecksum("mock-checksum");
        version.setOcrText(ocrText);
        version.setCreatedBy(userId);
        version = versionRepository.save(version);

        doc.setCurrentVersionId(version.getId());
        doc.setSizeBytes(file.getSize());
        documentRepository.save(doc);

        quotaService.updateUsage(tenantId, file.getSize());
        auditService.logAction(tenantId, doc.getId(), userId, "NEW_VERSION", "0.0.0.0", "System");

        return version;
    }

    @Transactional
    public Document rollbackToVersion(UUID documentId, UUID versionId, UUID tenantId, UUID userId) {
        Document doc = documentRepository.findByIdAndTenantId(documentId, tenantId).orElseThrow();
        DocumentVersion version = versionRepository.findByIdAndTenantId(versionId, tenantId).orElseThrow();

        doc.setCurrentVersionId(version.getId());
        doc.setSizeBytes(version.getSizeBytes());
        documentRepository.save(doc);

        auditService.logAction(tenantId, doc.getId(), userId, "VERSION_ROLLBACK", "0.0.0.0", "System");

        return doc;
    }
}
