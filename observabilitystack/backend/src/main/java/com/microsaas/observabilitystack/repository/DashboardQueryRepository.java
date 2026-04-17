package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.DashboardQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardQueryRepository extends JpaRepository<DashboardQuery, UUID> {
    List<DashboardQuery> findByTenantId(String tenantId);
    Optional<DashboardQuery> findByIdAndTenantId(UUID id, String tenantId);
}
