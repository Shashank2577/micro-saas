package com.microsaas.datacatalogai.ai;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    private final LiteLLMClient liteLLMClient;

    @Retry(name = "aiService", fallbackMethod = "fallbackGenerateCompletion")
    @CircuitBreaker(name = "aiService", fallbackMethod = "fallbackGenerateCompletion")
    public String generateAutoDocumentation(String schemaDetails) {
        String systemPrompt = "You are an expert data cataloger. Given the schema details, write a concise, clear description of the table and its contents. Reference at least one column name. Use markdown.";
        String prompt = "Please document the following dataset: " + schemaDetails;
        return liteLLMClient.generateCompletion(prompt, systemPrompt);
    }

    @Retry(name = "aiService", fallbackMethod = "fallbackDetectPii")
    @CircuitBreaker(name = "aiService", fallbackMethod = "fallbackDetectPii")
    public String detectPii(String columnName, String sampleData) {
        String systemPrompt = "You are a data compliance expert. Given a column name and sample data, determine the PII category. Respond with ONLY the category name (e.g., EMAIL, SSN, PHONE, ADDRESS, NAME, NONE) and the confidence score (0.0 to 1.0) separated by a comma. Example: EMAIL,0.95";
        String prompt = "Column: " + columnName + "\nSample data: " + sampleData;
        return liteLLMClient.generateCompletion(prompt, systemPrompt);
    }

    @Retry(name = "aiService", fallbackMethod = "fallbackGenerateEmbedding")
    @CircuitBreaker(name = "aiService", fallbackMethod = "fallbackGenerateEmbedding")
    public List<Double> getEmbedding(String text) {
        return liteLLMClient.generateEmbedding(text);
    }

    // Fallbacks
    public String fallbackGenerateCompletion(String schemaDetails, Throwable t) {
        return "Failed to generate documentation due to: " + t.getMessage();
    }

    public String fallbackDetectPii(String columnName, String sampleData, Throwable t) {
        return "NONE,0.0";
    }

    public List<Double> fallbackGenerateEmbedding(String text, Throwable t) {
        return List.of();
    }
}
