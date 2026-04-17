package com.microsaas.documentintelligence.repository;

import com.microsaas.documentintelligence.model.DocumentExtraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentExtractionRepository extends JpaRepository<DocumentExtraction, UUID> {
    List<DocumentExtraction> findByDocumentIdAndTenantId(UUID documentId, UUID tenantId);
}
