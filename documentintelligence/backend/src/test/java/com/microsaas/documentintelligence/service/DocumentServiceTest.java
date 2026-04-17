package com.microsaas.documentintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentintelligence.dto.DocumentDTO;
import com.microsaas.documentintelligence.model.Document;
import com.microsaas.documentintelligence.repository.DocumentAuditTrailRepository;
import com.microsaas.documentintelligence.repository.DocumentExtractionRepository;
import com.microsaas.documentintelligence.repository.DocumentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentExtractionRepository extractionRepository;
    @Mock
    private DocumentAuditTrailRepository auditTrailRepository;
    @Mock
    private ExtractionService extractionService;

    @InjectMocks
    private DocumentService documentService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void uploadDocument_ShouldSaveAndTriggerProcessing() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        Document doc = new Document();
        doc.setId(UUID.randomUUID());
        doc.setTenantId(tenantId);
        doc.setFilename("test.pdf");
        doc.setStatus("UPLOADED");
        doc.setSizeBytes((long) "dummy content".length());

        when(documentRepository.save(any(Document.class))).thenReturn(doc);

        DocumentDTO result = documentService.uploadDocument(file);

        assertNotNull(result);
        assertEquals("test.pdf", result.getFilename());
        verify(auditTrailRepository).save(any());
        verify(extractionService).processDocument(doc.getId(), tenantId);
    }

    @Test
    void getDocument_ShouldReturnDocument_WhenExists() {
        UUID docId = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(docId);
        doc.setFilename("test.pdf");

        when(documentRepository.findByIdAndTenantId(docId, tenantId)).thenReturn(Optional.of(doc));

        DocumentDTO result = documentService.getDocument(docId);

        assertNotNull(result);
        assertEquals("test.pdf", result.getFilename());
    }
}
