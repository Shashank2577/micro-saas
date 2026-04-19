package com.microsaas.deploysignal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoraMetricsController.class)
public class DoraMetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetDoraMetrics() throws Exception {
        mockMvc.perform(get("/api/metrics/dora"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deploymentFrequency").value("Daily"))
                .andExpect(jsonPath("$.changeFailureRate").value("5%"));
    }
}
