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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pay_equity_audits")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayEquityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_dimension", nullable = false)
    private GroupDimension groupDimension;

    @Column(name = "avg_gap_pct", nullable = false)
    private BigDecimal avgGapPct;

    @Column(name = "flagged_count", nullable = false)
    private Integer flaggedCount;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    public enum GroupDimension {
        GENDER, ETHNICITY, TENURE
    }
}
