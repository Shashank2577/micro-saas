package com.microsaas.voiceagentbuilder.service;

import com.microsaas.voiceagentbuilder.dto.AiSimulationRequest;
import com.microsaas.voiceagentbuilder.dto.AiSimulationResponse;
import com.microsaas.voiceagentbuilder.model.Agent;
import com.microsaas.voiceagentbuilder.model.KnowledgeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiSimulationService {
    private final AgentService agentService;
    private final KnowledgeDocumentService knowledgeService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cc.ai.gateway-url:http://litellm:4000}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key:sk-1234}")
    private String apiKey;

    public AiSimulationResponse simulate(UUID agentId, AiSimulationRequest request) {
        Agent agent = agentService.getAgent(agentId);
        List<KnowledgeDocument> documents = knowledgeService.getDocuments(agentId);

        String context = documents.stream()
                .map(KnowledgeDocument::getContent)
                .collect(Collectors.joining("\n\n"));

        String systemMessageContent = agent.getSystemPrompt();
        if (!context.isEmpty()) {
            systemMessageContent += "\n\nKnowledge Base:\n" + context;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", systemMessageContent),
                Map.of("role", "user", "content", request.getMessage())
        );
        requestBody.put("messages", messages);

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(gatewayUrl + "/v1/chat/completions", entity, Map.class);
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String reply = (String) message.get("content");
                    return new AiSimulationResponse(reply, null);
                }
            }
        } catch (Exception e) {
            // Log error
        }

        return new AiSimulationResponse("Sorry, I am unable to process your request at this time.", null);
    }
}
