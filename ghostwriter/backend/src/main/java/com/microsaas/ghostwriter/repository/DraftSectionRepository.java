package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.DraftSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DraftSectionRepository extends JpaRepository<DraftSection, UUID> {
    List<DraftSection> findBySessionIdAndTenantIdOrderBySectionOrderAsc(UUID sessionId, UUID tenantId);
    Optional<DraftSection> findByIdAndTenantId(UUID id, UUID tenantId);
}
