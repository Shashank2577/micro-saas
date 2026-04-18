package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.LogisticsException;
import com.microsaas.logisticsai.repository.LogisticsExceptionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import java.util.UUID;

@Service
public class ExceptionAgentService {

    private final LogisticsExceptionRepository repository;
    private final RestTemplate restTemplate;
    private final EventPublisherService eventPublisher;

    @Value("${cc.ai.gateway-url}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key}")
    private String aiApiKey;

    public ExceptionAgentService(LogisticsExceptionRepository repository, EventPublisherService eventPublisher) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<LogisticsException> getAllExceptions() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public LogisticsException reportException(LogisticsException exception) {
        exception.setTenantId(TenantContext.require());
        exception.setStatus("OPEN");
        LogisticsException analyzed = analyzeExceptionWithAI(exception);
        return repository.save(analyzed);
    }

    @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
    )
    public LogisticsException analyzeExceptionWithAI(LogisticsException exception) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", "claude-sonnet-4-6");
        request.put("messages", List.of(
            Map.of("role", "system", "content", "You are an AI logistics assistant. Determine the severity (LOW, MEDIUM, HIGH) and suggest a recommended action for the following exception."),
            Map.of("role", "user", "content", exception.getDescription())
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl + "/v1/chat/completions", entity, Map.class);
        
        // Simplified parsing
        if (response.getBody() != null && response.getBody().containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                String content = (String) message.get("content");
                
                if (content.toLowerCase().contains("high")) exception.setSeverity("HIGH");
                else if (content.toLowerCase().contains("medium")) exception.setSeverity("MEDIUM");
                else exception.setSeverity("LOW");
                
                exception.setRecommendedAction(content);
            }
        }
        return exception;
    }

    @Recover
    public LogisticsException recoverAiAnalysis(Exception e, LogisticsException exception) {
        exception.setSeverity("MEDIUM");
        exception.setRecommendedAction("Review manually. AI analysis failed: " + e.getMessage());
        return exception;
    }

    @Transactional
    public LogisticsException resolveException(UUID id) {
        LogisticsException exception = repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Exception not found"));
        exception.setStatus("RESOLVED");
        
        LogisticsException saved = repository.save(exception);
        
        eventPublisher.publishEvent("exception.resolved", Map.of(
            "exceptionId", saved.getId(),
            "status", saved.getStatus()
        ));
        
        return saved;
    }
}
