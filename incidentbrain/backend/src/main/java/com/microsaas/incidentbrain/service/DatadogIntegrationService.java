package com.microsaas.incidentbrain.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatadogIntegrationService {

    private final RestClient restClient;

    @Value("${datadog.api.key:dummy}")
    private String apiKey;

    @Value("${datadog.app.key:dummy}")
    private String appKey;

    public List<String> fetchRecentLogs(String serviceName) {
        log.info("Fetching logs from Datadog for service: {}", serviceName);
        try {
            JsonNode response = restClient.get()
                .uri("https://api.datadoghq.com/api/v2/logs/events?filter[query]=service:" + serviceName)
                .header("DD-API-KEY", apiKey)
                .header("DD-APPLICATION-KEY", appKey)
                .retrieve()
                .body(JsonNode.class);

            List<String> logs = new ArrayList<>();
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    if (node.has("attributes") && node.get("attributes").has("message")) {
                         logs.add(node.get("attributes").get("message").asText());
                    }
                }
            }
            return logs;
        } catch (Exception e) {
            log.error("Failed to fetch Datadog logs for service: " + serviceName, e);
            throw new RuntimeException("Failed to fetch Datadog logs", e);
        }
    }
}
