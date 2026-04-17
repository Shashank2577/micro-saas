package com.microsaas.healthcaredocai.repository;

import com.microsaas.healthcaredocai.domain.ClinicalTemplate;
import com.microsaas.healthcaredocai.domain.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalTemplateRepository extends JpaRepository<ClinicalTemplate, UUID> {
    List<ClinicalTemplate> findByTenantIdAndSpecialty(UUID tenantId, String specialty);
    Optional<ClinicalTemplate> findByTenantIdAndSpecialtyAndNoteType(UUID tenantId, String specialty, NoteType noteType);
}
