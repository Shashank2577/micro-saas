package com.microsaas.customersuccessos.service;

import com.microsaas.customersuccessos.model.*;
import com.microsaas.customersuccessos.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

@Service
public class QbrGenerationService {

    @Autowired
    private QbrDeckRepository qbrDeckRepository;

    @Autowired
    private CustomerSuccessService customerSuccessService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url:http://litellm:4000/v1/chat/completions}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:dummy-key}")
    private String aiApiKey;

    public List<QbrDeck> getQbrDecks(UUID accountId) {
        return qbrDeckRepository.findByTenantIdAndAccountIdOrderByGeneratedAtDesc(TenantContext.require(), accountId);
    }

    public QbrDeck generateQbr(UUID accountId) {
        Account account = customerSuccessService.getAccount(accountId);
        HealthScore healthScore = customerSuccessService.getLatestHealthScore(accountId);
        List<ExpansionOpportunity> opps = customerSuccessService.getExpansionOpportunities(accountId);

        QbrDeck deck = new QbrDeck();
        deck.setId(UUID.randomUUID());
        deck.setTenantId(TenantContext.require());
        deck.setAccountId(accountId);
        deck.setStatus("GENERATING");
        deck.setGeneratedAt(LocalDateTime.now());
        qbrDeckRepository.save(deck);

        try {
            String prompt = String.format("Generate a QBR summary for %s. Subscription: %s. Current Health Score: %d. Recent expansion opps: %d.", 
                account.getName(), account.getSubscriptionTier(), 
                healthScore != null ? healthScore.getScore() : 0, 
                opps.size());

            String result = callAiService(prompt);
            deck.setContent(result);
            deck.setStatus("COMPLETED");
        } catch (Exception e) {
            deck.setStatus("FAILED");
            deck.setContent(e.getMessage());
        }

        return qbrDeckRepository.save(deck);
    }

    private String callAiService(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl, requestEntity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    return (String) messageObj.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("AI request failed: " + e.getMessage());
            return "Generated QBR summary based on prompt: " + prompt; // Fallback
        }
        return "Failed to generate QBR.";
    }
}
