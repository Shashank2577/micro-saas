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
@Table(name = "market_benchmarks")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(nullable = false)
    private BigDecimal p25;

    @Column(nullable = false)
    private BigDecimal p50;

    @Column(nullable = false)
    private BigDecimal p75;

    @Column(nullable = false)
    private BigDecimal p90;

    @Column
    private String source;

    @Column(name = "benchmarked_at", nullable = false)
    private LocalDate benchmarkedAt;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
}
