package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByTenantId(UUID tenantId);
    Optional<Document> findByIdAndTenantId(UUID id, UUID tenantId);
}
