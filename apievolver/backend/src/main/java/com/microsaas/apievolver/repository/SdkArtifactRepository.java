package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.SdkArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SdkArtifactRepository extends JpaRepository<SdkArtifact, UUID> {
    List<SdkArtifact> findByTenantId(UUID tenantId);
    Optional<SdkArtifact> findByIdAndTenantId(UUID id, UUID tenantId);
}
