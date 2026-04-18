package com.microsaas.wealthedge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.wealthedge.domain.Portfolio;
import com.microsaas.wealthedge.service.AIWealthService;
import com.microsaas.wealthedge.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PortfolioController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
    }
)
@ActiveProfiles("test")
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @MockBean
    private AIWealthService aiWealthService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllPortfolios() throws Exception {
        Portfolio p = new Portfolio();
        p.setId(UUID.randomUUID());
        p.setName("My Portfolio");

        when(portfolioService.getAllPortfolios()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/portfolios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("My Portfolio"));
    }

    @Test
    void testGetInsights() throws Exception {
        UUID id = UUID.randomUUID();
        Portfolio p = new Portfolio();
        p.setId(id);
        
        when(portfolioService.getPortfolio(id)).thenReturn(p);
        when(aiWealthService.analyzeRiskConcentration(any(String.class))).thenReturn("Diversify your portfolio.");

        mockMvc.perform(get("/api/portfolios/" + id + "/insights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insights").value("Diversify your portfolio."));
    }
}
