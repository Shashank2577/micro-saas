package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealHealthScoringServiceTest {

    @Mock
    private DealActivityRepository activityRepository;

    @Mock
    private DealRiskSignalRepository riskSignalRepository;

    @InjectMocks
    private DealHealthScoringService service;

    @Test
    void calculateHealthScore_NoActivitiesNoRisks_ShouldReturn50() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        when(activityRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());
        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());

        int score = service.calculateHealthScore(tenantId, dealId);
        assertEquals(50, score);
    }

    @Test
    void calculateHealthScore_WithActivities_ShouldIncreaseScore() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        when(activityRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of(
                new DealActivity(), new DealActivity()
        ));
        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());

        int score = service.calculateHealthScore(tenantId, dealId);
        assertEquals(60, score); // 50 + 2*5
    }

    @Test
    void calculateHealthScore_WithHighRisk_ShouldDecreaseScore() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        DealRiskSignal risk = new DealRiskSignal();
        risk.setSeverity("HIGH");

        when(activityRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());
        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of(risk));

        int score = service.calculateHealthScore(tenantId, dealId);
        assertEquals(30, score); // 50 - 20
    }
}
