package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.model.ConsistencyTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsistencyTrendRepository extends JpaRepository<ConsistencyTrend, UUID> {
    List<ConsistencyTrend> findByTenantIdAndBrandProfileIdOrderByPeriodAsc(UUID tenantId, UUID brandProfileId);
    Optional<ConsistencyTrend> findByTenantIdAndBrandProfileIdAndPeriod(UUID tenantId, UUID brandProfileId, LocalDate period);
}
