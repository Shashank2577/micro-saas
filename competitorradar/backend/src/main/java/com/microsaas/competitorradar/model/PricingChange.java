package com.microsaas.competitorradar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pricing_changes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingChange {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID competitorId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String planName;
    private OffsetDateTime detectedAt;
}
