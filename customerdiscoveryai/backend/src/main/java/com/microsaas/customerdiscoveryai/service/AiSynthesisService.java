package com.microsaas.customerdiscoveryai.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.customerdiscoveryai.model.Insight;
import com.microsaas.customerdiscoveryai.model.Interview;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AiSynthesisService {

    private final AiService aiService;
    private final ObjectMapper objectMapper;

    public AiSynthesisService(AiService aiService, ObjectMapper objectMapper) {
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    public List<Insight> synthesizeInsights(List<Interview> interviews, UUID projectId, UUID tenantId) {
        if (interviews == null || interviews.isEmpty()) {
            return new ArrayList<>();
        }

        String transcriptsContent = interviews.stream()
                .filter(i -> i.getTranscript() != null && !i.getTranscript().isEmpty())
                .map(i -> "Participant: " + i.getParticipantName() + "\nTranscript: " + i.getTranscript())
                .collect(Collectors.joining("\n\n---\n\n"));

        String systemPrompt = "You are an expert UX researcher and product manager. Your task is to analyze the provided interview transcripts and extract the most important insights, themes, and contradictions. " +
                "Output your findings as a JSON array of objects. Each object must have the following fields: " +
                "'theme' (string, a short title for the insight), " +
                "'description' (string, detailed explanation), " +
                "'confidenceScore' (number between 0.0 and 1.0, representing how strong the evidence is), " +
                "'supportingQuotes' (array of strings, verbatim quotes from the transcripts that support the insight). " +
                "Do not include any other text besides the JSON array.";

        ChatRequest request = new ChatRequest(
            "gpt-4o-mini",
            List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", "Here are the interview transcripts:\n\n" + transcriptsContent)
            ),
            null,
            null
        );

        ChatResponse response = aiService.chat(request);
        String jsonContent = response.content().replaceAll("```json|```", "").trim();

        try {
            List<Map<String, Object>> insightMaps = objectMapper.readValue(jsonContent, new TypeReference<List<Map<String, Object>>>() {});
            return insightMaps.stream().map(map -> {
                try {
                    Insight insight = new Insight();
                    insight.setTenantId(tenantId);
                    insight.setProjectId(projectId);
                    insight.setTheme((String) map.get("theme"));
                    insight.setDescription((String) map.get("description"));
                    insight.setConfidenceScore(BigDecimal.valueOf(((Number) map.get("confidenceScore")).doubleValue()));
                    insight.setSupportingQuotes(objectMapper.writeValueAsString(map.get("supportingQuotes")));
                    return insight;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to serialize supporting quotes", e);
                }
            }).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response into insights: " + jsonContent, e);
        }
    }

    public String generateReport(List<Insight> insights, String projectName, String projectDescription) {
        if (insights == null || insights.isEmpty()) {
            return "No insights available to generate a report.";
        }

        String insightsContent = insights.stream()
                .map(i -> "- Theme: " + i.getTheme() + "\n  Description: " + i.getDescription() + "\n  Confidence: " + i.getConfidenceScore())
                .collect(Collectors.joining("\n\n"));

        String systemPrompt = "You are a senior user researcher. Write a comprehensive Markdown research report based on the provided insights. " +
                "The report should include an Executive Summary, Key Findings (detailed with the themes), and Recommendations. " +
                "Structure it professionally. Use bolding and bullet points to make it readable.";

        ChatRequest request = new ChatRequest(
            "gpt-4o-mini",
            List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", String.format("Project Name: %s\nProject Description: %s\n\nInsights:\n%s", projectName, projectDescription, insightsContent))
            ),
            null,
            null
        );

        return aiService.chat(request).content();
    }
}
