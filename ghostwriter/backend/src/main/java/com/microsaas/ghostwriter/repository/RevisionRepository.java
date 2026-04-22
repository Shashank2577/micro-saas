package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, UUID> {
    List<Revision> findByTenantId(String tenantId);
    Optional<Revision> findByIdAndTenantId(UUID id, String tenantId);
    List<Revision> findByDocumentIdAndTenantId(UUID documentId, String tenantId);
}
