package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, UUID> {
    List<MarketData> findByTenantId(UUID tenantId);
    List<MarketData> findByTenantIdAndRole(UUID tenantId, String role);
    List<MarketData> findByTenantIdAndRoleAndLocation(UUID tenantId, String role, String location);
}
