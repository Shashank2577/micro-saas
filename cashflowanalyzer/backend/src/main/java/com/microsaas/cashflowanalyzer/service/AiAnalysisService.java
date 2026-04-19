package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final AiService aiService;
    private static final String DEFAULT_MODEL = "gpt-4";

    public String analyze(String context) {
        String prompt = "Analyze the following cashflow context and provide insights: " + context;
        ChatRequest request = new ChatRequest(
            DEFAULT_MODEL,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1024
        );
        ChatResponse response = aiService.chat(request);
        return response.content();
    }

    public List<String> getRecommendations(String context) {
        String prompt = "Provide actionable cashflow recommendations based on this context: " + context;
        ChatRequest request = new ChatRequest(
            DEFAULT_MODEL,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1024
        );
        ChatResponse response = aiService.chat(request);
        return List.of(response.content().split("\n"));
    }
}
