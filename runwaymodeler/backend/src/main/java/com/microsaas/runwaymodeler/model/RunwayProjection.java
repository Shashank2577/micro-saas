package com.microsaas.runwaymodeler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "runway_projection")
@Data
public class RunwayProjection {
    @Id
    private UUID id;
    private UUID modelId;
    @jakarta.persistence.Column(name = "\"month\"")
    private LocalDate month;
    private BigDecimal projectedCash;
    private BigDecimal projectedBurn;
    private UUID tenantId;
}
