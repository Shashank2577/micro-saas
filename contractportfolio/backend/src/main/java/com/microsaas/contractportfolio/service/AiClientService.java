package com.microsaas.contractportfolio.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiClientService {

    private final AiService aiService;

    public String analyze(String prompt) {
        try {
            ChatRequest req = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), 0.3, 1000);
            return aiService.chat(req).content();
        } catch (Exception e) {
            return "{\"error\": \"AI analysis failed: " + e.getMessage() + "\"}";
        }
    }

    public String recommend(String prompt) {
        try {
            ChatRequest req = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), 0.5, 1000);
            return aiService.chat(req).content();
        } catch (Exception e) {
            return "{\"error\": \"AI recommendation failed: " + e.getMessage() + "\"}";
        }
    }
}
