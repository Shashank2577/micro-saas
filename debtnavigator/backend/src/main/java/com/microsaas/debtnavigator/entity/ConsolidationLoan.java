package com.microsaas.debtnavigator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "consolidation_loans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidationLoan {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal apr;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "origination_fee", nullable = false, precision = 19, scale = 4)
    private BigDecimal originationFee;

    @Column(name = "max_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal maxAmount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
