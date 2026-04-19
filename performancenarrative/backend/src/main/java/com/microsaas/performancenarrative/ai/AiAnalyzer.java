package com.microsaas.performancenarrative.ai;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@Service
public class AiAnalyzer {

    private final AiService aiService;
    private final ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(AiAnalyzer.class);

    public AiAnalyzer(AiService aiService, ObjectMapper mapper) {
        this.aiService = aiService;
        this.mapper = mapper;
    }

    public String analyzePerformance(String input) {
        ChatRequest request = new ChatRequest(
            "gpt-3.5-turbo",
            List.of(
                new ChatMessage("system", "You are an HR assistant. Analyze the following performance review and output ONLY valid JSON. Include an 'insights' field and a 'recommendation' field."),
                new ChatMessage("user", input)
            ),
            0.7,
            1000
        );

        int retries = 3;
        for (int i = 0; i < retries; i++) {
            try {
                log.info("Sending request to AiService. Attempt {}", i + 1);
                ChatResponse response = aiService.chat(request);
                String content = response.content();

                // Try parsing JSON to ensure guardrails
                try {
                    mapper.readTree(content);
                    return content;
                } catch (Exception parseEx) {
                    log.warn("Failed to parse JSON from AI response: {}", content);
                }
            } catch (Exception ex) {
                log.error("Error communicating with AI service", ex);
                try {
                    Thread.sleep((long) Math.pow(2, i) * 1000); // exponential backoff
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        log.error("AI Analysis failed after {} retries. Returning fallback.", retries);
        try {
            Map<String, String> fallback = new HashMap<>();
            fallback.put("insights", "Analysis unavailable.");
            fallback.put("recommendation", "Please review manually.");
            return mapper.writeValueAsString(fallback);
        } catch (JsonProcessingException e) {
            return "{\"insights\":\"Error\", \"recommendation\":\"Error\"}";
        }
    }
}
