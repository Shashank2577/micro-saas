package com.microsaas.investtracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "ticker_symbol", nullable = false)
    private String tickerSymbol;

    private String name;

    @Column(name = "asset_class", nullable = false)
    private String assetClass;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "last_updated_at")
    private OffsetDateTime lastUpdatedAt;
}
