package com.microsaas.churnpredictor.service;

import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.entity.CustomerHealthScore;
import com.microsaas.churnpredictor.entity.InterventionPlaybook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiOfferService {

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    @Value("${cc.ai.default-model}")
    private String defaultModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateOffer(Customer customer, CustomerHealthScore healthScore, InterventionPlaybook playbook) {
        String prompt = String.format("Generate a personalized retention email for customer %s in %s. Their health score is %s. Offer them a %s.",
                customer.getName(), customer.getIndustry(), healthScore != null ? healthScore.getCompositeScore() : "unknown", playbook.getActionType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are an expert customer success manager. Output only the email body without headers."),
                Map.of("role", "user", "content", prompt)
        ));

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(gatewayUrl + "/chat/completions", new HttpEntity<>(requestBody, headers), Map.class);
            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Mock fallback if AI is down
            return "Subject: Special Offer for " + customer.getName() + "\n\nHi there,\nWe noticed you haven't been as active lately. We'd like to offer you a " + playbook.getActionType() + ". Let us know if you're interested!\n\nBest,\nThe ChurnPredictor Team";
        }
        return "Failed to generate offer.";
    }
}
