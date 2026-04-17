package com.crosscutting.socialintelligence.service;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import com.crosscutting.socialintelligence.repository.GrowthRecommendationRepository;
import com.crosscutting.socialintelligence.client.LiteLLMClient;
import com.crosscutting.socialintelligence.dto.UnifiedMetrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AIRecommendationService {

    private final GrowthRecommendationRepository recommendationRepository;
    private final SocialMetricsAggregator metricsAggregator;
    private final LiteLLMClient llmClient;

    public AIRecommendationService(GrowthRecommendationRepository recommendationRepository,
                                   SocialMetricsAggregator metricsAggregator,
                                   LiteLLMClient llmClient) {
        this.recommendationRepository = recommendationRepository;
        this.metricsAggregator = metricsAggregator;
        this.llmClient = llmClient;
    }

    @Transactional
    public List<GrowthRecommendation> generateRecommendations(String tenantId) {
        UnifiedMetrics metrics = metricsAggregator.getUnifiedMetrics(tenantId, LocalDate.now().minusDays(30), LocalDate.now());

        String prompt = "Based on the following metrics over the last 30 days: Followers: " + metrics.getTotalFollowers() +
                ", Likes: " + metrics.getTotalLikes() + ", Avg Engagement: " + metrics.getAverageEngagementRate() +
                ", generate 3 specific, actionable growth recommendations in a simple comma-separated list of descriptions.";

        String response = llmClient.generateCompletion(prompt);
        String[] recs = response.split(",");

        for (String recDesc : recs) {
            if (!recDesc.trim().isEmpty()) {
                GrowthRecommendation rec = GrowthRecommendation.builder()
                        .tenantId(tenantId)
                        .platformName("ALL")
                        .recommendationType("CONTENT_STRATEGY")
                        .description(recDesc.trim())
                        .status("NEW")
                        .build();
                recommendationRepository.save(rec);
            }
        }
        return recommendationRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public List<GrowthRecommendation> getRecommendations(String tenantId) {
        return recommendationRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Transactional
    public GrowthRecommendation updateStatus(UUID id, String tenantId, String status) {
        GrowthRecommendation rec = recommendationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        rec.setStatus(status);
        return recommendationRepository.save(rec);
    }

    public String getGrowthProjections(String tenantId) {
        UnifiedMetrics metrics = metricsAggregator.getUnifiedMetrics(tenantId, LocalDate.now().minusDays(30), LocalDate.now());
        String prompt = "Given the past 30 days total followers: " + metrics.getTotalFollowers() +
                " and engagement rate: " + metrics.getAverageEngagementRate() +
                ", project the expected growth for the next 30 days and provide key drivers.";
        return llmClient.generateCompletion(prompt);
    }
}
