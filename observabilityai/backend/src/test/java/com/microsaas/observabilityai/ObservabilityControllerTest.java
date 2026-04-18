package com.microsaas.observabilityai;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ObservabilityController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
class ObservabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObservabilityService service;

    @MockBean
    private AIAnalysisService aiAnalysisService;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void getSignals() throws Exception {
        Mockito.when(service.getSignals(Mockito.any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/signals")
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void analyzeTrace() throws Exception {
        Mockito.when(service.getSignalsByTraceId("123")).thenReturn(Collections.singletonList(new ObservabilitySignal()));
        Mockito.when(aiAnalysisService.analyzeTrace(any())).thenReturn("{\"rootCause\": \"test\"}");

        mockMvc.perform(post("/api/analyze")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"traceId\": \"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rootCause").value("test"));
    }
}
