package com.microsaas.restaurantintel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class MenuItem {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private String category;
    private BigDecimal cost;
    private BigDecimal price;
    private Integer unitsSold;
    private BigDecimal profitMargin;
    private Instant createdAt;
    private Instant updatedAt;

    public MenuItem() {}

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        calculateMargin();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
        calculateMargin();
    }

    private void calculateMargin() {
        if (price != null && cost != null) {
            this.profitMargin = price.subtract(cost);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getUnitsSold() { return unitsSold; }
    public void setUnitsSold(Integer unitsSold) { this.unitsSold = unitsSold; }
    public BigDecimal getProfitMargin() { return profitMargin; }
    public void setProfitMargin(BigDecimal profitMargin) { this.profitMargin = profitMargin; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
