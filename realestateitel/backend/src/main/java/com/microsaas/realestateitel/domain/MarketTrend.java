package com.microsaas.realestateitel.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "market_trends")
@Data
@NoArgsConstructor
public class MarketTrend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "month_year", nullable = false)
    private LocalDate monthYear;

    @Column(name = "median_sale_price", nullable = false)
    private BigDecimal medianSalePrice;

    @Column(name = "days_on_market")
    private Integer daysOnMarket;

    @Column(name = "inventory_count")
    private Integer inventoryCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
