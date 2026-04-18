package com.microsaas.churnpredictor.dto;




import java.math.BigDecimal;
import java.util.Map;


public class DashboardStatsDto {
    private BigDecimal averageHealthScore;
    private Map<String, Long> riskDistribution;
    private long totalCustomers;
    private long totalActiveInterventions;

    public BigDecimal getAverageHealthScore() {
        return averageHealthScore;
    }

    public void setAverageHealthScore(BigDecimal averageHealthScore) {
        this.averageHealthScore = averageHealthScore;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalActiveInterventions() {
        return totalActiveInterventions;
    }

    public void setTotalActiveInterventions(long totalActiveInterventions) {
        this.totalActiveInterventions = totalActiveInterventions;
    }
}
