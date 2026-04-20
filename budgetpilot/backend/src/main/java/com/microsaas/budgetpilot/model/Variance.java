package com.microsaas.budgetpilot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "variances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_item_id", nullable = false)
    private BudgetItem budgetItem;

    @Column(name = "variance_amount", nullable = false)
    private BigDecimal varianceAmount;

    @Column(name = "variance_percentage", nullable = false)
    private BigDecimal variancePercentage;

    private String explanation;

    @Column(name = "date_calculated")
    private OffsetDateTime dateCalculated;

    @PrePersist
    protected void onCreate() {
        dateCalculated = OffsetDateTime.now();
    }
}
