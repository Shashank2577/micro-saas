package com.microsaas.insightengine.service;

import com.microsaas.insightengine.dto.InsightEnrichment;
import com.microsaas.insightengine.entity.Insight;
import com.microsaas.insightengine.entity.MetricData;
import com.microsaas.insightengine.repository.InsightRepository;
import com.microsaas.insightengine.repository.MetricDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightDiscoveryService {

    private final MetricDataRepository metricDataRepository;
    private final InsightRepository insightRepository;
    private final AiExplanationService aiExplanationService;
    private final AlertService alertService;

    // This would normally loop through all tenants. For simplicity, we assume one tenant or a job per tenant.
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void runContinuousDiscovery() {
        log.info("Running continuous discovery job...");
        // In reality, we'd fetch all active tenants
        // UUID tenantId = UUID.fromString("...");
        // discoverAnomalies(tenantId, LocalDateTime.now().minusDays(1));
    }

    @Transactional
    public List<Insight> discoverAnomalies(UUID tenantId, LocalDateTime since) {
        List<MetricData> data = metricDataRepository.findByTenantIdAndTimestampAfter(tenantId, since);
        List<Insight> newInsights = new ArrayList<>();
        
        // Mock statistical analysis
        if (!data.isEmpty()) {
            Insight insight = new Insight();
            insight.setTenantId(tenantId);
            insight.setType("ANOMALY");
            insight.setTitle("Unusual spike in " + data.get(0).getMetricName());
            insight.setDescription("Detected a 30% increase compared to normal baseline.");
            insight.setImpactScore(0.85);
            insight.setConfidenceScore(0.92);
            insight.setMetricNames(List.of(data.get(0).getMetricName()));
            
            // Enrich with AI
            InsightEnrichment enrichment = aiExplanationService.generateExplanationAndRecommendation(insight);
            insight.setExplanation(enrichment.getExplanation());
            insight.setRecommendedAction(enrichment.getRecommendedAction());
            
            insight = insightRepository.save(insight);
            newInsights.add(insight);
            
            // Evaluate alerts
            alertService.evaluateAlertRules(insight);
        }
        
        return newInsights;
    }
}
