package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.model.HistoricalDeal;
import com.microsaas.dealbrain.repository.DealRepository;
import com.microsaas.dealbrain.repository.HistoricalDealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloseProbabilityServiceTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private HistoricalDealRepository historicalDealRepository;

    @Mock
    private DealHealthScoringService healthScoringService;

    @InjectMocks
    private CloseProbabilityService service;

    @Test
    void estimateCloseProbability_NoHistory_ShouldUseDefaultBaseRate() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();
        Deal deal = new Deal();
        deal.setId(dealId);

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(healthScoringService.calculateHealthScore(tenantId, dealId)).thenReturn(50);
        when(historicalDealRepository.findByTenantIdAndOutcome(tenantId, "WON")).thenReturn(List.of());
        when(historicalDealRepository.findByTenantId(tenantId)).thenReturn(List.of());

        double prob = service.estimateCloseProbability(tenantId, dealId);

        // baseRate = 0.3, healthFactor = (50+50)/100 = 1.0.  0.3 * 1.0 = 0.3
        assertEquals(0.3, prob, 0.01);
    }

    @Test
    void estimateCloseProbability_WithHistoryAndHighHealth_ShouldAdjustProbability() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();
        Deal deal = new Deal();
        deal.setId(dealId);

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(healthScoringService.calculateHealthScore(tenantId, dealId)).thenReturn(100);

        // 50% historical win rate
        when(historicalDealRepository.findByTenantIdAndOutcome(tenantId, "WON")).thenReturn(List.of(new HistoricalDeal()));
        when(historicalDealRepository.findByTenantId(tenantId)).thenReturn(List.of(new HistoricalDeal(), new HistoricalDeal()));

        double prob = service.estimateCloseProbability(tenantId, dealId);

        // baseRate = 0.5, healthFactor = (100+50)/100 = 1.5.  0.5 * 1.5 = 0.75
        assertEquals(0.75, prob, 0.01);
    }
}
