package com.microsaas.localizationos.repository;

import com.microsaas.localizationos.domain.TranslationProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranslationProjectRepository extends JpaRepository<TranslationProject, UUID> {
    List<TranslationProject> findByTenantId(UUID tenantId);
    Optional<TranslationProject> findByIdAndTenantId(UUID id, UUID tenantId);
}
