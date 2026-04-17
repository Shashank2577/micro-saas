package com.microsaas.retentionsignal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flight_risk_scores")
@Data
public class FlightRiskScore {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    private int score;

    @Column(name = "top_risk_factors")
    private String topRiskFactors;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
