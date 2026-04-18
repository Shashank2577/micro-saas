package com.microsaas.logisticsai.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "carrier_performance")
public class CarrierPerformance {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "carrier_name", nullable = false)
    private String carrierName;

    @Column(name = "on_time_rate")
    private Double onTimeRate;

    @Column(name = "predicted_delay_mins")
    private Integer predictedDelayMins;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public CarrierPerformance() {}

    public CarrierPerformance(UUID tenantId, String carrierName, Double onTimeRate, Integer predictedDelayMins) {
        this.tenantId = tenantId;
        this.carrierName = carrierName;
        this.onTimeRate = onTimeRate;
        this.predictedDelayMins = predictedDelayMins;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
    public Double getOnTimeRate() { return onTimeRate; }
    public void setOnTimeRate(Double onTimeRate) { this.onTimeRate = onTimeRate; }
    public Integer getPredictedDelayMins() { return predictedDelayMins; }
    public void setPredictedDelayMins(Integer predictedDelayMins) { this.predictedDelayMins = predictedDelayMins; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
