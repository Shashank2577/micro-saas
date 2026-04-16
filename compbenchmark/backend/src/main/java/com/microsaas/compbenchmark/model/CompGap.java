package com.microsaas.compbenchmark.model;

import com.crosscutting.tenancy.TenantListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "comp_gaps")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompGap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "gap_amount", nullable = false)
    private BigDecimal gapAmount;

    @Column(name = "gap_pct", nullable = false)
    private BigDecimal gapPct;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "recommended_range_min", nullable = false)
    private BigDecimal recommendedRangeMin;

    @Column(name = "recommended_range_max", nullable = false)
    private BigDecimal recommendedRangeMax;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
