package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import com.microsaas.dealbrain.repository.StakeholderRepository;
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
class RiskSignalDetectionServiceTest {

    @Mock
    private DealActivityRepository activityRepository;

    @Mock
    private StakeholderRepository stakeholderRepository;

    @Mock
    private DealRiskSignalRepository riskSignalRepository;

    @InjectMocks
    private RiskSignalDetectionService service;

    @Test
    void detectRisks_NoActivity_ShouldCreateStaleActivitySignal() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        when(activityRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());
        when(stakeholderRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());
        when(riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)).thenReturn(List.of());

        service.detectRisks(tenantId, dealId);

        verify(riskSignalRepository, times(2)).save(any(DealRiskSignal.class)); // Stale Activity + No Decision Maker
    }
}
