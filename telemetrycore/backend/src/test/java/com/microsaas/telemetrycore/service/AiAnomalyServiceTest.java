package com.microsaas.telemetrycore.service;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiAnomalyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AiAnomalyService aiAnomalyService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        ReflectionTestUtils.setField(aiAnomalyService, "litellmUrl", "http://litellm:4000");
        ReflectionTestUtils.setField(aiAnomalyService, "litellmKey", "dummy-key");
    }

    @Test
    void testDetectAnomaly_flagsOutlier() {
        List<Map<String, Object>> recentData = Arrays.asList(Map.of("value", 100), Map.of("value", 1000));

        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("content", "Anomaly detected: high spike.");

        Map<String, Object> choice = new HashMap<>();
        choice.put("message", messageContent);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", Arrays.asList(choice));

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String result = aiAnomalyService.analyzeMetricAnomaly("cpu_usage", recentData);

        assertEquals("Anomaly detected: high spike.", result);
    }

    @Test
    void testDetectAnomaly_normalData_noFlag() {
        List<Map<String, Object>> recentData = Arrays.asList(Map.of("value", 100), Map.of("value", 101));

        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("content", "No anomalies.");

        Map<String, Object> choice = new HashMap<>();
        choice.put("message", messageContent);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", Arrays.asList(choice));

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(responseBody));

        String result = aiAnomalyService.analyzeMetricAnomaly("cpu_usage", recentData);

        assertEquals("No anomalies.", result);
    }
}
