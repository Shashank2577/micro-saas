package com.microsaas.investtracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.investtracker.dto.AiInsightDto;
import com.microsaas.investtracker.dto.CreatePortfolioRequest;
import com.microsaas.investtracker.dto.PortfolioDto;
import com.microsaas.investtracker.service.AiOptimizationService;
import com.microsaas.investtracker.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PortfolioController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @MockBean
    private AiOptimizationService aiOptimizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllPortfolios() throws Exception {
        PortfolioDto dto = new PortfolioDto();
        dto.setId(UUID.randomUUID());
        dto.setName("Main");
        dto.setCurrency("USD");

        when(portfolioService.getAllPortfolios()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/portfolios")
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Main"));
    }

    @Test
    void testCreatePortfolio() throws Exception {
        PortfolioDto dto = new PortfolioDto();
        dto.setId(UUID.randomUUID());
        dto.setName("New");
        dto.setCurrency("USD");

        when(portfolioService.createPortfolio(any(CreatePortfolioRequest.class))).thenReturn(dto);

        CreatePortfolioRequest req = new CreatePortfolioRequest();
        req.setName("New");
        req.setCurrency("USD");

        mockMvc.perform(post("/api/v1/portfolios")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }
}
