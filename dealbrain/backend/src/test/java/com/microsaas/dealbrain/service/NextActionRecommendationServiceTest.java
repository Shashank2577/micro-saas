package com.microsaas.dealbrain.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.model.DealRecommendation;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealRecommendationRepository;
import com.microsaas.dealbrain.repository.DealRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NextActionRecommendationServiceTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealRiskSignalRepository riskSignalRepository;

    @Mock
    private DealRecommendationRepository recommendationRepository;

    @Mock
    private AiService aiService;

    @Mock
    private WebhookService webhookService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NextActionRecommendationService nextActionRecommendationService;

    @Test
    void testGenerateRecommendations_withRisks() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        Deal deal = new Deal();
        deal.setId(dealId);
        deal.setTenantId(tenantId);
        deal.setName("Acme Corp");
        deal.setStage("Discovery");

        DealRiskSignal risk = new DealRiskSignal();
        risk.setSignalType("STALE_ACTIVITY");
        risk.setSeverity("HIGH");
        risk.setDescription("No activity in 10 days.");

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of(risk));
        when(recommendationRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());

        ChatResponse mockResponse = new ChatResponse("id", "model", "Schedule follow-up call | Deal has been inactive.", null);
        when(aiService.chat(any())).thenReturn(mockResponse);

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"dealId\":\"" + dealId + "\",\"action\":\"Schedule follow-up call\"}");

        nextActionRecommendationService.generateRecommendations(tenantId, dealId);

        verify(recommendationRepository).save(argThat(rec ->
            "Schedule follow-up call".equals(rec.getAction()) &&
            "Deal has been inactive.".equals(rec.getReason())
        ));

        verify(webhookService).dispatch(eq(tenantId), eq("dealbrain.recommendation.created"), anyString());
    }
}
