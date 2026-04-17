package com.microsaas.healthcaredocai.repository;

import com.microsaas.healthcaredocai.domain.GeneratedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeneratedNoteRepository extends JpaRepository<GeneratedNote, UUID> {
    List<GeneratedNote> findByTenantId(UUID tenantId);
    Optional<GeneratedNote> findByIdAndTenantId(UUID id, UUID tenantId);
    List<GeneratedNote> findByEncounterIdAndTenantId(UUID encounterId, UUID tenantId);
}
