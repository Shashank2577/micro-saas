package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.ForecastRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForecastRunRepository extends JpaRepository<ForecastRun, UUID> {
    List<ForecastRun> findByTenantId(UUID tenantId);
    Optional<ForecastRun> findByIdAndTenantId(UUID id, UUID tenantId);
}
