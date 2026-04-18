package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Alert;
import com.microsaas.vendormonitor.domain.PerformanceRecord;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.AlertRepository;
import com.microsaas.vendormonitor.repository.PerformanceRecordRepository;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PerformanceServiceTest {

    @Mock
    private PerformanceRecordRepository performanceRecordRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private PerformanceService performanceService;

    private AutoCloseable closeable;
    private final UUID tenantId = UUID.randomUUID();
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() throws Exception {
        TenantContext.clear();
        closeable.close();
    }

    @Test
    void recordPerformance_NoBreach() {
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);

        PerformanceRecord record = new PerformanceRecord();
        record.setIsSlaBreach(false);
        record.setRecordType("UPTIME");

        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(vendor));
        when(performanceRecordRepository.save(any(PerformanceRecord.class))).thenReturn(record);

        PerformanceRecord result = performanceService.recordPerformance(vendorId, record);

        assertNotNull(result);
        verify(performanceRecordRepository, times(1)).save(record);
        verify(alertRepository, never()).save(any(Alert.class));
    }

    @Test
    void recordPerformance_WithBreach_CreatesAlert() {
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);

        PerformanceRecord record = new PerformanceRecord();
        record.setIsSlaBreach(true);
        record.setRecordType("UPTIME");
        record.setDescription("Outage");

        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(vendor));
        when(performanceRecordRepository.save(any(PerformanceRecord.class))).thenReturn(record);

        PerformanceRecord result = performanceService.recordPerformance(vendorId, record);

        assertNotNull(result);
        verify(performanceRecordRepository, times(1)).save(record);
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void getMetricsSummary() {
        PerformanceRecord r1 = new PerformanceRecord();
        r1.setRecordType("UPTIME");
        r1.setMetricValue(new BigDecimal("99.9"));
        r1.setIsSlaBreach(false);

        PerformanceRecord r2 = new PerformanceRecord();
        r2.setRecordType("UPTIME");
        r2.setMetricValue(new BigDecimal("98.5"));
        r2.setIsSlaBreach(true);

        when(performanceRecordRepository.findByTenantIdAndVendorId(tenantId, vendorId))
                .thenReturn(List.of(r1, r2));

        Map<String, Object> summary = performanceService.getMetricsSummary(vendorId);

        assertEquals(1L, summary.get("breachCount"));
        assertEquals(new BigDecimal("99.20"), summary.get("averageUptime"));
    }
}
