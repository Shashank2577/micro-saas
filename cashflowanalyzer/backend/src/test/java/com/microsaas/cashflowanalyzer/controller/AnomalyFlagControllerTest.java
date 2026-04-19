package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.AnomalyFlag;
import com.microsaas.cashflowanalyzer.service.AnomaliesService;
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

@WebMvcTest(AnomalyFlagController.class)
class AnomalyFlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnomaliesService service;

    @Test
    void list_ShouldReturnOk() throws Exception {
        when(service.list()).thenReturn(List.of(new AnomalyFlag()));
        mockMvc.perform(get("/api/v1/cashflow/anomaly-flags"))
               .andExpect(status().isOk());
    }
}
