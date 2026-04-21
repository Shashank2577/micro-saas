package com.microsaas.experimentengine.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.error.CcErrorCodes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HypothesisService {

    private final AiService aiService;

    public HypothesisService(AiService aiService) {
        this.aiService = aiService;
    }

    public String generateHypothesis(String currentProblem, String proposedSolution) {
        if (currentProblem == null || proposedSolution == null) {
            throw CcErrorCodes.validationError("currentProblem and proposedSolution are required");
        }

        String prompt = String.format(
            "You are an expert product manager specializing in A/B testing and experimentation. " +
            "Based on the following problem and proposed solution, generate a formal, well-structured hypothesis for an A/B test. " +
            "The hypothesis should follow a format like: 'If we [change], then [impact], because [rationale].'\n\n" +
            "Problem: %s\n" +
            "Proposed Solution: %s",
            currentProblem, proposedSolution
        );

        ChatRequest request = new ChatRequest(
                "gpt-4o",
                List.of(new ChatMessage("user", prompt)),
                0.7,
                200
        );

        ChatResponse response = aiService.chat(request);
        return response.content();
    }
}
