package com.microsaas.incidentbrain.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubIntegrationService {

    private final RestClient restClient;

    @Value("${github.token:dummy}")
    private String githubToken;

    public String fetchRecentCommits(String repo) {
        log.info("Fetching recent commits from GitHub for repo: {}", repo);
        try {
            JsonNode[] response = restClient.get()
                .uri("https://api.github.com/repos/" + repo + "/commits")
                .header("Authorization", "Bearer " + githubToken)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .body(JsonNode[].class);

            StringBuilder sb = new StringBuilder();
            if (response != null) {
                int count = 0;
                for (JsonNode node : response) {
                    if (count >= 5) break; // Limit to recent 5
                    if (node.has("commit") && node.get("commit").has("message")) {
                        sb.append("Commit ").append(node.get("sha").asText().substring(0, 7)).append(": ")
                          .append(node.get("commit").get("message").asText()).append("\n");
                    }
                    count++;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("Failed to fetch GitHub commits for repo: " + repo, e);
            throw new RuntimeException("Failed to fetch commits", e);
        }
    }
}
