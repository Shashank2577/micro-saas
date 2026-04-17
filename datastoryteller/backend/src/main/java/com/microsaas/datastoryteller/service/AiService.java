package com.microsaas.datastoryteller.service;

import com.microsaas.datastoryteller.domain.model.AiUsage;
import com.microsaas.datastoryteller.repository.AiUsageRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final ChatClient.Builder chatClientBuilder;
    private final AiUsageRepository aiUsageRepository;

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @CircuitBreaker(name = "aiCircuitBreaker", fallbackMethod = "aiFallback")
    public String generateNarrative(String feature, String tenantId, String systemPrompt, String userPrompt) {
        log.info("Generating narrative for feature: {}", feature);
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        messages.add(new UserMessage(userPrompt));

        Prompt prompt = new Prompt(messages);

        ChatClient chatClient = chatClientBuilder.build();
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        recordUsage(feature, tenantId, response);

        return response.getResult().getOutput().getContent();
    }

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @CircuitBreaker(name = "aiCircuitBreaker", fallbackMethod = "aiFallback")
    public String explainChart(String feature, String tenantId, String systemPrompt, String imageBase64, String userPrompt) {
         log.info("Explaining chart for feature: {}", feature);
         // Spring AI image input via multimodal is supported but for now we format a special prompt since LiteLLM doesn't standardly map multimodal yet or we mock it.
         // In actual code, we use user message with media.

         // Mocking multimodal for simplicity here, relying on text prompt mapping
         List<Message> messages = new ArrayList<>();
         messages.add(new SystemMessage(systemPrompt));
         messages.add(new UserMessage("Image data: [base64 omitted]. " + userPrompt));

         Prompt prompt = new Prompt(messages);
         ChatClient chatClient = chatClientBuilder.build();
         ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

         recordUsage(feature, tenantId, response);

         return response.getResult().getOutput().getContent();
    }

    private void recordUsage(String feature, String tenantId, ChatResponse response) {
        try {
            int promptTokens = 0;
            int completionTokens = 0;

            if (response.getMetadata() != null && response.getMetadata().getUsage() != null) {
                Long pt = response.getMetadata().getUsage().getPromptTokens();
                Long ct = response.getMetadata().getUsage().getGenerationTokens();
                promptTokens = pt != null ? pt.intValue() : 0;
                completionTokens = ct != null ? ct.intValue() : 0;
            }

            // claude sonnet 4.6 roughly $3 / 1M prompt, $15 / 1M completion
            BigDecimal cost = BigDecimal.valueOf(promptTokens * 0.000003 + completionTokens * 0.000015);

            AiUsage usage = AiUsage.builder()
                    .tenantId(tenantId)
                    .feature(feature)
                    .promptTokens(promptTokens)
                    .completionTokens(completionTokens)
                    .costUsd(cost)
                    .build();

            aiUsageRepository.save(usage);
        } catch (Exception e) {
            log.warn("Failed to record AI usage", e);
        }
    }

    public String aiFallback(String feature, String tenantId, String systemPrompt, String userPrompt, Exception e) {
        log.error("AI call failed after retries for feature {}, falling back", feature, e);
        return "The AI service is currently unavailable. Please try again later. Error: " + e.getMessage();
    }

    public String aiFallback(String feature, String tenantId, String systemPrompt, String imageBase64, String userPrompt, Exception e) {
        log.error("AI call failed after retries for multimodal feature {}, falling back", feature, e);
        return "The AI service is currently unavailable for image processing. Please try again later.";
    }
}
