package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.model.ActionPlan;
import com.microsaas.engagementpulse.model.Alert;
import com.microsaas.engagementpulse.repository.ActionPlanRepository;
import com.microsaas.engagementpulse.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private ActionPlanRepository actionPlanRepository;

    @InjectMocks
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void addActionPlan() {
        UUID alertId = UUID.randomUUID();
        Alert alert = new Alert();
        alert.setId(alertId);

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));

        ActionPlan plan = new ActionPlan();
        plan.setDescription("Test plan");
        
        when(actionPlanRepository.save(any(ActionPlan.class))).thenReturn(plan);

        ActionPlan result = alertService.addActionPlan(alertId, "Test plan");
        assertEquals("Test plan", result.getDescription());
        verify(actionPlanRepository).save(any(ActionPlan.class));
    }
}
