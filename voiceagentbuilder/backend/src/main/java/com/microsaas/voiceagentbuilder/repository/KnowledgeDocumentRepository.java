package com.microsaas.voiceagentbuilder.repository;

import com.microsaas.voiceagentbuilder.model.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, UUID> {
    List<KnowledgeDocument> findByAgentIdAndTenantId(UUID agentId, UUID tenantId);
    Optional<KnowledgeDocument> findByIdAndAgentIdAndTenantId(UUID id, UUID agentId, UUID tenantId);
}
