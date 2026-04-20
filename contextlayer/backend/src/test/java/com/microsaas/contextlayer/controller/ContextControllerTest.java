package com.microsaas.contextlayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.dto.ContextUpdateDTO;
import com.microsaas.contextlayer.service.ContextRetrievalService;
import com.microsaas.contextlayer.service.ContextUpdateService;
import com.microsaas.contextlayer.service.PreferenceLearningService;
import com.microsaas.contextlayer.service.PrivacyEnforcementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContextController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ContextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContextRetrievalService retrievalService;

    @MockBean
    private ContextUpdateService updateService;

    @MockBean
    private PreferenceLearningService preferenceService;

    @MockBean
    private PrivacyEnforcementService privacyService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void getContext_returnsContext() throws Exception {
        CustomerContext ctx = new CustomerContext();
        ctx.setCustomerId("c1");
        ctx.setProfile("{\"name\":\"John\"}");
        when(retrievalService.getContext("c1")).thenReturn(Optional.of(ctx));

        mockMvc.perform(get("/api/customers/c1/context")
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("c1"));
    }

    @Test
    void updateContext_updatesAndReturns() throws Exception {
        CustomerContext ctx = new CustomerContext();
        ctx.setCustomerId("c1");
        ctx.setProfile("{\"name\":\"John Updated\"}");

        when(updateService.updateContext(eq("c1"), any(), any())).thenReturn(ctx);

        ContextUpdateDTO dto = new ContextUpdateDTO();
        dto.setProfile("{\"name\":\"John Updated\"}");

        mockMvc.perform(put("/api/customers/c1/context")
                .header("X-Tenant-ID", tenantId.toString())
                .header("X-App-Id", "test-app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").value("{\"name\":\"John Updated\"}"));
    }
}
