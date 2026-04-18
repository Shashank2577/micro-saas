package com.microsaas.compensationos.dto;


import java.math.BigDecimal;
import java.util.UUID;


public class BenchmarkingResponse {
    private EmployeeCompDto employee;
    private MarketDataDto marketData;
    private BigDecimal compaRatio; // baseSalary / market p50
    private String rangePenetration; // where they fall in the range

    public EmployeeCompDto getEmployee() { return this.employee; }
    public void setEmployee(EmployeeCompDto employee) { this.employee = employee; }
    public MarketDataDto getMarketData() { return this.marketData; }
    public void setMarketData(MarketDataDto marketData) { this.marketData = marketData; }
    public BigDecimal getCompaRatio() { return this.compaRatio; }
    public void setCompaRatio(BigDecimal compaRatio) { this.compaRatio = compaRatio; }
    public String getRangePenetration() { return this.rangePenetration; }
    public void setRangePenetration(String rangePenetration) { this.rangePenetration = rangePenetration; }
}
