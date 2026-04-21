package com.microsaas.telemetrycore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.telemetrycore.dto.FunnelDTO;
import com.microsaas.telemetrycore.model.Funnel;
import com.microsaas.telemetrycore.service.FunnelService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FunnelController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FunnelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FunnelService funnelService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID tenantId;
    private UUID funnelId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        funnelId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void createFunnel() throws Exception {
        FunnelDTO inputDto = new FunnelDTO();
        inputDto.setName("Test Funnel");
        inputDto.setSteps(Arrays.asList("Step1", "Step2"));

        Funnel createdFunnel = new Funnel();
        createdFunnel.setId(funnelId);
        createdFunnel.setName("Test Funnel");
        createdFunnel.setSteps(Arrays.asList("Step1", "Step2"));

        when(funnelService.createFunnel(any(Funnel.class))).thenReturn(createdFunnel);

        mockMvc.perform(post("/api/funnels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funnelId.toString()))
                .andExpect(jsonPath("$.name").value("Test Funnel"))
                .andExpect(jsonPath("$.steps[0]").value("Step1"));
    }

    @Test
    void getFunnel() throws Exception {
        Funnel funnel = new Funnel();
        funnel.setId(funnelId);
        funnel.setName("Test Funnel");

        when(funnelService.getFunnel(funnelId)).thenReturn(funnel);

        mockMvc.perform(get("/api/funnels/{id}", funnelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funnelId.toString()))
                .andExpect(jsonPath("$.name").value("Test Funnel"));
    }

    @Test
    void analyzeFunnel() throws Exception {
        Map<String, Object> analysis = Map.of("Step1", 100, "Step2", 50);

        when(funnelService.analyzeFunnel(funnelId)).thenReturn(analysis);

        mockMvc.perform(get("/api/funnels/{id}/analysis", funnelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funnelId").value(funnelId.toString()))
                .andExpect(jsonPath("$.stepAnalysis.Step1").value(100))
                .andExpect(jsonPath("$.stepAnalysis.Step2").value(50));
    }

    @Test
    void getAllFunnels() throws Exception {
        Funnel funnel = new Funnel();
        funnel.setId(funnelId);
        funnel.setName("Test Funnel");

        when(funnelService.getAllFunnels()).thenReturn(Arrays.asList(funnel));

        mockMvc.perform(get("/api/funnels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(funnelId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Funnel"));
    }

    @Test
    void deleteFunnel() throws Exception {
        doNothing().when(funnelService).deleteFunnel(funnelId);

        mockMvc.perform(delete("/api/funnels/{id}", funnelId))
                .andExpect(status().isNoContent());

        verify(funnelService, times(1)).deleteFunnel(funnelId);
    }
}
