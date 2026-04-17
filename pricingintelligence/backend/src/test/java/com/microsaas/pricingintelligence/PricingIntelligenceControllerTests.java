package com.microsaas.pricingintelligence;

import com.microsaas.pricingintelligence.api.PricingIntelligenceController;
import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.domain.PricingExperiment;
import com.microsaas.pricingintelligence.service.PricingIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PricingIntelligenceControllerTests {

    private MockMvc mockMvc;

    @Mock
    private PricingIntelligenceService pricingIntelligenceService;

    @InjectMocks
    private PricingIntelligenceController pricingIntelligenceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pricingIntelligenceController).build();
    }

    @Test
    void testGetModels() throws Exception {
        UUID tenantId = UUID.randomUUID();
        ElasticityModel model = new ElasticityModel();
        model.setId(UUID.randomUUID());
        model.setSegment("SMB");
        model.setBasePrice(new BigDecimal("49.00"));

        when(pricingIntelligenceService.getElasticityModels(any(UUID.class))).thenReturn(Arrays.asList(model));

        mockMvc.perform(get("/api/v1/pricingintelligence/models")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].segment").value("SMB"));
    }

    @Test
    void testGenerateModel() throws Exception {
        UUID tenantId = UUID.randomUUID();
        String segment = "Enterprise";

        ElasticityModel model = new ElasticityModel();
        model.setId(UUID.randomUUID());
        model.setSegment(segment);
        model.setBasePrice(new BigDecimal("499.00"));

        when(pricingIntelligenceService.generateElasticityModel(any(UUID.class), eq(segment))).thenReturn(model);

        mockMvc.perform(post("/api/v1/pricingintelligence/models/generate")
                .header("X-Tenant-ID", tenantId.toString())
                .param("segment", segment)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.segment").value("Enterprise"));
    }
}
