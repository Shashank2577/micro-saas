package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricalDataRepository extends JpaRepository<HistoricalData, UUID> {
    List<HistoricalData> findByTenantId(UUID tenantId);
}
