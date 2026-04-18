package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.RenewalSummary;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.ContractRepository;
import com.microsaas.vendormonitor.repository.PerformanceRecordRepository;
import com.microsaas.vendormonitor.repository.RenewalSummaryRepository;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AISummaryServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PerformanceRecordRepository performanceRecordRepository;

    @Mock
    private RenewalSummaryRepository renewalSummaryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AISummaryService aiSummaryService;

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
    void generateSummary() {
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setName("Test Vendor");
        vendor.setSlaDescription("desc");

        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(vendor));
        when(contractRepository.findByTenantIdAndVendorId(tenantId, vendorId)).thenReturn(Collections.emptyList());
        when(performanceRecordRepository.findByTenantIdAndVendorId(tenantId, vendorId)).thenReturn(Collections.emptyList());

        Map<String, Object> aiResponse = Map.of("response", "Score: 85\nRecommendation: RENEW\nSummary: Good performance.");
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(aiResponse, HttpStatus.OK));

        when(renewalSummaryRepository.save(any(RenewalSummary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RenewalSummary summary = aiSummaryService.generateSummary(vendorId);

        assertNotNull(summary);
        assertEquals(75, summary.getOverallScore());
        assertEquals("RENEGOTIATE", summary.getRecommendation());
        assertNotNull(summary.getAiSummary());
        
        verify(renewalSummaryRepository, times(1)).save(any(RenewalSummary.class));
    }
}
