package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.ForecastRun;
import com.microsaas.cashflowanalyzer.service.ForecastingService;
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

@WebMvcTest(ForecastRunController.class)
class ForecastRunControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForecastingService service;

    @Test
    void list_ShouldReturnOk() throws Exception {
        when(service.list()).thenReturn(List.of(new ForecastRun()));
        mockMvc.perform(get("/api/v1/cashflow/forecast-runs"))
               .andExpect(status().isOk());
    }
}
