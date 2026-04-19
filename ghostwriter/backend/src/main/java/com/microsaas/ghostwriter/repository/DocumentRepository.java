package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
    List<Document> findByTenantIdAndFormatOrderByCreatedAtDesc(UUID tenantId, String format);
    Optional<Document> findByIdAndTenantId(UUID id, UUID tenantId);
}
