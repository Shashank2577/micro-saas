package com.microsaas.wealthedge.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthedge.domain.Asset;
import com.microsaas.wealthedge.domain.Portfolio;
import com.microsaas.wealthedge.repository.AssetRepository;
import com.microsaas.wealthedge.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;
    
    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void testCreatePortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setName("Main Portfolio");

        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(i -> {
            Portfolio saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Portfolio saved = portfolioService.createPortfolio(portfolio);

        assertNotNull(saved.getId());
        assertEquals(BigDecimal.ZERO, saved.getTotalValue());
        assertEquals(tenantId, saved.getTenantId());
    }

    @Test
    void testAddAssetToPortfolio() {
        UUID portfolioId = UUID.randomUUID();
        UUID assetId = UUID.randomUUID();

        Portfolio portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        Asset asset = new Asset();
        asset.setId(assetId);
        asset.setCurrentValue(new BigDecimal("1000"));

        when(portfolioRepository.findByIdAndTenantId(portfolioId, tenantId)).thenReturn(Optional.of(portfolio));
        when(assetRepository.findByIdAndTenantId(assetId, tenantId)).thenReturn(Optional.of(asset));
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(i -> i.getArgument(0));

        Portfolio updated = portfolioService.addAssetToPortfolio(portfolioId, assetId);

        assertEquals(1, updated.getAssets().size());
        assertEquals(new BigDecimal("1000"), updated.getTotalValue());
    }
}
