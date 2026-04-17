package com.microsaas.documentvault.repository;

import com.microsaas.documentvault.model.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentShareRepository extends JpaRepository<DocumentShare, UUID> {
    Optional<DocumentShare> findByIdAndTenantId(UUID id, UUID tenantId);
}
