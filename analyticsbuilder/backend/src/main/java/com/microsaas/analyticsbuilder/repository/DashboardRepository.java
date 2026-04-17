package com.microsaas.analyticsbuilder.repository;

import com.microsaas.analyticsbuilder.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, UUID> {
    List<Dashboard> findByTenantId(String tenantId);
    Optional<Dashboard> findByIdAndTenantId(UUID id, String tenantId);
}
