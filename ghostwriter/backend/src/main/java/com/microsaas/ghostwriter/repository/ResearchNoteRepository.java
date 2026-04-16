package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.ResearchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResearchNoteRepository extends JpaRepository<ResearchNote, UUID> {
    List<ResearchNote> findBySessionIdAndTenantId(UUID sessionId, UUID tenantId);
    Optional<ResearchNote> findByIdAndTenantId(UUID id, UUID tenantId);
}
