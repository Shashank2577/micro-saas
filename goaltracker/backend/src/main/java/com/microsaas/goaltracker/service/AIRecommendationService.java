package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Goal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    @Value("${ai.litellm.base-url}")
    private String liteLlmBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateMotivationNudge(Goal goal) {
        String prompt = String.format("Generate a short, motivational nudge for a user trying to save $%s for '%s' by %s. They currently have $%s saved.",
                goal.getTargetAmount(), goal.getTitle(), goal.getDeadline(), goal.getCurrentAmount());
        return callLiteLlm(prompt);
    }

    public String suggestInvestment(Goal goal) {
        String prompt = String.format("Suggest a general investment or savings vehicle (like HYSA, ETFs) for a '%s' goal of $%s to be achieved by %s.",
                goal.getCategory(), goal.getTargetAmount(), goal.getDeadline());
        return callLiteLlm(prompt);
    }

    private String callLiteLlm(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            Map<String, Object> response = restTemplate.postForObject(liteLlmBaseUrl + "/v1/chat/completions", request, Map.class);

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "Keep up the great work!";
        } catch (Exception e) {
            log.error("Failed to call LiteLLM: ", e);
            return "You're doing great, keep saving!";
        }
    }
}
