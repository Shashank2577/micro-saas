package com.microsaas.observabilitystack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "slo_compliance")
public class SLOCompliance {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "slo_id")
    private SLO slo;

    private Double compliancePercentage;
    private LocalDateTime measuredAt = LocalDateTime.now();
}
