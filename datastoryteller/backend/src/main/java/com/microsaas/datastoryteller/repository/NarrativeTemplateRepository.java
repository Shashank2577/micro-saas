package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.NarrativeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrativeTemplateRepository extends JpaRepository<NarrativeTemplate, UUID> {
    List<NarrativeTemplate> findByTenantId(String tenantId);
    Optional<NarrativeTemplate> findByIdAndTenantId(UUID id, String tenantId);
}
