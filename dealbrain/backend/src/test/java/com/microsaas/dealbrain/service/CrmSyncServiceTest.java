package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.repository.DealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrmSyncServiceTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private CrmSyncService service;

    @Test
    void syncDealFromCrm_ValidDeal_ShouldUpdateStageAndAmount() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        Deal deal = new Deal();
        deal.setId(dealId);
        deal.setTenantId(tenantId);
        deal.setStage("PROSPECTING");

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));

        service.syncDealFromCrm(tenantId, dealId, "NEGOTIATION", 50000.0);

        verify(dealRepository).save(deal);
        assertEquals("NEGOTIATION", deal.getStage());
        assertEquals(50000.0, deal.getAmount());
    }
}
