package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.VestingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VestingScheduleRepository extends JpaRepository<VestingSchedule, UUID> {
    List<VestingSchedule> findByTenantId(UUID tenantId);
    Optional<VestingSchedule> findByIdAndTenantId(UUID id, UUID tenantId);
}
