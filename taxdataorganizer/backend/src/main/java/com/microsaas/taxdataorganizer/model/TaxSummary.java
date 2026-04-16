package com.microsaas.taxdataorganizer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tax_summary", schema = "taxdataorganizer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tax_year_id", nullable = false)
    private UUID taxYearId;

    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue;

    @Column(name = "total_deductions", nullable = false)
    private BigDecimal totalDeductions;

    @Column(name = "net_taxable_income", nullable = false)
    private BigDecimal netTaxableIncome;

    @Column(name = "transaction_count", nullable = false)
    private int transactionCount;

    @Column(name = "document_count", nullable = false)
    private int documentCount;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
