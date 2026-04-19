package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.domain.Document;
import com.microsaas.ghostwriter.dto.DocumentUpdateRequest;
import com.microsaas.ghostwriter.dto.GenerateRequest;
import com.microsaas.ghostwriter.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    private DocumentRepository documentRepository;
    private AiGenerationService aiGenerationService;
    private ApplicationEventPublisher eventPublisher;
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentRepository = mock(DocumentRepository.class);
        aiGenerationService = mock(AiGenerationService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        documentService = new DocumentService(documentRepository, aiGenerationService, eventPublisher);
    }

    @Test
    void getAllDocuments_ReturnsList() {
        UUID tenantId = UUID.randomUUID();
        when(documentRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)).thenReturn(List.of(new Document()));
        
        List<Document> result = documentService.getAllDocuments(tenantId, null);
        
        assertFalse(result.isEmpty());
        verify(documentRepository).findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Test
    void getDocumentById_WhenFound_ReturnsDocument() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        when(documentRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(doc));

        Document result = documentService.getDocumentById(id, tenantId);

        assertNotNull(result);
        assertEquals(doc, result);
    }

    @Test
    void generateDocument_CreatesAndSavesDocument() {
        UUID tenantId = UUID.randomUUID();
        GenerateRequest request = new GenerateRequest();
        request.setFormat("BLOG_POST");
        request.setTone("PROFESSIONAL");
        request.setTopic("Test Topic");
        request.setInstructions("Test Instructions");

        when(documentRepository.save(any(Document.class))).thenAnswer(i -> i.getArguments()[0]);
        when(aiGenerationService.generateContent("BLOG_POST", "PROFESSIONAL", "Test Topic", "Test Instructions"))
            .thenReturn("Generated Content");

        Document result = documentService.generateDocument(tenantId, request);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("BLOG_POST", result.getFormat());
        assertEquals("PROFESSIONAL", result.getTone());
        assertEquals("Test Topic", result.getTitle());
        assertEquals("COMPLETED", result.getStatus());
        assertEquals("Generated Content", result.getContent());
        
        verify(eventPublisher).publishEvent(any(com.microsaas.ghostwriter.event.ContentGeneratedEvent.class));
        verify(documentRepository, times(2)).save(any(Document.class));
    }

    @Test
    void updateDocument_UpdatesFields() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        doc.setTitle("Old Title");
        when(documentRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Document.class))).thenAnswer(i -> i.getArguments()[0]);

        DocumentUpdateRequest request = new DocumentUpdateRequest();
        request.setTitle("New Title");

        Document result = documentService.updateDocument(id, tenantId, request);

        assertEquals("New Title", result.getTitle());
    }

    @Test
    void deleteDocument_DeletesDocument() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        when(documentRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(doc));

        documentService.deleteDocument(id, tenantId);

        verify(documentRepository).delete(doc);
    }
}
