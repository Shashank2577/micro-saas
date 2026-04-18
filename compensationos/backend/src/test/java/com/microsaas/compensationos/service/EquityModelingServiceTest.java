package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.EquityGrantCalculationRequest;
import com.microsaas.compensationos.dto.EquityGrantCalculationResponse;
import com.microsaas.compensationos.entity.EquityModel;
import com.microsaas.compensationos.repository.EquityModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquityModelingServiceTest {

    @Mock
    private EquityModelRepository equityModelRepository;

    @InjectMocks
    private EquityModelingService equityModelingService;

    @Test
    void calculateGrant_ReturnsSchedule() {
        UUID tenantId = UUID.randomUUID();
        try (MockedStatic<TenantContext> mockedContext = mockStatic(TenantContext.class)) {
            mockedContext.when(TenantContext::require).thenReturn(tenantId);

            UUID planId = UUID.randomUUID();
            EquityModel model = new EquityModel();
            model.setId(planId);
            model.setTenantId(tenantId);
            model.setVestingScheduleMonths(48);
            model.setCliffMonths(12);
            model.setCurrentValuation(new BigDecimal("10.00"));

            when(equityModelRepository.findById(planId)).thenReturn(Optional.of(model));

            EquityGrantCalculationRequest req = new EquityGrantCalculationRequest();
            req.setPlanId(planId);
            req.setShares(4800L);
            req.setVestingStartDate("2024-01-01");

            EquityGrantCalculationResponse response = equityModelingService.calculateGrant(req);

            assertNotNull(response);
            assertEquals(4800L, response.getTotalShares());
            assertEquals(new BigDecimal("48000.00"), response.getTotalValue());
            assertEquals(37, response.getVestingSchedule().size()); // 1 cliff event + 36 monthly events

            // Cliff
            assertEquals(1200L, response.getVestingSchedule().get(0).getSharesVesting());
            assertEquals("2025-01-01", response.getVestingSchedule().get(0).getDate());

            // First month after cliff
            assertEquals(100L, response.getVestingSchedule().get(1).getSharesVesting());
            assertEquals("2025-02-01", response.getVestingSchedule().get(1).getDate());
        }
    }
}
