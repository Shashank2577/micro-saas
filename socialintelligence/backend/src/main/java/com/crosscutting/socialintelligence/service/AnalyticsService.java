package com.crosscutting.socialintelligence.service;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.AudienceDemographic;
import com.crosscutting.socialintelligence.domain.ContentAnalysis;
import com.crosscutting.socialintelligence.repository.AudienceDemographicRepository;
import com.crosscutting.socialintelligence.repository.ContentAnalysisRepository;
import com.crosscutting.socialintelligence.client.LiteLLMClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AudienceDemographicRepository demographicRepository;
    private final ContentAnalysisRepository contentRepository;
    private final LiteLLMClient llmClient;

    public AnalyticsService(AudienceDemographicRepository demographicRepository,
                            ContentAnalysisRepository contentRepository,
                            LiteLLMClient llmClient) {
        this.demographicRepository = demographicRepository;
        this.contentRepository = contentRepository;
        this.llmClient = llmClient;
    }

    public List<AudienceDemographic> getAggregatedDemographics(String tenantId) {
        return demographicRepository.findByTenantId(tenantId);
    }

    public List<ContentAnalysis> getTopPerformingContent(String tenantId, int limit) {
        List<ContentAnalysis> content = contentRepository.findByTenantIdOrderByLikesDesc(tenantId);
        return content.stream().limit(limit).collect(Collectors.toList());
    }

    public String analyzeContentPatterns(String tenantId) {
        List<ContentAnalysis> topContent = getTopPerformingContent(tenantId, 5);
        if (topContent.isEmpty()) return "No content available for analysis.";

        String data = topContent.stream()
                .map(c -> "Type: " + c.getContentType() + ", Likes: " + c.getLikes())
                .collect(Collectors.joining("; "));

        String prompt = "Analyze the following top-performing posts: " + data + ". Identify common patterns that contribute to their success.";
        return llmClient.generateCompletion(prompt);
    }
}
