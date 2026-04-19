package com.microsaas.debtnavigator.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtAiService {
    private final AiService aiService;

    @Value("${cc.ai.default-model:claude-sonnet-4-6}")
    private String defaultModel;

    public String analyzeDebt(String prompt) {
        ChatRequest request = new ChatRequest(
            defaultModel,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1000
        );
        return aiService.chat(request).content();
    }

    public String getRecommendations(String prompt) {
        ChatRequest request = new ChatRequest(
            defaultModel,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1000
        );
        return aiService.chat(request).content();
    }
}
