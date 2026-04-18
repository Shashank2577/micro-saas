package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Impact;
import com.microsaas.nonprofitos.dto.ImpactDto;
import com.microsaas.nonprofitos.repository.ImpactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImpactServiceTest {

    @Mock
    private ImpactRepository impactRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private ImpactService impactService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateImpact() {
        ImpactDto dto = new ImpactDto();
        dto.setMetricName("Meals Served");
        dto.setMetricValue(new BigDecimal("1000"));

        Impact savedImpact = new Impact();
        savedImpact.setMetricName("Meals Served");
        savedImpact.setMetricValue(new BigDecimal("1000"));

        when(impactRepository.save(any(Impact.class))).thenReturn(savedImpact);

        Impact result = impactService.createImpact(dto);

        assertEquals("Meals Served", result.getMetricName());
    }

    @Test
    void testGenerateNarrative() {
        Impact impact = new Impact();
        impact.setMetricName("Meals Served");
        impact.setMetricValue(new BigDecimal("1000"));

        when(impactRepository.findByTenantId(tenantId)).thenReturn(List.of(impact));
        when(aiService.generateImpactNarrative(anyString())).thenReturn("A great narrative");
        when(impactRepository.save(any(Impact.class))).thenReturn(impact);

        String result = impactService.generateNarrative();

        assertEquals("A great narrative", result);
        assertEquals("A great narrative", impact.getNarrative());
    }
}
