package com.microsaas.queryoptimizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.queryoptimizer.domain.IndexSuggestion;
import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.domain.QueryRecommendation;
import com.microsaas.queryoptimizer.repository.IndexSuggestionRepository;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import com.microsaas.queryoptimizer.repository.QueryRecommendationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AIRecommendationService {
    
    private final QueryFingerprintRepository fingerprintRepository;
    private final QueryRecommendationRepository recommendationRepository;
    private final IndexSuggestionRepository indexSuggestionRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${cc.ai.gateway-url:http://localhost:4000}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key:sk-local-dev-key}")
    private String apiKey;

    public AIRecommendationService(QueryFingerprintRepository fingerprintRepository,
                                   QueryRecommendationRepository recommendationRepository,
                                   IndexSuggestionRepository indexSuggestionRepository,
                                   RestTemplate restTemplate,
                                   ObjectMapper objectMapper) {
        this.fingerprintRepository = fingerprintRepository;
        this.recommendationRepository = recommendationRepository;
        this.indexSuggestionRepository = indexSuggestionRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void analyzeFingerprint(UUID tenantId, UUID fingerprintId) {
        QueryFingerprint fingerprint = fingerprintRepository.findByIdAndTenantId(fingerprintId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Fingerprint not found"));

        try {
            String prompt = "You are an expert database administrator. Analyze the following SQL query and provide: " +
                    "1. 'recommendations': array of objects (type, description, confidence, impact). " +
                    "2. 'index_suggestions': array of objects (table, columns, sql, estimated_improvement). " +
                    "3. 'rewritten_query': string (a safely rewritten version of the query with optimizations and safety validation). " +
                    "Format the response strictly as JSON. " +
                    "Query: " + fingerprint.getNormalizedQuery();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-sonnet-4-6");
            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", prompt)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(gatewayUrl + "/v1/chat/completions", entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String aiContent = rootNode.path("choices").get(0).path("message").path("content").asText();

            if (aiContent.contains("```json")) {
                aiContent = aiContent.substring(aiContent.indexOf("```json") + 7, aiContent.lastIndexOf("```"));
            }

            JsonNode aiJson = objectMapper.readTree(aiContent);
            
            if (aiJson.has("recommendations")) {
                for (JsonNode recNode : aiJson.get("recommendations")) {
                    QueryRecommendation recommendation = new QueryRecommendation(
                            tenantId,
                            fingerprint,
                            recNode.path("type").asText("GENERAL"),
                            recNode.path("description").asText("AI Suggestion"),
                            recNode.path("confidence").asDouble(0.8),
                            recNode.path("impact").asText("Medium")
                    );
                    recommendationRepository.save(recommendation);
                }
            }

            if (aiJson.has("index_suggestions")) {
                for (JsonNode idxNode : aiJson.get("index_suggestions")) {
                    IndexSuggestion indexSuggestion = new IndexSuggestion(
                            tenantId,
                            fingerprint,
                            idxNode.path("table").asText("unknown"),
                            idxNode.path("columns").asText("unknown"),
                            idxNode.path("sql").asText(""),
                            idxNode.path("estimated_improvement").asDouble(0.0)
                    );
                    indexSuggestionRepository.save(indexSuggestion);
                }
            }
            
            if (aiJson.has("rewritten_query")) {
                QueryRecommendation rewriteRec = new QueryRecommendation(
                        tenantId,
                        fingerprint,
                        "REWRITE",
                        "Rewritten query suggestion: " + aiJson.get("rewritten_query").asText(),
                        0.9,
                        "High"
                );
                recommendationRepository.save(rewriteRec);
            }
        } catch (Exception e) {
            System.err.println("AI analysis failed: " + e.getMessage());
        }
    }
}
