package com.microsaas.documentvault.repository;

import com.microsaas.documentvault.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, UUID> {
    List<DocumentVersion> findByDocumentIdAndTenantIdOrderByVersionNumberDesc(UUID documentId, UUID tenantId);
    Optional<DocumentVersion> findByIdAndTenantId(UUID id, UUID tenantId);
}
