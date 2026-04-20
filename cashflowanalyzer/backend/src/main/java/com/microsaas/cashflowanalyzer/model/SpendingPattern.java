package com.microsaas.cashflowanalyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Table(name = "spending_patterns")
public class SpendingPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "trend_percentage")
    private BigDecimal trendPercentage;

    @Column(name = "analysis_period")
    private String analysisPeriod;

    public SpendingPattern() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public BigDecimal getTrendPercentage() { return trendPercentage; }
    public void setTrendPercentage(BigDecimal trendPercentage) { this.trendPercentage = trendPercentage; }
    public String getAnalysisPeriod() { return analysisPeriod; }
    public void setAnalysisPeriod(String analysisPeriod) { this.analysisPeriod = analysisPeriod; }
}
