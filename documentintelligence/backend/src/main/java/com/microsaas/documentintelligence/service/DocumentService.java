package com.microsaas.documentintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentintelligence.dto.DocumentDTO;
import com.microsaas.documentintelligence.dto.DocumentExtractionDTO;
import com.microsaas.documentintelligence.model.Document;
import com.microsaas.documentintelligence.model.DocumentAuditTrail;
import com.microsaas.documentintelligence.model.DocumentExtraction;
import com.microsaas.documentintelligence.repository.DocumentAuditTrailRepository;
import com.microsaas.documentintelligence.repository.DocumentExtractionRepository;
import com.microsaas.documentintelligence.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentExtractionRepository extractionRepository;
    private final DocumentAuditTrailRepository auditTrailRepository;
    private final ExtractionService extractionService;

    @Transactional
    public DocumentDTO uploadDocument(MultipartFile file) {
        UUID tenantId = TenantContext.require();
        
        Document document = new Document();
        document.setTenantId(tenantId);
        document.setFilename(file.getOriginalFilename());
        document.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        document.setStoragePath("mock/path/" + UUID.randomUUID() + "-" + file.getOriginalFilename());
        document.setStatus("UPLOADED");
        document.setSizeBytes(file.getSize());
        
        document = documentRepository.save(document);
        
        DocumentAuditTrail audit = new DocumentAuditTrail();
        audit.setTenantId(tenantId);
        audit.setDocumentId(document.getId());
        audit.setAction("UPLOAD");
        audit.setDetails("{\"filename\":\"" + file.getOriginalFilename() + "\"}");
        auditTrailRepository.save(audit);
        
        // Trigger async processing
        extractionService.processDocument(document.getId(), tenantId);
        
        return mapToDTO(document);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getDocuments(Pageable pageable) {
        UUID tenantId = TenantContext.require();
        return documentRepository.findByTenantId(tenantId, pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public DocumentDTO getDocument(UUID id) {
        UUID tenantId = TenantContext.require();
        Document document = documentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return mapToDTO(document);
    }

    @Transactional(readOnly = true)
    public List<DocumentExtractionDTO> getExtractions(UUID id) {
        UUID tenantId = TenantContext.require();
        return extractionRepository.findByDocumentIdAndTenantId(id, tenantId).stream()
                .map(this::mapExtractionToDTO)
                .collect(Collectors.toList());
    }
    
    private DocumentDTO mapToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setFilename(document.getFilename());
        dto.setContentType(document.getContentType());
        dto.setStatus(document.getStatus());
        dto.setDocumentType(document.getDocumentType());
        dto.setSizeBytes(document.getSizeBytes());
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }
    
    private DocumentExtractionDTO mapExtractionToDTO(DocumentExtraction extraction) {
        DocumentExtractionDTO dto = new DocumentExtractionDTO();
        dto.setId(extraction.getId());
        dto.setExtractionType(extraction.getExtractionType());
        dto.setKeyName(extraction.getKeyName());
        dto.setValueText(extraction.getValueText());
        dto.setConfidenceScore(extraction.getConfidenceScore());
        dto.setPageNumber(extraction.getPageNumber());
        return dto;
    }
}
