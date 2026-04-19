package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.CashflowPeriod;
import com.microsaas.cashflowanalyzer.service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CashflowPeriodController.class)
class CashflowPeriodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngestionService service;

    @Test
    void list_ShouldReturnOk() throws Exception {
        when(service.list()).thenReturn(List.of(new CashflowPeriod()));
        mockMvc.perform(get("/api/v1/cashflow/cashflow-periods"))
               .andExpect(status().isOk());
    }

    @Test
    void getById_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getById(id)).thenReturn(new CashflowPeriod());
        mockMvc.perform(get("/api/v1/cashflow/cashflow-periods/" + id))
               .andExpect(status().isOk());
    }

    @Test
    void validate_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.validate(id)).thenReturn(true);
        mockMvc.perform(post("/api/v1/cashflow/cashflow-periods/" + id + "/validate"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.valid").value(true));
    }
}
