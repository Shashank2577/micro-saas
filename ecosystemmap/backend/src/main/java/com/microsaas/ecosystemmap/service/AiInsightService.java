package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.ecosystemmap.entity.AiInsight;
import com.microsaas.ecosystemmap.entity.Ecosystem;
import com.microsaas.ecosystemmap.repository.AiInsightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiInsightService {

    private final AiInsightRepository aiInsightRepository;
    private final EcosystemService ecosystemService;
    private final LiteLlmClient liteLlmClient;
    private final WebhookService webhookService;

    public List<AiInsight> getInsightsByEcosystem(UUID ecosystemId) {
        return aiInsightRepository.findByTenantIdAndEcosystemId(TenantContext.require().toString(), ecosystemId);
    }

    public AiInsight getInsightById(UUID id) {
        return aiInsightRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("AI Insight not found"));
    }

    @Transactional
    public AiInsight generateInsight(UUID ecosystemId, String insightType) {
        Ecosystem ecosystem = ecosystemService.getEcosystemById(ecosystemId);

        String prompt = String.format("Analyze the ecosystem '%s' (ID: %s) and provide a '%s' insight.",
                ecosystem.getName(), ecosystem.getId(), insightType);

        String aiResponse = "Generated insight content for " + insightType;
        try {
            Map<String, Object> request = Map.of(
                    "model", "gpt-4o",
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are an AI ecosystem expert."),
                            Map.of("role", "user", "content", prompt)
                    )
            );
            Map<String, Object> response = liteLlmClient.chatCompletions(request);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.get("content") != null) {
                    aiResponse = (String) message.get("content");
                }
            }
        } catch (Exception e) {
            log.error("Failed to generate AI insight using LiteLLM. Using fallback content.", e);
            aiResponse = "Fallback insight: Analysis failed due to AI service error. " + e.getMessage();
        }

        AiInsight insight = AiInsight.builder()
                .tenantId(TenantContext.require().toString())
                .ecosystem(ecosystem)
                .insightType(insightType)
                .content(aiResponse)
                .status("GENERATED")
                .build();

        AiInsight saved = aiInsightRepository.save(insight);
        webhookService.dispatch(TenantContext.require(), "insight.generated", saved.getId().toString());
        return saved;
    }

    @Transactional
    public AiInsight updateInsightStatus(UUID id, String status) {
        AiInsight existing = getInsightById(id);
        existing.setStatus(status);
        AiInsight updated = aiInsightRepository.save(existing);
        webhookService.dispatch(TenantContext.require(), "insight.updated", updated.getId().toString());
        return updated;
    }

    @Transactional
    public void deleteInsight(UUID id) {
        AiInsight existing = getInsightById(id);
        aiInsightRepository.delete(existing);
        webhookService.dispatch(TenantContext.require(), "insight.deleted", existing.getId().toString());
    }
}
