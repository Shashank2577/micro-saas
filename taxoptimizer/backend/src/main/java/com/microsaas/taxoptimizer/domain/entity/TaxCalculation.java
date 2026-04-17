package com.microsaas.taxoptimizer.domain.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tax_calculations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private TaxProfile profile;

    @Column(name = "tax_year", nullable = false)
    private Integer taxYear;

    @Column(name = "calculation_type", nullable = false)
    private String calculationType;

    @Column(name = "federal_liability", nullable = false, precision = 19, scale = 4)
    private BigDecimal federalLiability;

    @Column(name = "state_liability", nullable = false, precision = 19, scale = 4)
    private BigDecimal stateLiability;

    @Column(name = "total_liability", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalLiability;

    @Type(JsonType.class)
    @Column(name = "calculation_details", columnDefinition = "jsonb")
    private Map<String, Object> calculationDetails;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
