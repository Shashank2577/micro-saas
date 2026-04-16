package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.*;
import com.microsaas.creatoranalytics.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final ContentChannelRepository channelRepository;
    private final ContentPerformanceRepository performanceRepository;
    private final BusinessOutcomeRepository outcomeRepository;
    private final ContentInsightRepository insightRepository;
    private final InsightAIService insightAIService;

    public ContentChannel createChannel(UUID tenantId, ContentChannel channel) {
        channel.setTenantId(tenantId);
        if (channel.getTrackedSince() == null) {
            channel.setTrackedSince(LocalDate.now());
        }
        return channelRepository.save(channel);
    }

    public List<ContentChannel> getChannels(UUID tenantId) {
        return channelRepository.findByTenantId(tenantId);
    }

    public ContentPerformance ingestPerformance(UUID tenantId, UUID channelId, ContentPerformance performance) {
        ContentChannel channel = channelRepository.findByIdAndTenantId(channelId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        performance.setTenantId(tenantId);
        performance.setChannelId(channelId);
        if (performance.getMeasuredAt() == null) {
            performance.setMeasuredAt(LocalDate.now());
        }
        return performanceRepository.save(performance);
    }

    public List<ContentPerformance> getTopContent(UUID tenantId, UUID channelId) {
        List<ContentPerformance> performances = performanceRepository.findByTenantIdAndChannelId(tenantId, channelId);
        return performances.stream()
                .sorted(Comparator.comparing(ContentPerformance::getViews).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public BusinessOutcome recordOutcome(UUID tenantId, BusinessOutcome outcome) {
        outcome.setTenantId(tenantId);
        if (outcome.getOccurredAt() == null) {
            outcome.setOccurredAt(LocalDateTime.now());
        }
        return outcomeRepository.save(outcome);
    }

    public Map<String, Object> getRoiAnalytics(UUID tenantId) {
        List<BusinessOutcome> outcomes = outcomeRepository.findByTenantId(tenantId);
        List<ContentPerformance> performances = performanceRepository.findByTenantId(tenantId);

        BigDecimal totalValue = outcomes.stream()
                .map(BusinessOutcome::getAttributedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalViews = performances.stream()
                .mapToLong(ContentPerformance::getViews)
                .sum();

        Map<String, Object> roi = new HashMap<>();
        roi.put("totalValue", totalValue);
        roi.put("totalViews", totalViews);
        roi.put("valuePerView", totalViews > 0 ? totalValue.divide(new BigDecimal(totalViews), 4, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO);

        return roi;
    }

    public List<ContentInsight> getInsights(UUID tenantId) {
        // Mock generation or fetch existing
        List<ContentInsight> insights = insightRepository.findByTenantId(tenantId);
        if (insights.isEmpty()) {
            return insightAIService.generateInsights(tenantId);
        }
        return insights;
    }
}
