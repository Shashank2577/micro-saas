package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.ExportArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExportArtifactRepository extends JpaRepository<ExportArtifact, UUID> {
    List<ExportArtifact> findByTenantId(UUID tenantId);
    Optional<ExportArtifact> findByIdAndTenantId(UUID id, UUID tenantId);
}
