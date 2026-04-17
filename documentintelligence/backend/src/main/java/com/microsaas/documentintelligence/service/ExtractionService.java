package com.microsaas.documentintelligence.service;

import com.microsaas.documentintelligence.model.Document;
import com.microsaas.documentintelligence.model.DocumentExtraction;
import com.microsaas.documentintelligence.repository.DocumentExtractionRepository;
import com.microsaas.documentintelligence.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExtractionService {

    private final DocumentRepository documentRepository;
    private final DocumentExtractionRepository extractionRepository;
    // Assume a mock LLM service or use a basic interface
    
    @Async
    @Transactional
    public void processDocument(UUID documentId, UUID tenantId) {
        Document document = documentRepository.findByIdAndTenantId(documentId, tenantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        document.setStatus("EXTRACTING");
        documentRepository.save(document);
        
        // Mock extraction logic
        try {
            Thread.sleep(1000); // Simulate processing time
            
            DocumentExtraction extraction = new DocumentExtraction();
            extraction.setTenantId(tenantId);
            extraction.setDocumentId(documentId);
            extraction.setExtractionType("KEY_VALUE");
            extraction.setKeyName("Invoice Number");
            extraction.setValueText("INV-12345");
            extraction.setConfidenceScore(new BigDecimal("0.9500"));
            extraction.setPageNumber(1);
            
            extractionRepository.save(extraction);
            
            document.setStatus("COMPLETE");
            document.setDocumentType("Invoice");
        } catch (Exception e) {
            document.setStatus("ERROR");
        }
        
        documentRepository.save(document);
    }
}
