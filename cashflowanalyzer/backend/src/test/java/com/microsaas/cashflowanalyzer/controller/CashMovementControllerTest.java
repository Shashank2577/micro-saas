package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.CashMovement;
import com.microsaas.cashflowanalyzer.service.AnalysisService;
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

@WebMvcTest(CashMovementController.class)
class CashMovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisService service;

    @Test
    void list_ShouldReturnOk() throws Exception {
        when(service.list()).thenReturn(List.of(new CashMovement()));
        mockMvc.perform(get("/api/v1/cashflow/cash-movements"))
               .andExpect(status().isOk());
    }

    @Test
    void validate_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.validate(id)).thenReturn(true);
        mockMvc.perform(post("/api/v1/cashflow/cash-movements/" + id + "/validate"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.valid").value(true));
    }
}
