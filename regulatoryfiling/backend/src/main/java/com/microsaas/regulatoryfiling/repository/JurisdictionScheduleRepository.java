package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.JurisdictionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JurisdictionScheduleRepository extends JpaRepository<JurisdictionSchedule, UUID> {
    List<JurisdictionSchedule> findByTenantId(UUID tenantId);
    Optional<JurisdictionSchedule> findByIdAndTenantId(UUID id, UUID tenantId);
}
