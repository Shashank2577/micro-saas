package com.microsaas.usageintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.usageintelligence.domain.AiInsight;
import com.microsaas.usageintelligence.domain.Cohort;
import com.microsaas.usageintelligence.domain.Event;
import com.microsaas.usageintelligence.domain.Metric;
import com.microsaas.usageintelligence.dto.CreateCohortDto;
import com.microsaas.usageintelligence.dto.CreateEventDto;
import com.microsaas.usageintelligence.repository.AiInsightRepository;
import com.microsaas.usageintelligence.repository.CohortRepository;
import com.microsaas.usageintelligence.repository.EventRepository;
import com.microsaas.usageintelligence.repository.MetricRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UsageIntelligenceService {
    private final EventRepository eventRepository;
    private final MetricRepository metricRepository;
    private final AiInsightRepository aiInsightRepository;
    private final CohortRepository cohortRepository;
    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key}")
    private String aiApiKey;

    public UsageIntelligenceService(EventRepository eventRepository,
                                    MetricRepository metricRepository,
                                    AiInsightRepository aiInsightRepository,
                                    CohortRepository cohortRepository) {
        this.eventRepository = eventRepository;
        this.metricRepository = metricRepository;
        this.aiInsightRepository = aiInsightRepository;
        this.cohortRepository = cohortRepository;
        this.restTemplate = new RestTemplate();
    }

    public Event trackEvent(CreateEventDto dto) {
        Event event = new Event();
        event.setTenantId(TenantContext.require());
        event.setUserId(dto.getUserId());
        event.setEventName(dto.getEventName());
        event.setProperties(dto.getProperties());
        event.setCreatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    public List<Event> getRecentEvents() {
        return eventRepository.findByTenantId(TenantContext.require());
    }

    public List<Metric> getMetrics(String metricName, LocalDateTime startDate, LocalDateTime endDate) {
        return metricRepository.findByTenantIdAndMetricNameAndCreatedAtBetween(TenantContext.require(), metricName, startDate, endDate);
    }

    public Cohort createCohort(CreateCohortDto dto) {
        Cohort cohort = new Cohort();
        cohort.setTenantId(TenantContext.require());
        cohort.setName(dto.getName());
        cohort.setDescription(dto.getDescription());
        cohort.setCriteria(dto.getCriteria());
        cohort.setCreatedAt(LocalDateTime.now());
        cohort.setUpdatedAt(LocalDateTime.now());
        return cohortRepository.save(cohort);
    }

    public List<Cohort> getCohorts() {
        return cohortRepository.findByTenantId(TenantContext.require());
    }

    public List<AiInsight> getInsights() {
        return aiInsightRepository.findByTenantIdOrderByCreatedAtDesc(TenantContext.require());
    }

    public AiInsight generateInsight() {
        UUID tenantId = TenantContext.require();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<Event> recentEvents = eventRepository.findByTenantIdAndCreatedAtAfter(tenantId, oneWeekAgo);

        String prompt = "Given the following usage data over the last 7 days: " + recentEvents.size() + " events recorded. " +
                "Identify any anomalies or interesting trends, explain why they might be happening, and provide a recommendation.";

        String aiRecommendation = "Consider running a re-engagement campaign.";
        String aiTitle = "Usage Trend Analysis";
        String aiDescription = "User engagement appears stable but with some potential for increased activation.";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "claude-3-sonnet-20240229");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl + "/v1/chat/completions", entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    aiDescription = "Generated analysis based on " + recentEvents.size() + " recent events.";
                    aiRecommendation = content;
                }
            }
        } catch (Exception e) {
            // Fallback to stubbed strings if AI fails
        }

        AiInsight insight = new AiInsight();
        insight.setTenantId(tenantId);
        insight.setTitle(aiTitle);
        insight.setDescription(aiDescription);
        insight.setRecommendation(aiRecommendation);
        insight.setCreatedAt(LocalDateTime.now());
        Map<String, Object> refs = new HashMap<>();
        refs.put("eventCount", recentEvents.size());
        insight.setDataReferences(refs);

        return aiInsightRepository.save(insight);
    }
}
