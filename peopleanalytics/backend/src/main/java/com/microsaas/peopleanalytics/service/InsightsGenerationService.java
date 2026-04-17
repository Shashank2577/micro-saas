package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.client.LiteLLMClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsightsGenerationService {

    private final LiteLLMClient llmClient;

    public String generatePredictiveInsights(UUID tenantId) {
        String prompt = "Generate a brief summary of organizational health based on recent HR data indicating steady retention but dropping engagement.";
        return llmClient.getInsights(prompt);
    }
}
