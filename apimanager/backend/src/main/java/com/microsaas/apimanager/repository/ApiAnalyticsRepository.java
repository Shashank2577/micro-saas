package com.microsaas.apimanager.repository;

import com.microsaas.apimanager.model.ApiAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ApiAnalyticsRepository extends JpaRepository<ApiAnalytics, UUID> {
    List<ApiAnalytics> findByProjectIdAndTenantId(UUID projectId, String tenantId);
}
