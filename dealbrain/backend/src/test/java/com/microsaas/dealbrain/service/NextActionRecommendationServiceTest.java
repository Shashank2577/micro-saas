package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealRecommendation;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealRecommendationRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NextActionRecommendationServiceTest {

    @Mock
    private DealRiskSignalRepository riskSignalRepository;

    @Mock
    private DealRecommendationRepository recommendationRepository;

    @InjectMocks
    private NextActionRecommendationService service;

    @Test
    void generateRecommendations_WithStaleActivity_ShouldCreateFollowUpRecommendation() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        DealRiskSignal risk = new DealRiskSignal();
        risk.setSignalType("STALE_ACTIVITY");

        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of(risk));

        // Return empty list so it creates the specific recommendation,
        // but when it checks for default it also returns empty list so it creates the default one too.
        // So we expect 2 saves!
        when(recommendationRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());

        service.generateRecommendations(tenantId, dealId);

        verify(recommendationRepository, times(2)).save(any(DealRecommendation.class));
    }
}
