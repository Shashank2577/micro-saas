package com.microsaas.taxdataorganizer.service;

import com.microsaas.taxdataorganizer.model.DocumentStatus;
import com.microsaas.taxdataorganizer.model.DocumentType;
import com.microsaas.taxdataorganizer.model.TaxDocument;
import com.microsaas.taxdataorganizer.repository.TaxDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxDocumentService {

    private final TaxDocumentRepository taxDocumentRepository;

    @Transactional
    public TaxDocument uploadDocument(UUID taxYearId, String fileName, DocumentType documentType, UUID tenantId) {
        TaxDocument document = TaxDocument.builder()
                .taxYearId(taxYearId)
                .fileName(fileName)
                .documentType(documentType)
                .uploadedAt(LocalDateTime.now())
                .status(DocumentStatus.PENDING)
                .tenantId(tenantId)
                .build();
        return taxDocumentRepository.save(document);
    }

    @Transactional
    public TaxDocument updateDocumentStatus(UUID documentId, DocumentStatus status, UUID tenantId) {
        TaxDocument document = taxDocumentRepository.findById(documentId)
                .filter(doc -> doc.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setStatus(status);
        return taxDocumentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<TaxDocument> listDocumentsByTaxYear(UUID taxYearId, UUID tenantId) {
        return taxDocumentRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId);
    }
}
