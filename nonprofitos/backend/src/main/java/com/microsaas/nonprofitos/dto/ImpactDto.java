package com.microsaas.nonprofitos.dto;

import java.math.BigDecimal;

public class ImpactDto {
    private String metricName;
    private BigDecimal metricValue;

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public BigDecimal getMetricValue() { return metricValue; }
    public void setMetricValue(BigDecimal metricValue) { this.metricValue = metricValue; }
}
