package com.microsaas.pricingintelligence.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "historical_data")
public class HistoricalData {
    @Id
    private UUID id;
    private UUID tenantId;
    private String customerId;
    private String dataType;
    private BigDecimal pricePoint;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
}
