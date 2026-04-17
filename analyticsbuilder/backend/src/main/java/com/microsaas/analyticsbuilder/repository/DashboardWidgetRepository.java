package com.microsaas.analyticsbuilder.repository;

import com.microsaas.analyticsbuilder.model.DashboardWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, UUID> {
    List<DashboardWidget> findByDashboardIdAndTenantId(UUID dashboardId, String tenantId);
}
