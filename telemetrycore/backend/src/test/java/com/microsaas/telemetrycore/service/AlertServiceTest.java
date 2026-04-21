package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Alert;
import com.microsaas.telemetrycore.model.Metric;
import com.microsaas.telemetrycore.repository.AlertRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertService alertService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateAlert_success() {
        Alert alert = new Alert();
        alert.setCondition(">");
        alert.setThreshold(80.0);
        alert.setNotificationChannel("email");

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> {
            Alert saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Alert result = alertService.createAlert(alert);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testEvaluateAlert_triggersWhenThresholdBreached() {
        Alert alert = new Alert();
        alert.setCondition(">");
        alert.setThreshold(80.0);
        alert.setNotificationChannel("email");

        Metric metric = new Metric();
        metric.setName("cpu_usage");
        alert.setMetric(metric);

        when(alertRepository.findByTenantId(tenantId)).thenReturn(Arrays.asList(alert));

        alertService.evaluateAlerts(90.0, "cpu_usage", tenantId);

        verify(alertRepository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testEvaluateAlert_noTrigger_belowThreshold() {
        Alert alert = new Alert();
        alert.setCondition(">");
        alert.setThreshold(80.0);
        alert.setNotificationChannel("email");

        Metric metric = new Metric();
        metric.setName("cpu_usage");
        alert.setMetric(metric);

        when(alertRepository.findByTenantId(tenantId)).thenReturn(Arrays.asList(alert));

        alertService.evaluateAlerts(70.0, "cpu_usage", tenantId);

        verify(alertRepository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testListAlerts_byTenant() {
        List<Alert> expectedAlerts = Arrays.asList(new Alert());
        when(alertRepository.findByTenantId(tenantId)).thenReturn(expectedAlerts);

        List<Alert> result = alertService.getAllAlerts();

        assertEquals(expectedAlerts.size(), result.size());
        verify(alertRepository, times(1)).findByTenantId(tenantId);
    }
}
