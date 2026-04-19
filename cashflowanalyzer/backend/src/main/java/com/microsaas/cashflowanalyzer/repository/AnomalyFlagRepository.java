package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.AnomalyFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnomalyFlagRepository extends JpaRepository<AnomalyFlag, UUID> {
    List<AnomalyFlag> findByTenantId(UUID tenantId);
    Optional<AnomalyFlag> findByIdAndTenantId(UUID id, UUID tenantId);
}
