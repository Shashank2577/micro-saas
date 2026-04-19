package com.microsaas.financenarrator.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiOrchestrationService {
    private final AiService aiService;

    public String analyze(Map<String, Object> request) {
        log.info("Analyzing via LiteLLM: {}", request);
        ChatRequest chatReq = new ChatRequest("claude-sonnet-4-6", List.of(new ChatMessage("user", "Perform deep financial analysis on the following data: " + request.toString())), 0.7, 1000);
        ChatResponse res = aiService.chat(chatReq);
        return res.content();
    }

    public String recommend(Map<String, Object> request) {
        log.info("Generating recommendations via LiteLLM: {}", request);
        ChatRequest chatReq = new ChatRequest("claude-sonnet-4-6", List.of(new ChatMessage("user", "Provide executive recommendations based on: " + request.toString())), 0.7, 1000);
        ChatResponse res = aiService.chat(chatReq);
        return res.content();
    }
}
