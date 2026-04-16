package com.microsaas.localizationos.repository;

import com.microsaas.localizationos.domain.TranslationMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TranslationMemoryRepository extends JpaRepository<TranslationMemory, UUID> {
    List<TranslationMemory> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    List<TranslationMemory> findByTenantIdAndSourceLanguageAndTargetLanguageAndApprovedTrue(UUID tenantId, String sourceLanguage, String targetLanguage);
}
