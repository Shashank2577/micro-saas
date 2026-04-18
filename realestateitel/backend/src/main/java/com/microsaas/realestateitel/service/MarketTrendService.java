package com.microsaas.realestateitel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.realestateitel.domain.MarketTrend;
import com.microsaas.realestateitel.repository.MarketTrendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketTrendService {

    private final MarketTrendRepository marketTrendRepository;

    public List<MarketTrend> getMarketTrends(String zipCode) {
        return marketTrendRepository.findByTenantIdAndZipCodeOrderByMonthYearDesc(TenantContext.require(), zipCode);
    }
}
