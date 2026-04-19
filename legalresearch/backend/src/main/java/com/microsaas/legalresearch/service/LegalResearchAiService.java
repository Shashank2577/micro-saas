package com.microsaas.legalresearch.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.ai.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LegalResearchAiService {
    private final AiService aiService;

    public String analyze(Map<String, Object> payload) {
        String prompt = "Analyze the following legal research payload: " + payload.toString();
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), 0.7, 1000);
        ChatResponse response = aiService.chat(request);
        return response.content();
    }

    public String recommendations(Map<String, Object> payload) {
        String prompt = "Provide legal recommendations for the following context: " + payload.toString();
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), 0.7, 1000);
        ChatResponse response = aiService.chat(request);
        return response.content();
    }
}
