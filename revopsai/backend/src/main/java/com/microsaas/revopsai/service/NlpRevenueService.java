package com.microsaas.revopsai.service;

import com.microsaas.revopsai.service.LiteLLMClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NlpRevenueService {
    private final LiteLLMClient liteLLMClient;

    public String analyzeRevenueQuery(String query) {
        String prompt = "You are an AI revenue operations platform. Answer the following revenue question: " + query;
        return liteLLMClient.generateText(prompt, "claude-3-haiku-20240307");
    }
}
