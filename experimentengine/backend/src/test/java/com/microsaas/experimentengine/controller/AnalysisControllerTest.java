package com.microsaas.experimentengine.controller;

import com.microsaas.experimentengine.service.AnalysisService;
import com.microsaas.experimentengine.service.StatsEngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AnalysisController.class)
public class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisService analysisService;

    @MockBean
    private StatsEngineService statsEngineService;

    @Test
    @WithMockUser
    public void testGetResults() throws Exception {
        UUID experimentId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        mockMvc.perform(get("/api/experiments/{experimentId}/analysis/results", experimentId)
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pvalue").exists());
    }

    @Test
    @WithMockUser
    public void testCheckSignificance() throws Exception {
        UUID experimentId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        mockMvc.perform(get("/api/experiments/{experimentId}/analysis/significance", experimentId)
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.significant").exists());
    }

    @Test
    @WithMockUser
    public void testTriggerAnalysis() throws Exception {
        UUID experimentId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(analysisService.getResults(experimentId, tenantId)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/experiments/{experimentId}/analysis/run", experimentId)
                .header("X-Tenant-ID", tenantId.toString())
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetVariantMetrics() throws Exception {
        UUID experimentId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        mockMvc.perform(get("/api/experiments/{experimentId}/analysis/variants", experimentId)
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
