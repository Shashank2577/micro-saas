package com.crosscutting.starter.ai;

import com.crosscutting.starter.error.CcErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final RestClient restClient;

    public AiService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Send a chat completion request to the LiteLLM gateway.
     */
    public ChatResponse chat(ChatRequest request) {
        log.debug("Sending chat request to model={}", request.model());

        Map<String, Object> body = buildChatBody(request);

        Map<String, Object> response = restClient.post()
                .uri("/chat/completions")
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null) {
            throw CcErrorCodes.internalError("Empty response from AI gateway");
        }

        return parseChatResponse(response);
    }

    /**
     * Generate embeddings for the given text.
     */
    @SuppressWarnings("unchecked")
    public List<Double> embed(String model, String text) {
        log.debug("Sending embed request to model={}", model);

        Map<String, Object> body = Map.of(
                "model", model,
                "input", text
        );

        Map<String, Object> response = restClient.post()
                .uri("/embeddings")
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null) {
            throw CcErrorCodes.internalError("Empty response from AI gateway");
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        if (data == null || data.isEmpty()) {
            throw CcErrorCodes.internalError("No embedding data in response");
        }

        List<Number> embedding = (List<Number>) data.get(0).get("embedding");
        return embedding.stream().map(Number::doubleValue).toList();
    }

    /**
     * List available models from the gateway.
     */
    @SuppressWarnings("unchecked")
    public List<String> listModels() {
        log.debug("Listing available models");

        Map<String, Object> response = restClient.get()
                .uri("/models")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null) {
            throw CcErrorCodes.internalError("Empty response from AI gateway");
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        if (data == null) {
            return List.of();
        }

        return data.stream()
                .map(m -> (String) m.get("id"))
                .toList();
    }

    private Map<String, Object> buildChatBody(ChatRequest request) {
        List<Map<String, String>> messages = request.messages().stream()
                .map(m -> Map.of("role", m.role(), "content", m.content()))
                .toList();

        Map<String, Object> body = new java.util.HashMap<>();
        body.put("model", request.model());
        body.put("messages", messages);
        if (request.temperature() != null) {
            body.put("temperature", request.temperature());
        }
        if (request.maxTokens() != null) {
            body.put("max_tokens", request.maxTokens());
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private ChatResponse parseChatResponse(Map<String, Object> response) {
        String id = (String) response.get("id");
        String model = (String) response.get("model");

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        String content = "";
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message != null) {
                content = (String) message.get("content");
            }
        }

        Map<String, Object> usageMap = (Map<String, Object>) response.get("usage");
        ChatResponse.Usage usage = new ChatResponse.Usage(0, 0, 0);
        if (usageMap != null) {
            usage = new ChatResponse.Usage(
                    ((Number) usageMap.get("prompt_tokens")).intValue(),
                    ((Number) usageMap.get("completion_tokens")).intValue(),
                    ((Number) usageMap.get("total_tokens")).intValue()
            );
        }

        return new ChatResponse(id, model, content, usage);
    }
}
