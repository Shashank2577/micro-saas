package com.microsaas.vendormonitor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "value_amount")
    private BigDecimal valueAmount;

    @Column(name = "value_currency")
    private String valueCurrency;

    @Column(name = "sla_response_time_minutes")
    private Integer slaResponseTimeMinutes;

    @Column(name = "sla_uptime_percentage")
    private BigDecimal slaUptimePercentage;

    @Column(name = "auto_renew")
    private Boolean autoRenew;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = createdAt;
        if (valueCurrency == null) {
            valueCurrency = "USD";
        }
        if (autoRenew == null) {
            autoRenew = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getValueAmount() { return valueAmount; }
    public void setValueAmount(BigDecimal valueAmount) { this.valueAmount = valueAmount; }

    public String getValueCurrency() { return valueCurrency; }
    public void setValueCurrency(String valueCurrency) { this.valueCurrency = valueCurrency; }

    public Integer getSlaResponseTimeMinutes() { return slaResponseTimeMinutes; }
    public void setSlaResponseTimeMinutes(Integer slaResponseTimeMinutes) { this.slaResponseTimeMinutes = slaResponseTimeMinutes; }

    public BigDecimal getSlaUptimePercentage() { return slaUptimePercentage; }
    public void setSlaUptimePercentage(BigDecimal slaUptimePercentage) { this.slaUptimePercentage = slaUptimePercentage; }

    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
