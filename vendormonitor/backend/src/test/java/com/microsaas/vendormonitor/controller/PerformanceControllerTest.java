package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.PerformanceRecord;
import com.microsaas.vendormonitor.service.PerformanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PerformanceControllerTest {

    @Mock
    private PerformanceService performanceService;

    @InjectMocks
    private PerformanceController performanceController;

    private AutoCloseable closeable;
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPerformanceRecords() {
        when(performanceService.getPerformanceRecordsForVendor(vendorId)).thenReturn(List.of(new PerformanceRecord()));
        ResponseEntity<List<PerformanceRecord>> response = performanceController.getPerformanceRecords(vendorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetricsSummary() {
        when(performanceService.getMetricsSummary(vendorId)).thenReturn(Map.of("breachCount", 1L));
        ResponseEntity<Map<String, Object>> response = performanceController.getMetricsSummary(vendorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().get("breachCount"));
    }
}
