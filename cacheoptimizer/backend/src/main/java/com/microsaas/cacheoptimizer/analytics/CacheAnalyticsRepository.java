package com.microsaas.cacheoptimizer.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CacheAnalyticsRepository extends JpaRepository<CacheAnalytics, UUID> {
    List<CacheAnalytics> findByTenantId(String tenantId);
}
