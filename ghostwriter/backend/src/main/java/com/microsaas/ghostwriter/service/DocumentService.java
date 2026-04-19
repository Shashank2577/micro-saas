package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.domain.Document;
import com.microsaas.ghostwriter.dto.DocumentUpdateRequest;
import com.microsaas.ghostwriter.dto.GenerateRequest;
import com.microsaas.ghostwriter.event.ContentGeneratedEvent;
import com.microsaas.ghostwriter.repository.DocumentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AiGenerationService aiGenerationService;
    private final ApplicationEventPublisher eventPublisher;

    public DocumentService(DocumentRepository documentRepository, AiGenerationService aiGenerationService, ApplicationEventPublisher eventPublisher) {
        this.documentRepository = documentRepository;
        this.aiGenerationService = aiGenerationService;
        this.eventPublisher = eventPublisher;
    }

    public List<Document> getAllDocuments(UUID tenantId, String format) {
        if (format != null && !format.isEmpty()) {
            return documentRepository.findByTenantIdAndFormatOrderByCreatedAtDesc(tenantId, format);
        }
        return documentRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public Document getDocumentById(UUID id, UUID tenantId) {
        return documentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional
    public Document generateDocument(UUID tenantId, GenerateRequest request) {
        Document document = new Document();
        document.setTenantId(tenantId);
        document.setTitle(request.getTopic() != null ? request.getTopic() : "Untitled");
        document.setFormat(request.getFormat());
        document.setTone(request.getTone());
        document.setStatus("GENERATING");
        document = documentRepository.save(document);

        // Synchronous generation for simplicity in this implementation
        String content = aiGenerationService.generateContent(
            request.getFormat(), 
            request.getTone(), 
            request.getTopic(), 
            request.getInstructions()
        );

        document.setContent(content);
        document.setStatus("COMPLETED");
        document = documentRepository.save(document);

        ContentGeneratedEvent event = new ContentGeneratedEvent(
            UUID.randomUUID().toString(),
            tenantId,
            "ghostwriter",
            Map.of("documentId", document.getId().toString(), "format", document.getFormat())
        );
        eventPublisher.publishEvent(event);

        return document;
    }

    @Transactional
    public Document updateDocument(UUID id, UUID tenantId, DocumentUpdateRequest request) {
        Document document = getDocumentById(id, tenantId);
        if (request.getTitle() != null) {
            document.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            document.setContent(request.getContent());
        }
        return documentRepository.save(document);
    }

    @Transactional
    public void deleteDocument(UUID id, UUID tenantId) {
        Document document = getDocumentById(id, tenantId);
        documentRepository.delete(document);
    }
}
