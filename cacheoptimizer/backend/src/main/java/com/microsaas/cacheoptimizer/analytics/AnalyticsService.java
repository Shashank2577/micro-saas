package com.microsaas.cacheoptimizer.analytics;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsService {

    private final CacheAnalyticsRepository repository;

    public AnalyticsService(CacheAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CacheAnalytics> getAnalytics() {
        String tenantId = TenantContext.require().toString();
        return repository.findByTenantId(tenantId);
    }
    
    @Transactional
    public void recordHit(String tenantId, String namespace, long sizeBytes) {
        CacheAnalytics analytics = getOrCreate(tenantId, namespace);
        analytics.setHitCount(analytics.getHitCount() + 1);
        analytics.setTotalSizeBytes(analytics.getTotalSizeBytes() + sizeBytes);
        repository.save(analytics);
    }

    @Transactional
    public void recordMiss(String tenantId, String namespace) {
        CacheAnalytics analytics = getOrCreate(tenantId, namespace);
        analytics.setMissCount(analytics.getMissCount() + 1);
        repository.save(analytics);
    }
    
    private CacheAnalytics getOrCreate(String tenantId, String namespace) {
        return repository.findByTenantId(tenantId).stream()
            .filter(a -> a.getNamespace().equals(namespace))
            .findFirst()
            .orElseGet(() -> {
                CacheAnalytics a = new CacheAnalytics();
                a.setTenantId(tenantId);
                a.setNamespace(namespace);
                return a;
            });
    }
}
