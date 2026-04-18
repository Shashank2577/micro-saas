package com.microsaas.investtracker.service;

import com.microsaas.investtracker.dto.CreatePortfolioRequest;
import com.microsaas.investtracker.dto.PortfolioDto;
import com.microsaas.investtracker.entity.Portfolio;
import com.microsaas.investtracker.repository.PortfolioRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public List<PortfolioDto> getAllPortfolios() {
        UUID tenantId = TenantContext.require();
        return portfolioRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PortfolioDto createPortfolio(CreatePortfolioRequest request) {
        UUID tenantId = TenantContext.require();
        Portfolio portfolio = new Portfolio();
        portfolio.setTenantId(tenantId);
        portfolio.setName(request.getName());
        portfolio.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
        portfolio.setTargetAllocation(request.getTargetAllocation());
        Portfolio saved = portfolioRepository.save(portfolio);
        return mapToDto(saved);
    }

    public PortfolioDto getPortfolio(UUID id) {
        UUID tenantId = TenantContext.require();
        return portfolioRepository.findByIdAndTenantId(id, tenantId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    private PortfolioDto mapToDto(Portfolio portfolio) {
        PortfolioDto dto = new PortfolioDto();
        dto.setId(portfolio.getId());
        dto.setName(portfolio.getName());
        dto.setCurrency(portfolio.getCurrency());
        dto.setTargetAllocation(portfolio.getTargetAllocation());
        return dto;
    }
}
