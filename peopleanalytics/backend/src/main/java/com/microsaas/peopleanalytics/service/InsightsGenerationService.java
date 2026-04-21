package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.tenancy.TenantContext;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightsGenerationService {
    private final AiService aiService;

    @Value("${cc.ai.default-model:claude-3-5-sonnet}")
    private String aiModel;

    @CircuitBreaker(name = "aiService")
    public String generateNarrative(String contextType, Map<String, Object> data) {
        log.info("Generating AI insights for: {}", contextType);

        String prompt = String.format(
            "Based on the following %s data, provide a concise narrative insight for the HR team: %s",
            contextType, data.toString()
        );

        try {
            ChatResponse response = aiService.chat(new ChatRequest(
                aiModel,
                List.of(new ChatMessage("user", prompt)),
                0.7,
                1000
            ));
            return response.content();
        } catch (Exception e) {
            log.error("Failed to generate AI insights", e);
            return "Unable to generate insights at this time.";
        }
    }
}
