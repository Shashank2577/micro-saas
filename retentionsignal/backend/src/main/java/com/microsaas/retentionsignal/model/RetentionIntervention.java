package com.microsaas.retentionsignal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "retention_interventions")
@Data
public class RetentionIntervention {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "intervention_type", nullable = false)
    private InterventionType interventionType;

    @Column(name = "recommended_action")
    private String recommendedAction;

    @Enumerated(EnumType.STRING)
    private Outcome outcome;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
