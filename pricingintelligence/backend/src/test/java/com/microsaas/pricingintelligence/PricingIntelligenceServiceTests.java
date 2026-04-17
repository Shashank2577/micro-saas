package com.microsaas.pricingintelligence;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.domain.PricingExperiment;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.microsaas.pricingintelligence.repository.HistoricalDataRepository;
import com.microsaas.pricingintelligence.repository.PricingExperimentRepository;
import com.microsaas.pricingintelligence.service.PricingIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PricingIntelligenceServiceTests {

    @Mock
    private HistoricalDataRepository historicalDataRepository;

    @Mock
    private ElasticityModelRepository elasticityModelRepository;

    @Mock
    private PricingExperimentRepository pricingExperimentRepository;

    @InjectMocks
    private PricingIntelligenceService pricingIntelligenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateElasticityModel() {
        UUID tenantId = UUID.randomUUID();
        String segment = "Enterprise";

        ElasticityModel mockModel = new ElasticityModel();
        mockModel.setId(UUID.randomUUID());
        mockModel.setTenantId(tenantId);
        mockModel.setSegment(segment);
        mockModel.setBasePrice(new BigDecimal("100.00"));

        when(elasticityModelRepository.save(any(ElasticityModel.class))).thenReturn(mockModel);

        ElasticityModel result = pricingIntelligenceService.generateElasticityModel(tenantId, segment);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(segment, result.getSegment());
        assertEquals(new BigDecimal("100.00"), result.getBasePrice());
    }

    @Test
    void testCreateExperiment() {
        UUID tenantId = UUID.randomUUID();
        String name = "Q3 Pricing Test";
        String segment = "SMB";
        BigDecimal controlPrice = new BigDecimal("49.00");
        BigDecimal variantPrice = new BigDecimal("59.00");

        PricingExperiment mockExperiment = new PricingExperiment();
        mockExperiment.setId(UUID.randomUUID());
        mockExperiment.setTenantId(tenantId);
        mockExperiment.setName(name);
        mockExperiment.setSegment(segment);
        mockExperiment.setControlPrice(controlPrice);
        mockExperiment.setVariantPrice(variantPrice);

        when(pricingExperimentRepository.save(any(PricingExperiment.class))).thenReturn(mockExperiment);

        PricingExperiment result = pricingIntelligenceService.createExperiment(tenantId, name, segment, controlPrice, variantPrice);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(name, result.getName());
        assertEquals(controlPrice, result.getControlPrice());
        assertEquals(variantPrice, result.getVariantPrice());
    }
}
