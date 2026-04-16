package com.microsaas.runwaymodeler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "runway_model")
@Data
public class RunwayModel {
    @Id
    private UUID id;
    private String name;
    private BigDecimal currentBurn;
    private BigDecimal currentCash;
    private int runwayDays;
    private UUID tenantId;
    private LocalDateTime updatedAt;
}
