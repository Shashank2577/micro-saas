package com.microsaas.localizationos.repository;

import com.microsaas.localizationos.domain.TranslationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranslationJobRepository extends JpaRepository<TranslationJob, UUID> {
    List<TranslationJob> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<TranslationJob> findByIdAndTenantId(UUID id, UUID tenantId);
}
