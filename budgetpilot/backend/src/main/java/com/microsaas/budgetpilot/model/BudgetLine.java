package com.microsaas.budgetpilot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetLine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Budget budget;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String department;

    @Column(name = "allocated_amount", nullable = false)
    private BigDecimal allocatedAmount;

    @Column(name = "actual_amount", nullable = false)
    @Builder.Default
    private BigDecimal actualAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal variance = BigDecimal.ZERO;

    @Column(name = "variance_pct", nullable = false)
    @Builder.Default
    private BigDecimal variancePct = BigDecimal.ZERO;

    public void calculateVariance() {
        if (allocatedAmount != null && actualAmount != null) {
            this.variance = allocatedAmount.subtract(actualAmount);
            if (allocatedAmount.compareTo(BigDecimal.ZERO) > 0) {
                this.variancePct = this.variance.divide(allocatedAmount, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            } else {
                this.variancePct = BigDecimal.ZERO;
            }
        }
    }
}
