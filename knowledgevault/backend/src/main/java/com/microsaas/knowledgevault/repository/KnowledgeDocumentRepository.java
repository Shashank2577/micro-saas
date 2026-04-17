package com.microsaas.knowledgevault.repository;

import com.microsaas.knowledgevault.domain.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, UUID> {
    List<KnowledgeDocument> findByTenantId(String tenantId);
    List<KnowledgeDocument> findByTenantIdAndFreshnessStatus(String tenantId, String freshnessStatus);
}
