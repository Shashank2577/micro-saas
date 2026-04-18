package com.microsaas.insuranceai.service;

import com.microsaas.insuranceai.client.LiteLLMClient;
import com.microsaas.insuranceai.domain.Claim;
import com.microsaas.insuranceai.dto.FraudScoreResult;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Service
public class AIFraudService {

    private final LiteLLMClient liteLLMClient;
    private final ObjectMapper objectMapper;

    public AIFraudService(LiteLLMClient liteLLMClient) {
        this.liteLLMClient = liteLLMClient;
        this.objectMapper = new ObjectMapper();
    }

    public FraudScoreResult analyzeClaim(Claim claim) {
        String prompt = "Analyze the following claim details for potential fraud. Consider the amount, description, and incident timing. Provide a fraud score from 0 (lowest risk) to 100 (highest risk) and a brief reasoning. Output exactly in JSON format: {\"score\": number, \"reasoning\": string}. Details: Amount: " + claim.getAmount() + ", Description: " + claim.getDescription() + ", Incident Date: " + claim.getIncidentDate();
        
        String responseContent = liteLLMClient.generateCompletion(prompt);
        
        try {
            return objectMapper.readValue(responseContent, FraudScoreResult.class);
        } catch (Exception e) {
            System.err.println("Failed to parse LLM response: " + e.getMessage());
            FraudScoreResult fallback = new FraudScoreResult();
            fallback.setScore(new BigDecimal("50.0"));
            fallback.setReasoning("Fallback score due to parsing error.");
            return fallback;
        }
    }
}
