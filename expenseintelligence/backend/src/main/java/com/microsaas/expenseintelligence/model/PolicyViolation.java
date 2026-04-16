package com.microsaas.expenseintelligence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "policy_violation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "expense_id", nullable = false)
    private UUID expenseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "violation_type", nullable = false)
    private ViolationType violationType;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViolationStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
