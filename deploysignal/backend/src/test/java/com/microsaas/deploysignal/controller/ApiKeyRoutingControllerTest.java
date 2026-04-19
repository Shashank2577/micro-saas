package com.microsaas.deploysignal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiKeyRoutingController.class)
public class ApiKeyRoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMissingApiKey() throws Exception {
        mockMvc.perform(get("/api/routing/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Missing API Key"));
    }

    @Test
    public void testWave2Routing() throws Exception {
        mockMvc.perform(get("/api/routing/test")
                        .header("X-API-Key", "wave2-abc123xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.route").value("wave2-cluster"));
    }

    @Test
    public void testWave1Routing() throws Exception {
        mockMvc.perform(get("/api/routing/test")
                        .header("X-API-Key", "wave1-xyz789abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.route").value("wave1-cluster"));
    }

    @Test
    public void testInvalidApiKeyRouting() throws Exception {
        mockMvc.perform(get("/api/routing/test")
                        .header("X-API-Key", "invalid-key"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid API Key prefix for routing"));
    }
}
