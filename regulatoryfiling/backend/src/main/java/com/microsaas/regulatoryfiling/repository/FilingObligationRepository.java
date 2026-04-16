package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.FilingObligation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilingObligationRepository extends JpaRepository<FilingObligation, UUID> {
    List<FilingObligation> findByTenantId(UUID tenantId);
    List<FilingObligation> findByTenantIdAndDueDateBetween(UUID tenantId, LocalDate startDate, LocalDate endDate);
    List<FilingObligation> findByTenantIdAndDueDateBeforeAndStatusNot(UUID tenantId, LocalDate date, com.microsaas.regulatoryfiling.domain.FilingStatus status);
    Optional<FilingObligation> findByIdAndTenantId(UUID id, UUID tenantId);
}
