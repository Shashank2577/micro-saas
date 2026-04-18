package com.microsaas.wealthedge.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthedge.domain.Asset;
import com.microsaas.wealthedge.domain.Portfolio;
import com.microsaas.wealthedge.repository.AssetRepository;
import com.microsaas.wealthedge.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Portfolio getPortfolio(UUID id) {
        return portfolioRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
    }

    @Transactional
    public Portfolio createPortfolio(Portfolio portfolio) {
        portfolio.setTenantId(TenantContext.require());
        portfolio.setTotalValue(BigDecimal.ZERO);
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio updatePortfolio(UUID id, Portfolio portfolioDetails) {
        Portfolio portfolio = getPortfolio(id);
        portfolio.setName(portfolioDetails.getName());
        portfolio.setDescription(portfolioDetails.getDescription());
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio addAssetToPortfolio(UUID portfolioId, UUID assetId) {
        Portfolio portfolio = getPortfolio(portfolioId);
        Asset asset = assetRepository.findByIdAndTenantId(assetId, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        if (!portfolio.getAssets().contains(asset)) {
            portfolio.getAssets().add(asset);
            recalculateTotalValue(portfolio);
        }
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio removeAssetFromPortfolio(UUID portfolioId, UUID assetId) {
        Portfolio portfolio = getPortfolio(portfolioId);
        portfolio.getAssets().removeIf(a -> a.getId().equals(assetId));
        recalculateTotalValue(portfolio);
        return portfolioRepository.save(portfolio);
    }

    private void recalculateTotalValue(Portfolio portfolio) {
        BigDecimal total = portfolio.getAssets().stream()
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        portfolio.setTotalValue(total);
    }

    @Transactional
    public void deletePortfolio(UUID id) {
        Portfolio portfolio = getPortfolio(id);
        portfolioRepository.delete(portfolio);
    }
}
