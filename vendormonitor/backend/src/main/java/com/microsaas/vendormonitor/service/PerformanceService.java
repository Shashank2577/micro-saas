package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Alert;
import com.microsaas.vendormonitor.domain.PerformanceRecord;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.AlertRepository;
import com.microsaas.vendormonitor.repository.PerformanceRecordRepository;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PerformanceService {

    private final PerformanceRecordRepository performanceRecordRepository;
    private final VendorRepository vendorRepository;
    private final AlertRepository alertRepository;

    @Autowired
    public PerformanceService(PerformanceRecordRepository performanceRecordRepository,
                              VendorRepository vendorRepository,
                              AlertRepository alertRepository) {
        this.performanceRecordRepository = performanceRecordRepository;
        this.vendorRepository = vendorRepository;
        this.alertRepository = alertRepository;
    }

    public List<PerformanceRecord> getPerformanceRecordsForVendor(UUID vendorId) {
        return performanceRecordRepository.findByTenantIdAndVendorId(TenantContext.require(), vendorId);
    }

    public PerformanceRecord recordPerformance(UUID vendorId, PerformanceRecord record) {
        Vendor vendor = vendorRepository.findByIdAndTenantId(vendorId, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        record.setTenantId(TenantContext.require());
        record.setVendor(vendor);
        if (record.getRecordedAt() == null) {
            record.setRecordedAt(ZonedDateTime.now());
        }

        PerformanceRecord savedRecord = performanceRecordRepository.save(record);

        if (Boolean.TRUE.equals(record.getIsSlaBreach())) {
            createAlertForBreach(vendor, savedRecord);
        }

        return savedRecord;
    }

    private void createAlertForBreach(Vendor vendor, PerformanceRecord record) {
        Alert alert = new Alert();
        alert.setTenantId(TenantContext.require());
        alert.setVendor(vendor);
        alert.setTitle("SLA Breach Detected: " + record.getRecordType());
        alert.setDescription("An SLA breach was recorded on " + record.getRecordedAt() + ". Description: " + record.getDescription());
        alert.setSeverity("HIGH");
        alert.setStatus("OPEN");
        alert.setGeneratedAt(ZonedDateTime.now());
        alertRepository.save(alert);
    }

    public Map<String, Object> getMetricsSummary(UUID vendorId) {
        List<PerformanceRecord> records = performanceRecordRepository.findByTenantIdAndVendorId(TenantContext.require(), vendorId);
        
        long breachCount = records.stream().filter(r -> Boolean.TRUE.equals(r.getIsSlaBreach())).count();
        
        double uptimeSum = 0;
        int uptimeCount = 0;
        double responseTimeSum = 0;
        int responseTimeCount = 0;

        for (PerformanceRecord record : records) {
            if ("UPTIME".equalsIgnoreCase(record.getRecordType()) && record.getMetricValue() != null) {
                uptimeSum += record.getMetricValue().doubleValue();
                uptimeCount++;
            } else if ("TICKET".equalsIgnoreCase(record.getRecordType()) && record.getMetricValue() != null) {
                responseTimeSum += record.getMetricValue().doubleValue();
                responseTimeCount++;
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("breachCount", breachCount);
        summary.put("averageUptime", uptimeCount > 0 ? BigDecimal.valueOf(uptimeSum / uptimeCount).setScale(2, RoundingMode.HALF_UP) : null);
        summary.put("averageResponseTime", responseTimeCount > 0 ? BigDecimal.valueOf(responseTimeSum / responseTimeCount).setScale(2, RoundingMode.HALF_UP) : null);
        
        return summary;
    }
}
