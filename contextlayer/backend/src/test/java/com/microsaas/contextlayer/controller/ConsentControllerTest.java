package com.microsaas.contextlayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.ConsentRecord;
import com.microsaas.contextlayer.dto.ConsentRecordDTO;
import com.microsaas.contextlayer.service.PrivacyEnforcementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsentController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ConsentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void getConsent_returnsConsent() throws Exception {
        ConsentRecord record = new ConsentRecord();
        record.setCustomerId("c1");
        record.setConsentType("data_processing");
        record.setGranted(true);
        when(privacyService.getConsent("c1", "data_processing")).thenReturn(Optional.of(record));

        mockMvc.perform(get("/api/consent/c1")
                .header("X-Tenant-ID", tenantId.toString())
                .param("type", "data_processing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("c1"))
                .andExpect(jsonPath("$.granted").value(true));
    }

    @Test
    void recordConsent_recordsAndReturns() throws Exception {
        ConsentRecord record = new ConsentRecord();
        record.setCustomerId("c1");
        record.setConsentType("data_processing");
        record.setGranted(true);

        when(privacyService.recordConsent(eq("c1"), eq("data_processing"), eq(true), eq("test-app"))).thenReturn(record);

        ConsentRecordDTO dto = new ConsentRecordDTO();
        dto.setCustomerId("c1");
        dto.setConsentType("data_processing");
        dto.setGranted(true);

        mockMvc.perform(post("/api/consent")
                .header("X-Tenant-ID", tenantId.toString())
                .header("X-App-Id", "test-app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("c1"))
                .andExpect(jsonPath("$.granted").value(true));
    }

    @Test
    void revokeConsent_revokes() throws Exception {
        mockMvc.perform(delete("/api/consent/c1")
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk());

        verify(privacyService).revokeConsent("c1");
    }

    @Test
    void getConsentHistory_returnsHistory() throws Exception {
        ConsentRecord record1 = new ConsentRecord();
        record1.setCustomerId("c1");
        record1.setConsentType("data_processing");

        when(privacyService.getConsentHistory("c1")).thenReturn(List.of(record1));

        mockMvc.perform(get("/api/consent/c1/history")
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("c1"));
    }
}
