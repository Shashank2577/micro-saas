package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.NarrativeInsight;
import com.microsaas.cashflowanalyzer.service.ReportingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NarrativeInsightController.class)
class NarrativeInsightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingService service;

    @Test
    void list_ShouldReturnOk() throws Exception {
        when(service.list()).thenReturn(List.of(new NarrativeInsight()));
        mockMvc.perform(get("/api/v1/cashflow/narrative-insights"))
               .andExpect(status().isOk());
    }
}
