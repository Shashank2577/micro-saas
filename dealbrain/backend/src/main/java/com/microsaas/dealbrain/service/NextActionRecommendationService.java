package com.microsaas.dealbrain.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.model.DealRecommendation;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealRecommendationRepository;
import com.microsaas.dealbrain.repository.DealRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NextActionRecommendationService {

    private final DealRepository dealRepository;
    private final DealRiskSignalRepository riskSignalRepository;
    private final DealRecommendationRepository recommendationRepository;
    private final AiService aiService;
    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public List<DealRecommendation> generateRecommendations(UUID tenantId, UUID dealId) {
        Deal deal = dealRepository.findById(dealId)
                .filter(d -> d.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Deal not found"));

        List<DealRiskSignal> risks = riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId);

        String riskDescriptions = risks.stream()
                .map(r -> r.getSignalType() + " (" + r.getSeverity() + "): " + r.getDescription())
                .collect(Collectors.joining(", "));

        String prompt = String.format("Generate a short, actionable next step recommendation for a deal named '%s' at stage '%s'. It has the following risks: [%s]. Respond with ONLY the action name, a pipe character '|', and a short reason.", deal.getName(), deal.getStage(), riskDescriptions);

        ChatRequest request = new ChatRequest(
                "gpt-4o",
                List.of(new ChatMessage("user", prompt)),
                0.7,
                100
        );

        ChatResponse response = aiService.chat(request);
        String aiResponse = response.content();

        if (aiResponse != null && aiResponse.contains("|")) {
            String[] parts = aiResponse.split("\\|", 2);
            String action = parts[0].trim();
            String reason = parts[1].trim();
            createRecommendation(tenantId, dealId, action, reason);
        } else if (risks.isEmpty()) {
            createRecommendation(tenantId, dealId, "Send check-in email", "Maintain regular cadence.");
        }

        return recommendationRepository.findByTenantIdAndDealId(tenantId, dealId);
    }

    private void createRecommendation(UUID tenantId, UUID dealId, String action, String reason) {
        List<DealRecommendation> existing = recommendationRepository.findByTenantIdAndDealId(tenantId, dealId)
            .stream().filter(r -> action.equals(r.getAction())).collect(Collectors.toList());

        if (existing.isEmpty()) {
            DealRecommendation rec = new DealRecommendation();
            rec.setTenantId(tenantId);
            rec.setDealId(dealId);
            rec.setAction(action);
            rec.setReason(reason);
            rec.setStatus("PENDING");
            recommendationRepository.save(rec);

            try {
                Map<String, String> payloadMap = new HashMap<>();
                payloadMap.put("dealId", dealId.toString());
                payloadMap.put("action", action);
                String payload = objectMapper.writeValueAsString(payloadMap);
                webhookService.dispatch(tenantId, "dealbrain.recommendation.created", payload);
            } catch (JsonProcessingException e) {
                // Log and ignore for now, the webhook is non-critical for the main flow
            }
        }
    }
}
