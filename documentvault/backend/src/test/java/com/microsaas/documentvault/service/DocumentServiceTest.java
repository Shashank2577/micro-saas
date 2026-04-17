package com.microsaas.documentvault.service;

import com.microsaas.documentvault.dto.DocumentCreateRequest;
import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.model.DocumentVersion;
import com.microsaas.documentvault.repository.DocumentRepository;
import com.microsaas.documentvault.repository.DocumentVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @Mock private DocumentRepository documentRepository;
    @Mock private DocumentVersionRepository versionRepository;
    @Mock private LocalStorageService storageService;
    @Mock private VirusScannerService virusScannerService;
    @Mock private AIService aiService;
    @Mock private QuotaService quotaService;
    @Mock private AuditService auditService;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadDocument_success() throws IOException {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        DocumentCreateRequest req = new DocumentCreateRequest();
        req.setTitle("Test Doc");
        
        when(storageService.store(any(), any())).thenReturn("mock-s3-key");
        when(aiService.performOCR(any())).thenReturn("mock ocr");
        
        Document mockDoc = new Document();
        mockDoc.setId(UUID.randomUUID());
        when(documentRepository.save(any(Document.class))).thenReturn(mockDoc);
        
        DocumentVersion mockVersion = new DocumentVersion();
        mockVersion.setId(UUID.randomUUID());
        when(versionRepository.save(any(DocumentVersion.class))).thenReturn(mockVersion);

        Document doc = documentService.uploadDocument(file, req, userId, tenantId);

        assertNotNull(doc);
        verify(virusScannerService, times(1)).scan(any());
        verify(quotaService, times(1)).checkQuota(any(), anyLong());
        verify(documentRepository, times(2)).save(any());
        verify(versionRepository, times(1)).save(any());
        verify(auditService, times(1)).logAction(any(), any(), any(), eq("UPLOAD"), any(), any());
    }
}
