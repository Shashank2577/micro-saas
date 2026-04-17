package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import com.microsaas.peopleanalytics.model.AnalyticsCache;
import com.microsaas.peopleanalytics.repository.TeamHealthMetricRepository;
import com.microsaas.peopleanalytics.repository.AnalyticsCacheRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamHealthAnalysisService {

    private final TeamHealthMetricRepository repository;
    private final AnalyticsCacheRepository cacheRepository;

    public List<TeamHealthMetric> getMetrics(UUID tenantId) {
        String cacheKey = "team-health-metrics";
        Optional<AnalyticsCache> cached = cacheRepository.findByTenantIdAndCacheKey(tenantId, cacheKey);

        if (cached.isPresent() && cached.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            // Logic to convert cached JSON map back to list of objects would go here
            // Returning direct query for simplicity, but updating cache
        }

        List<TeamHealthMetric> results = repository.findByTenantId(tenantId);

        AnalyticsCache cache = cached.orElse(new AnalyticsCache());
        cache.setTenantId(tenantId);
        cache.setCacheKey(cacheKey);
        Map<String, Object> data = new HashMap<>();
        data.put("count", results.size());
        cache.setData(data);
        cache.setExpiresAt(LocalDateTime.now().plusHours(1));
        cacheRepository.save(cache);

        return results;
    }
}
