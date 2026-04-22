package com.microsaas.investtracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class PortfolioAnalyticsDto {
    private BigDecimal totalValue;
    private BigDecimal ytdReturn;
    private Map<String, BigDecimal> allocation;
    private BigDecimal benchmarkComparison;
}