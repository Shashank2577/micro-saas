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
        List<ContentChannel> channels = channelRepository.findByTenantId(tenantId);

        Map<String, Object> response = new HashMap<>();

        Map<String, Map<String, Object>> roiByChannel = new HashMap<>();

        for(ContentChannel channel: channels) {
            Map<String, Object> channelStats = new HashMap<>();

            List<BusinessOutcome> channelOutcomes = outcomes.stream().filter(o -> o.getChannelId().equals(channel.getId())).toList();
            List<ContentPerformance> channelPerformances = performances.stream().filter(p -> p.getChannelId().equals(channel.getId())).toList();

            BigDecimal channelTotalValue = channelOutcomes.stream()
                .map(BusinessOutcome::getAttributedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            long channelTotalViews = channelPerformances.stream()
                    .mapToLong(ContentPerformance::getViews)
                    .sum();

            channelStats.put("totalValue", channelTotalValue);
            channelStats.put("totalViews", channelTotalViews);
            channelStats.put("platform", channel.getPlatform().toString());
            channelStats.put("valuePerView", channelTotalViews > 0 ? channelTotalValue.divide(new BigDecimal(channelTotalViews), 4, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO);

            roiByChannel.put(channel.getName(), channelStats);
        }

        BigDecimal totalValue = outcomes.stream()
                .map(BusinessOutcome::getAttributedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalViews = performances.stream()
                .mapToLong(ContentPerformance::getViews)
                .sum();

        response.put("totalValue", totalValue);
        response.put("totalViews", totalViews);
        response.put("valuePerView", totalViews > 0 ? totalValue.divide(new BigDecimal(totalViews), 4, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO);
        response.put("roiByChannel", roiByChannel);

        return response;
    }

    public List<ContentInsight> getInsights(UUID tenantId) {
        List<ContentInsight> insights = insightRepository.findByTenantId(tenantId);
        if (insights.isEmpty()) {
            return insightAIService.generateInsights(tenantId);
        }
        return insights;
    }
}
