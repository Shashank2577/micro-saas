package com.microsaas.insightengine.service;

import com.microsaas.insightengine.dto.InsightEnrichment;
import com.microsaas.insightengine.entity.Insight;
import org.springframework.stereotype.Service;

@Service
public class AiExplanationService {

    public InsightEnrichment generateExplanationAndRecommendation(Insight insight) {
        // In a real implementation, this would call LiteLLM proxy
        // String prompt = "Explain this insight in business terms: " + insight.getTitle();
        // LLMResponse response = litellmClient.generate(prompt);
        
        InsightEnrichment enrichment = new InsightEnrichment();
        enrichment.setExplanation("AI Explanation: Based on recent metric data, " + insight.getTitle() + " has occurred. This could be due to seasonal variations or recent marketing campaigns.");
        enrichment.setRecommendedAction("AI Recommendation: Investigate the underlying causes by reviewing the segment breakdown and comparing with last month's performance.");
        
        return enrichment;
    }
}
