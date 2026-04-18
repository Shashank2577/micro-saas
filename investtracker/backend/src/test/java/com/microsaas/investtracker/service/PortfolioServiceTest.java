package com.microsaas.investtracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.investtracker.dto.CreatePortfolioRequest;
import com.microsaas.investtracker.dto.PortfolioDto;
import com.microsaas.investtracker.entity.Portfolio;
import com.microsaas.investtracker.repository.PortfolioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private UUID tenantId;
    private UUID portfolioId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        portfolioId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetAllPortfolios() {
        Portfolio p = new Portfolio();
        p.setId(portfolioId);
        p.setName("My Portfolio");
        p.setCurrency("USD");
        p.setTenantId(tenantId);

        when(portfolioRepository.findByTenantId(tenantId)).thenReturn(List.of(p));

        List<PortfolioDto> result = portfolioService.getAllPortfolios();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("My Portfolio", result.get(0).getName());
        assertEquals("USD", result.get(0).getCurrency());
        verify(portfolioRepository).findByTenantId(tenantId);
    }

    @Test
    void testCreatePortfolio() {
        CreatePortfolioRequest request = new CreatePortfolioRequest();
        request.setName("New Port");
        request.setCurrency("EUR");

        Portfolio saved = new Portfolio();
        saved.setId(portfolioId);
        saved.setName("New Port");
        saved.setCurrency("EUR");
        saved.setTenantId(tenantId);

        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(saved);

        PortfolioDto result = portfolioService.createPortfolio(request);

        assertNotNull(result);
        assertEquals("New Port", result.getName());
        assertEquals("EUR", result.getCurrency());
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void testGetPortfolio() {
        Portfolio p = new Portfolio();
        p.setId(portfolioId);
        p.setName("Test");
        p.setCurrency("USD");
        p.setTenantId(tenantId);

        when(portfolioRepository.findByIdAndTenantId(portfolioId, tenantId)).thenReturn(Optional.of(p));

        PortfolioDto result = portfolioService.getPortfolio(portfolioId);

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(portfolioRepository).findByIdAndTenantId(portfolioId, tenantId);
    }
}
