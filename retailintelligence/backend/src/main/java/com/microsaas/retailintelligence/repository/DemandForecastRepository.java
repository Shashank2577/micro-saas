package com.microsaas.retailintelligence.repository;

import com.microsaas.retailintelligence.entity.DemandForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DemandForecastRepository extends JpaRepository<DemandForecast, UUID> {
    List<DemandForecast> findByTenantIdAndSkuId(UUID tenantId, UUID skuId);
}
