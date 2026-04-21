package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.domain.PriceRecommendation;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.microsaas.pricingintelligence.repository.PriceRecommendationRepository;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceOptimizationServiceTest {

    @Mock
    private PriceRecommendationRepository priceRecommendationRepository;

    @Mock
    private ElasticityModelRepository elasticityModelRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private PriceOptimizationService priceOptimizationService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID segmentId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        TenantContext.set(tenantId);
    }

    @Test
    public void generateRecommendation_createsRecommendationWithAiRationale() {
        ElasticityModel model = new ElasticityModel();
        model.setElasticityCoefficient(-1.5);
        model.setRSquared(0.85);
        when(elasticityModelRepository.findByTenantIdAndSegmentId(tenantId, segmentId))
                .thenReturn(Optional.of(model));

        ChatResponse.Usage mockUsage = new ChatResponse.Usage(10, 10, 20);
        ChatResponse mockResponse = new ChatResponse("test-id", "test-model", "Test Rationale", mockUsage);
        when(aiService.chat(any(ChatRequest.class))).thenReturn(mockResponse);

        PriceRecommendation mockSaved = new PriceRecommendation();
        mockSaved.setRationale("Test Rationale");
        when(priceRecommendationRepository.save(any(PriceRecommendation.class))).thenReturn(mockSaved);

        PriceRecommendation result = priceOptimizationService.generateRecommendation(segmentId);

        assertNotNull(result);
        assertEquals("Test Rationale", result.getRationale());
        verify(priceRecommendationRepository).save(any(PriceRecommendation.class));
    }
}
