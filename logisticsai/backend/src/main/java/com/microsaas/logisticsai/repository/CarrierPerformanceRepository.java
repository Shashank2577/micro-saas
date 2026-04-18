package com.microsaas.logisticsai.repository;

import com.microsaas.logisticsai.domain.CarrierPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface CarrierPerformanceRepository extends JpaRepository<CarrierPerformance, UUID> {
    List<CarrierPerformance> findByTenantId(UUID tenantId);
    Optional<CarrierPerformance> findByIdAndTenantId(UUID id, UUID tenantId);
}
