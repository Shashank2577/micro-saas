package com.microsaas.insuranceai.service;

import com.microsaas.insuranceai.client.LiteLLMClient;
import com.microsaas.insuranceai.domain.Policy;
import com.microsaas.insuranceai.dto.RiskScoreResult;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Service
public class AIRiskService {

    private final LiteLLMClient liteLLMClient;
    private final ObjectMapper objectMapper;

    public AIRiskService(LiteLLMClient liteLLMClient) {
        this.liteLLMClient = liteLLMClient;
        this.objectMapper = new ObjectMapper();
    }

    public RiskScoreResult analyzePolicy(Policy policy) {
        String prompt = "Analyze the following policy details and customer profile for underwriting risk. Consider the policy type, premium, and coverage dates. Provide a risk score from 0 (lowest risk) to 100 (highest risk) and a brief list of risk factors. Output exactly in JSON format: {\"score\": number, \"factors\": string}. Details: Type: " + policy.getPolicyType() + ", Premium: " + policy.getPremiumAmount() + ", Customer: " + policy.getCustomerName();
        
        String responseContent = liteLLMClient.generateCompletion(prompt);
        
        try {
            return objectMapper.readValue(responseContent, RiskScoreResult.class);
        } catch (Exception e) {
            System.err.println("Failed to parse LLM response: " + e.getMessage());
            RiskScoreResult fallback = new RiskScoreResult();
            fallback.setScore(new BigDecimal("50.0"));
            fallback.setFactors("Fallback score due to parsing error.");
            return fallback;
        }
    }
}
