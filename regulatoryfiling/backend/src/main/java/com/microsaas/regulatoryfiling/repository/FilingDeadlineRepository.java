package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.FilingDeadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilingDeadlineRepository extends JpaRepository<FilingDeadline, UUID> {
    List<FilingDeadline> findByTenantId(UUID tenantId);
    Optional<FilingDeadline> findByIdAndTenantId(UUID id, UUID tenantId);
}
