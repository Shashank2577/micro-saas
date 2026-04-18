package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.CompensationCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompensationCycleRepository extends JpaRepository<CompensationCycle, UUID> {
    List<CompensationCycle> findByTenantId(UUID tenantId);
}
