package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.AnalyticsCache;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AnalyticsCacheRepository extends JpaRepository<AnalyticsCache, UUID> {
    Optional<AnalyticsCache> findByTenantIdAndCacheKey(UUID tenantId, String cacheKey);
}
