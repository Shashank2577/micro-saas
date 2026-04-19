package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.PrecedentNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrecedentNoteRepository extends JpaRepository<PrecedentNote, UUID> {
    List<PrecedentNote> findByTenantId(UUID tenantId);
    Optional<PrecedentNote> findByIdAndTenantId(UUID id, UUID tenantId);
}
